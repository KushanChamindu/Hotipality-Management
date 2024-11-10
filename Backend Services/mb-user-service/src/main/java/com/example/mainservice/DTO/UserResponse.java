package com.example.mainservice.DTO;

import com.example.mainservice.Entity.UserType;

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
