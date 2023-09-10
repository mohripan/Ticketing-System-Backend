package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.Role;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.DepartmentRepository;
import com.example.TicketingSystemBackend.repository.RoleRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email Not Found"));
        if(passwordEncoder.matches(password, user.getEncryptedPassword())) {
            return user;
        } else {
            throw new RuntimeException("Incorrect Password");
        }
    }

    public User createUser(User user) {
        user.setEncryptedPassword(passwordEncoder.encode(user.getEncryptedPassword()));

        if(user.getDepartment() != null && user.getDepartment().getDepartmentID() != null) {
            Department department = departmentRepository.findById(user.getDepartment().getDepartmentID())
                    .orElseThrow(() -> new RuntimeException("Department Not Found"));
            user.setDepartment(department);
        }

        if(user.getRole() != null && user.getRole().getRoleId() != null) {
            Role role = roleRepository.findById(user.getRole().getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role Not Found"));
        }

        return userRepository.save(user);
    }
}
