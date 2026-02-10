package com.hotel.management.service;

import com.hotel.management.dto.LoginRequest;
import com.hotel.management.dto.LoginResponse;
import com.hotel.management.model.User;
import com.hotel.management.repository.UserRepository;
import com.hotel.management.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
            
            return new LoginResponse(
                token,
                LoginResponse.UserDTO.fromUser(user),
                jwtUtil.getExpirationTime()
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}