package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null && passwordEncoder.matches(password, user.getEncryptedPassword())) {
            return user;
        }
        return null;
    }
}
