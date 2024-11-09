package com.example.megablissmainservice.Service.Impl;

import com.example.megablissmainservice.DTO.*;
import com.example.megablissmainservice.Config.AuthenticationConstants;
import com.example.megablissmainservice.DAO.mapper.UserMapper;
import com.example.megablissmainservice.Entity.RoleType;
import com.example.megablissmainservice.Entity.User;
import com.example.megablissmainservice.Entity.UserStatistic;
import com.example.megablissmainservice.Entity.UserType;
import com.example.megablissmainservice.Repository.UserRepository;
import com.example.megablissmainservice.Repository.UserStatisticRepository;
import com.example.megablissmainservice.Service.JWTService;
import com.example.megablissmainservice.Service.UserIService;
import com.example.megablissmainservice.exception.UserServiceCustomException;
import com.example.megablissmainservice.utility.EmailUtility;
import com.example.megablissmainservice.utility.ResponseUtil;
import com.example.megablissmainservice.utility.UserStatisticDailyRefreshUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserIService {
    @Autowired
    UserRepository ur;

    @Autowired
    UserStatisticRepository usr;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTService jwtService;

    @Autowired
    private EmailUtility emailUtility;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserStatisticDailyRefreshUtil userStatisticDailyRefreshUtil;

    private Random random = new Random();

    @Override
    public UserDTO createUserAccount(UserRegisterDTO userRegisterDTO) throws MessagingException {
        if (userRegisterDTO.getUserType() != UserType.CUSTOMER) {
            throw new UserServiceCustomException("Unsupported user type", "UNSUPPORTED_USER_TYPE",
                    HttpStatus.BAD_REQUEST);
        }
        userRegisterDTO.setUserType(UserType.CUSTOMER);
        return createAccount(userRegisterDTO, new HashSet<>(Arrays.asList(RoleType.USER)));
    }

    @Override
    public UserDTO createServiceProviderAccount(UserRegisterDTO userRegisterDTO) throws MessagingException {
        List<UserType> serviceProviderTypes = Arrays.asList(UserType.SP_HOTEL, UserType.SP_PACKAGE,
                UserType.SP_RESTURANT);
        if (!serviceProviderTypes.contains(userRegisterDTO.getUserType())) {
            throw new UserServiceCustomException("Unsupported service provider type", "UNSUPPORTED_SP_TYPE",
                    HttpStatus.BAD_REQUEST);
        }
        return createAccount(userRegisterDTO, new HashSet<>(Arrays.asList(RoleType.SERVICE_PROVIDER)));
    }

    @Override
    public UserDTO createAdminAccount(UserRegisterDTO userRegisterDTO) throws MessagingException {
        if (userRegisterDTO.getUserType() != UserType.ADMIN) {
            throw new UserServiceCustomException("Unsupported user type", "UNSUPPORTED_USER_TYPE",
                    HttpStatus.BAD_REQUEST);
        }
        return createAccount(userRegisterDTO, new HashSet<>(Arrays.asList(RoleType.ADMIN)));
    }

    private UserDTO createAccount(UserRegisterDTO userRegisterDTO, Set<RoleType> roles) throws MessagingException {
        // what the token field in the user object. where do you set that token and
        // tokenCreationTime fields
        if (ur.existsByEmail(userRegisterDTO.getEmail())) {
            throw new UserServiceCustomException("Email must be unique", "DUPLICATED_EMAIL");
        }
        User user = userMapper.mapToUser(userRegisterDTO);
        user.setDateCreation(Instant.now());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setValidate(false);
        user.setTokenToValidate(generateOTPToSend());
        user.setValidateCodeCreationDate(LocalDateTime.now());
        user.setRoleTypes(roles);
        User userSave = ur.save(user);
        UserDTO userDTO1 = userMapper.mapToUserDto(userSave);
        emailUtility.sendVerificationEmail(userRegisterDTO.getEmail(), userRegisterDTO.getUsername(),
                user.getTokenToValidate(), user.getRoleTypes());
        return userDTO1;
    }

    @Override
    public ResponseDto changePassword(ChangePasswordDTO changePasswordDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!Objects.equals(changePasswordDTO.getNewPassword(), changePasswordDTO.getConfirmPassword())) {
                throw new UserServiceCustomException("New password should be equal to confirmed password",
                        "BAD_REQUEST",
                        HttpStatus.BAD_REQUEST);
            }
            // check for the authentication object validity
            if (authentication == null || authentication.getDetails() == null) {
                throw new UserServiceCustomException("Authentication details are missing", "AUTHENTICATION_MISMATCH",
                        HttpStatus.FORBIDDEN);
            }
            // get user id, that we set in the authentication filter
            String authenticatedUserId = (String) authentication.getDetails();
            User user = ur.findByEmail(authenticatedUserId)
                    .orElseThrow(() -> new UserServiceCustomException(
                            "User Not Found for this logged email: " + authenticatedUserId,
                            "USER_NOT_FOUND", HttpStatus.NOT_FOUND));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticatedUserId, changePasswordDTO.getOldPassword()));
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            return responseUtil.createResponse("Password was changed successfully", "SUCCESS", "Password changed");
        } catch (AuthenticationException e) {
            throw new UserServiceCustomException("Old password didn't match", "OLD_PASSWORD_MISMATCH",
                    HttpStatus.FORBIDDEN);
        }
    }

    private Long generateOTPToSend() {
        int min = 10000;
        int max = 99999;
        return Long.valueOf(this.random.nextInt(max - min + 1) + min);
    }

    @Override
    public ResponseDto validateAccount(Long verificationCode, String email) throws MessagingException {
        User user = ur.findByEmail(email)
                .orElseThrow(() -> new UserServiceCustomException("User Not Found for this email: " + email,
                        "USER_NOT_FOUND", HttpStatus.NOT_FOUND));
        if (Boolean.FALSE.equals(user.getValidate()) && !user.getTokenToValidate().equals(verificationCode)) {
            throw new UserServiceCustomException("Invalid verification code!", "INVALID_CODE");
        }
        if (isTokenExpired(user.getValidateCodeCreationDate())) {
            user.setTokenToValidate(generateOTPToSend());
            user.setValidateCodeCreationDate(LocalDateTime.now());
            user = ur.save(user);
            emailUtility.sendVerificationEmail(user.getEmail(), user.getUsername(), user.getTokenToValidate(),
                    user.getRoleTypes());
            return responseUtil.createResponse("Current token experied!! Sent a new Token", "SUCCESS",
                    "Check your email :" + email
                            + " and OTP token to reset your password. If you didn't receive contact support team.");
        }
        if (Boolean.TRUE.equals(user.getValidate())) {
            return responseUtil.createResponse("Your account is already validated", "SUCCESS",
                    "Account already validated");
        }
        user.setValidate(true);
        user.setTokenToValidate(null);
        ur.save(user);
        return responseUtil.createResponse("Your account is validated", "SUCCESS", "Account validated successfully");
    }

    @Override
    public UserDTO updateAccount(UpdateUserDTO updateUserDTO, String email) {
        User user = ur.findByEmail(email)
                .orElseThrow(() -> new UserServiceCustomException("User Not Found for this email: " + email,
                        "USER_NOT_FOUND", HttpStatus.NOT_FOUND));
        user.setFirstName(updateUserDTO.getFirstName());
        user.setLastName(updateUserDTO.getLastName());
        user.setUsername(updateUserDTO.getUsername());
        user.setPhoneNumber(updateUserDTO.getPhoneNumber());
        user.setProfilePicture(updateUserDTO.getProfilePicture());
        User userSave = ur.save(user);
        return userMapper.mapToUserDto(userSave);
    }

    @Override
    public User getuserbyoken(String token) {
        String email = jwtService.extractUserEmail(token);
        return ur.findByEmail(email)
                .orElseThrow(() -> new UserServiceCustomException("User Not Found for this email: " + email,
                        "USER_NOT_FOUND", HttpStatus.NOT_FOUND));

    }

    public User getuserbutoken(@NonNull HttpServletRequest request) {
        final String autHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;
        jwt = autHeader.substring(7);
        email = jwtService.extractUserEmail(jwt);
        return ur.findByEmail(email)
                .orElseThrow(() -> new UserServiceCustomException("User Not Found for this email: " + email,
                        "USER_NOT_FOUND", HttpStatus.NOT_FOUND));

    }

    @Override
    public List<User> getAllAccount() {
        return (List<User>) ur.findAll();
    }

    @Override
    public ResponseDto forgotPassword(String email) throws MessagingException {
        User user = ur.findByEmail(email)
                .orElseThrow(() -> new UserServiceCustomException("No user found with this email: " + email,
                        "USER_NOT_FOUND", HttpStatus.NOT_FOUND));

        if (Boolean.FALSE.equals(user.getValidate())) {
            throw new UserServiceCustomException("You need to validate your account first.",
                    "ACCOUNT_NOT_VALID", HttpStatus.FORBIDDEN);
        }

        if (user.getTokenToForgotPassword() != null && !isTokenExpired(user.getTokenCreationDate())) {
            return responseUtil.createResponse("Your forgot password OTP was already sent", "OTP_ALREADY_SENT",
                    "Check your email :" + email + "and OTP token to reset your password");
        }
        user.setTokenToForgotPassword(generateOTPToSend());
        user.setTokenCreationDate(LocalDateTime.now());
        user = ur.save(user);
        emailUtility.sendForgetPasswordEmail(user.getEmail(), user.getUsername(), user.getTokenToForgotPassword());
        return responseUtil.createResponse("Your Token is successfully sended", "SUCCESS",
                "Check your email :" + email + "and OTP token to reset your password");

    }

    @Override
    public ResponseDto resetPassword(String email, Long token, String password) {
        User user = ur.findByEmail(email).orElseThrow(
                () -> new UserServiceCustomException("No user found with this email:" + email, "User_Not_Found"));

        if (!Objects.equals(user.getTokenToForgotPassword(), token)) {
            throw new UserServiceCustomException("Token Invalid", "Token invalid");
        }
        LocalDateTime tokenCreationDate = user.getTokenCreationDate();
        if (isTokenExpired(tokenCreationDate)) {
            return responseUtil.createResponse("Token expired", "Failed", "Token expired");
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setTokenToForgotPassword(null);
        user.setTokenCreationDate(null);
        ur.save(user);
        return responseUtil.createResponse("Password Updated", "SUCCESS", "Your password successfully updated.");
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);
        return diff.toMinutes() >= AuthenticationConstants.EXPIRE_TOKEN_AFTER_MINUTES;
    }

    @Override
    public JwtResponse generateToken(String email) {
        String token = null;
        User user = ur.findByEmail(email).orElseThrow(
                () -> new UserServiceCustomException("No user found with this email:" + email, "User_Not_Found"));
        if (Boolean.FALSE.equals(user.getValidate())) {
            throw new UserServiceCustomException("You need to validate your account first and check your email",
                    "ACCOUNT_NOT_VALID", HttpStatus.FORBIDDEN);
        } else {
            token = jwtService.generateToken(email);
            user.setLastLoginDateTime(LocalDateTime.now());
            UserResponse userResponse = userMapper.mapToUserResponse(user);

            return new JwtResponse(userResponse, token);
        }
    }

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    @Override
    public List<String> getRolefromToken(String token) {
        return jwtService.getRoleFromToken(token);
    }

    @Override
    public List<UserStatistic> getUserStatisticByDate(LocalDate startDate, LocalDate endDate) {

//        if the end date is not specified, set it to the start date, and vice versa
//        if both end date and start date are null, set them to yesterday date
        if(startDate == null && endDate != null){
            startDate = endDate;
        }else if(startDate != null && endDate == null){
            endDate = startDate;
        }else if(startDate == null && endDate == null){
            startDate = LocalDate.now().minusDays(1);
            endDate = startDate;
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("The end date should be after the start date!");
        }

//      calculate yesterday's user statistics data if the data is not present.
        if(!usr.existsUserStatisticByDate(LocalDate.now().minusDays(1))){
            userStatisticDailyRefreshUtil.calculateUserStatistics();
        }
        List<UserStatistic> userStatisticList = usr.findAllByDateBetween(startDate, endDate);

        return userStatisticList;
    }

    @Override
    public List<UserStatistic> updateUserStatisticByDate(LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("The end date should be after the start date!");
        }
