package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.dto.CreateUserDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private TokenService tokenService;

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
            String jwt = jwtUtil.generateToken(userDetails, userDTO);

            tokenService.saveToken(email, jwt);

            return jwt;
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

    public User createUser(CreateUserDTO request) {
        User user = new User();

        boolean findDepartmentByID = true; // for demonstration only

        user.setEmail(request.getEmail());
        user.setEncryptedPassword(passwordEncoder.encode(request.getEncryptedPassword()));
        user.setUserName(request.getUserName());
        user.setName(request.getName());

        if(findDepartmentByID) {
            // This code is to set the department based on the Auth User
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            User authenticatedUser = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

            user.setDepartment(authenticatedUser.getDepartment());

            // STAFF Role
            Role role = roleRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }

        else {
            // Set department and role manually in .json body
            Department department = departmentRepository.findById(request.getDepartmentID())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            user.setDepartment(department);

            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }

        return userRepository.save(user);
    }
}
