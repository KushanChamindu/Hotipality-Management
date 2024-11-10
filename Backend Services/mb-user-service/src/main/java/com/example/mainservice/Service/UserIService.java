package com.example.mainservice.Service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mainservice.DTO.*;
import com.example.mainservice.Entity.User;
import com.example.mainservice.Entity.UserStatistic;

import java.time.LocalDate;
import java.util.List;

public interface UserIService {
    public UserDTO createUserAccount(UserRegisterDTO userRegisterDTO) throws MessagingException;

    public UserDTO createServiceProviderAccount(UserRegisterDTO userRegisterDTO) throws MessagingException;

    public UserDTO createAdminAccount(UserRegisterDTO userRegisterDTO) throws MessagingException;

    public JwtResponse generateToken(String email);

    public void validateToken(String token);

    public List<String> getRolefromToken(String token);

    public User getuserbutoken(@NonNull HttpServletRequest request);

    public List<User> getAllAccount();

    public ResponseDto forgotPassword(String email) throws MessagingException;

    public ResponseDto resetPassword(String email, Long token, String password);

    public ResponseDto validateAccount(Long verificationCode, String email) throws MessagingException;

    public UserDTO updateAccount(UpdateUserDTO updateUserDTO, String email);

    public User getuserbyoken(@RequestParam("token") String token);

    public ResponseDto changePassword(ChangePasswordDTO changePasswordDTO);

    public List<UserStatistic> getUserStatisticByDate(LocalDate startDate, LocalDate endDate);

    public List<UserStatistic> updateUserStatisticByDate(LocalDate startDate, LocalDate endDate);
}
