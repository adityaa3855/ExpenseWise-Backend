package com.example.backend.service;

import com.example.backend.dto.AuthResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register User
    public String registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists!";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return "User Registered Successfully";
    }

    // Login User
    public AuthResponse loginUser(String email, String password) {

        var optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new AuthResponse("Invalid Email or Password", null);
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new AuthResponse("Invalid Email or Password", null);
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse("Login Successful", token);
    }

}