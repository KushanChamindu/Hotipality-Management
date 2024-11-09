package com.example.megablissmainservice.DTO;

import com.example.megablissmainservice.Entity.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private UserType userType;
}
