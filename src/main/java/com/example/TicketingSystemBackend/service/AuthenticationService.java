package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.UserRepository;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public Integer getDepartmentFromToken(String token) {
        return jwtUtil.extractDepartmentID(token);
    }

    public User getAuthenticatedUser(String token) {
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
