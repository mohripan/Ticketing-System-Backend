package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.DepartmentRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null && passwordEncoder.matches(password, user.getEncryptedPassword())) {
            return user;
        }
        return null;
    }

    public User createUser(User user) {
        user.setEncryptedPassword(passwordEncoder.encode(user.getEncryptedPassword()));

        if(user.getDepartment() != null && user.getDepartment().getDepartmentID() != null) {
            Department department = departmentRepository.findById(user.getDepartment().getDepartmentID())
                    .orElseThrow(() -> new RuntimeException("Department Not Found"));
            user.setDepartment(department);
        }

        return userRepository.save(user);
    }
}
