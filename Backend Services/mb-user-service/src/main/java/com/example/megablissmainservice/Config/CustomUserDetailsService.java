package com.example.megablissmainservice.Config;

import com.example.megablissmainservice.Entity.User;
import com.example.megablissmainservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository ur;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> credential = ur.findByEmail(email);
        return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("usernotfound"));
    }
}