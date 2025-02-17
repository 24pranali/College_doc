package com.example.demo_pranali.service;

import com.example.demo_pranali.Model.AuthUser;
import com.example.demo_pranali.repository.AuthUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthUserService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public AuthUser registerUser(AuthUser user) {
        // Check if the email already exists
        Optional<AuthUser> existingUser = authUserRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this email already exists!");
        }

        // Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authUserRepository.save(user);
    }

    // Find user by email for login
    public Optional<AuthUser> findByEmail(String email) {
        return authUserRepository.findByEmail(email);
    }

    // Verify if the password matches the hashed password
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
