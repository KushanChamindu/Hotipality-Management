package com.example.megablissmainservice.DTO;

import com.example.megablissmainservice.DTO.annotation.Password;
import com.example.megablissmainservice.Entity.UserType;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {

    @NotBlank(message = "Invalid Email: Empty Email")
    @NotNull(message = "Invalid Email: Email is NULL")
    @Email(message = "Invalid email")
    private String email;

    @Password
    private String password;

    @NotBlank(message = "Invalid firstName: Empty firstName")
    @NotNull(message = "Invalid firstName: firstName is NULL")
    @Size(min = 3, max = 30, message = "Invalid firstName: Must be of 3 - 30 characters")
    private String firstName;

    @NotBlank(message = "Invalid lastName: Empty lastName")
    @NotNull(message = "Invalid lastName: lastName is NULL")
    @Size(min = 3, max = 30, message = "Invalid lastName: Must be of 3 - 30 characters")
    private String lastName;

    @NotBlank(message = "Invalid username: Empty username")
    @NotNull(message = "Invalid username: username is NULL")
    @Size(min = 3, max = 30, message = "Invalid username: Must be of 3 - 30 characters")
    private String username;

    @Pattern(regexp = "^(?:\\+\\d{2,3}\\d{9}|0\\d{9})$", message = "Phone number is invalid")
    private String phoneNumber;
    private String profilePicture;
    @NotNull(message = "Invalid user type: userType is NULL")
    private UserType userType;
}