//        should not update yesterday, today and future data by filling in with previous data
        if(!startDate.isBefore(LocalDate.now().minusDays(1)) || !endDate.isBefore(LocalDate.now().minusDays(1))){
            throw new IllegalArgumentException("Both start date and end date should be before yesterday!");
        }

//        get date list
        List<LocalDate> dateList = getDateList(startDate, endDate);

//        Update UserStatistics for the specified date range,
//        filling missing days of active users count by copying values from the previous available day
//        filling missing days of total users count and new users count by calculating the exact values
        for(LocalDate currentDate: dateList){
            if(!usr.existsUserStatisticByDate(currentDate)){
                LocalDate previousDate = currentDate.minusDays(1);
                while(!previousDate.isBefore(startDate) && !usr.existsUserStatisticByDate(previousDate)){
                    previousDate = previousDate.minusDays(1);
                }

                Optional<UserStatistic> previousUserStatisticOptional = usr.findByDate(previousDate);
                if(previousUserStatisticOptional.isPresent()){
                    UserStatistic previousUserStatistic = previousUserStatisticOptional.get();
                    UserStatistic newUserStatistic = new UserStatistic();
                    newUserStatistic.setDate(currentDate);

                    Instant startOfToday = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
                    List<User> existingUserList = ur.findAllByDateCreationBefore(startOfToday);
                    List<User> newUserList = ur.findAllByDateCreationBetween(
                            startOfToday.minus(1, ChronoUnit.DAYS),
                            startOfToday.minusSeconds(1));

                    newUserStatistic.setTotalUsersCount(existingUserList.size());
                    newUserStatistic.setNewUsersCount(newUserList.size());

                    newUserStatistic.setActiveUsersCount(previousUserStatistic.getActiveUsersCount());
                    usr.save(newUserStatistic);
                }
            }
        }

        List<UserStatistic> userStatisticList = usr.findAllByDateBetween(startDate, endDate);
        return userStatisticList;
    }

    private List<LocalDate> getDateList(LocalDate startDate, LocalDate endDate){
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = startDate;
        while(!currentDate.isAfter(endDate)){
            dateList.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dateList;
    }

}
