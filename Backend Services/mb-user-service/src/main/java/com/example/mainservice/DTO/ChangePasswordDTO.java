package com.example.mainservice.DTO;

import com.example.mainservice.DTO.annotation.Password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDTO {

    @Password
    private String oldPassword;

    @Password
    private String newPassword;

    @Password
    private String confirmPassword;
}
