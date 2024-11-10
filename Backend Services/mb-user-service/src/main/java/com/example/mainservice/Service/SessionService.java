package com.example.mainservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.mainservice.Entity.User;
import com.example.mainservice.Repository.UserRepository;

import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    UserRepository userrRepository;

    public Optional<User> getUserBySession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String mail = userDetails.getUsername();
            return userrRepository.findByEmail(mail);
        } else {
            return null; // or throw an exception, depending on your requirements
        }
    }
}
