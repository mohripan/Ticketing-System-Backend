package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.dto.UserDTO;
import com.example.TicketingSystemBackend.model.Department;
import com.example.TicketingSystemBackend.model.Role;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.DepartmentRepository;
import com.example.TicketingSystemBackend.repository.RoleRepository;
import com.example.TicketingSystemBackend.repository.UserRepository;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
    CustomUserDetailsService userDetailsService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        if(passwordEncoder.matches(password, user.getEncryptedPassword())) {
            UserDTO userDTO = convertToDTO(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
            return jwtUtil.generateToken(userDetails, userDTO);
        } else {
            throw new RuntimeException("Incorrect Password");
        }
    }

    public UserDTO convertToDTO(User user) {

        return modelMapper.map(user, UserDTO.class);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
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
            user.setRole(role);
        }

        return userRepository.save(user);
    }
}
