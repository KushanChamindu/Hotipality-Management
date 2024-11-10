package com.example.mainservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "[Users]")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;
    private String username;
    private Boolean validate;
    private Long tokenToValidate;
    private LocalDateTime validateCodeCreationDate;
    private Instant dateCreation;
    private Long tokenToForgotPassword;
    private String phoneNumber;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;
    private String profilePicture;
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roleTypes;
    private UserType userType;
    private LocalDateTime lastLoginDateTime;
}
