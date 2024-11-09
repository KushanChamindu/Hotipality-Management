package com.example.megablissmainservice.DAO.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.megablissmainservice.DTO.UserDTO;
import com.example.megablissmainservice.DTO.UserRegisterDTO;
import com.example.megablissmainservice.DTO.UserResponse;
import com.example.megablissmainservice.Entity.User;

import lombok.Getter;

@Component
@Getter
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    // convert User Jpa Entity into UserDTO
    public UserDTO mapToUserDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    // Convert UserDTO to User JPA Entity
    public User mapToUser(UserRegisterDTO userRegisterDTO) {
        return modelMapper.map(userRegisterDTO, User.class);
    }

    // Convert UserDTO to User JPA Entity
    public UserResponse mapToUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}