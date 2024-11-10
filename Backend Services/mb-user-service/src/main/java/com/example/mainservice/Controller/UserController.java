package com.example.mainservice.Controller;

import jakarta.mail.MessagingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.example.mainservice.DTO.*;
import com.example.mainservice.Entity.User;
import com.example.mainservice.Entity.UserStatistic;
import com.example.mainservice.Service.UserIService;
import com.example.mainservice.exception.UserServiceCustomException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user-service")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UserController {
    @Autowired
    UserIService uis;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/user/register")
    public UserDTO createUserAccount(@Valid @RequestBody UserRegisterDTO userRegisterDTO) throws MessagingException {
        return uis.createUserAccount(userRegisterDTO);
    }

    @PostMapping("/service-provider/register")
    public UserDTO createServiceProviderAccount(@Valid @RequestBody UserRegisterDTO userRegisterDTO)
            throws MessagingException {
        return uis.createServiceProviderAccount(userRegisterDTO);
    }

    @PostMapping("/admin/register")
    public UserDTO createAdminAccount(@Valid @RequestBody UserRegisterDTO userRegisterDTO) throws MessagingException {
        return uis.createAdminAccount(userRegisterDTO);
    }

    @PutMapping("/validate")
    public ResponseDto validateAccount(@Valid @RequestBody AccountValidateDTO accountValidateDTO)
            throws MessagingException {
        return uis.validateAccount(accountValidateDTO.getVerificationCode(), accountValidateDTO.getEmail());
    }

    @PostMapping("/token")
    public JwtResponse generateToken(@Valid @RequestBody LoginRequestDTO loginData) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword()));
            return uis.generateToken(loginData.getEmail());
        } catch (AuthenticationException e) {
            throw new UserServiceCustomException("Invalid Information", "BAD_LOGIN_CREDENTIALS");
        }
    }

    @PostMapping("/change-password")
    public ResponseDto changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        return uis.changePassword(changePasswordDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getrole")
    public List<String> getRolefromToken(@RequestParam String token) {
        return uis.getRolefromToken(token);
    }

    @GetMapping("/getSession")
    public User getuserbutoken(@NonNull HttpServletRequest request) {
        return uis.getuserbutoken(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getSessionByToken")
    public User getuserbyoken(@RequestParam("token") String token) {
        return uis.getuserbyoken(token);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllAccount")
    public List<User> getAllAccount() {
        return uis.getAllAccount();
    }

    @PostMapping("/forgot-password")
    public ResponseDto forgotPassword(
            @NotBlank(message = "Invalid Email: Empty Email") @NotNull(message = "Invalid Email: Email is NULL") @Email(message = "Invalid email") @RequestParam String email)
            throws MessagingException {
        return uis.forgotPassword(email);
    }

    @PutMapping("/reset-password")
    public ResponseDto resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return uis.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken(),
                resetPasswordRequest.getPassword());
    }

    @PutMapping("/updateAccount")
    public UserDTO updateAccount(
            @NotBlank(message = "Invalid Email: Empty Email") @NotNull(message = "Invalid Email: Email is NULL") @Email(message = "Invalid email") @RequestParam String email,
            @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        return uis.updateAccount(updateUserDTO, email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userStatistic/date")
    public List<UserStatistic> getUserStatisticByDate(
            @RequestParam(name = "startDate", required = false) @Past(message = "Start date must be in the past or today") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @Past(message = "End date must be in the past or today") LocalDate endDate) {
        List<UserStatistic> userStatisticList = uis.getUserStatisticByDate(startDate, endDate);
        return userStatisticList;
    }

    // only manually used when missing historical user statistic data before
    // yesterday
    // Update UserStatistics for the specified date range,
    // filling missing days of active users count by copying values from the
    // previous available day
    // filling missing days of total users count and new users count by calculating
    // the exact values
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/userStatistic/date")
    public List<UserStatistic> updateUserStatisticByDate(
            @RequestParam(name = "startDate", required = true) @Past(message = "Start date must be in the past") LocalDate startDate,
            @RequestParam(name = "endDate", required = true) @Past(message = "End date must be in the past") LocalDate endDate) {
        List<UserStatistic> userStatisticList = uis.updateUserStatisticByDate(startDate, endDate);
        return userStatisticList;
    }

}
