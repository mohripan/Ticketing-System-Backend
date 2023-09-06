package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User login(String email, String password) {
        // TODO: Encrypt the provided password and match with DB's encryptedPassword
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null && user.getEncryptedPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
