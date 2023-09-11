package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.dto.JwtResponseDTO;
import com.example.TicketingSystemBackend.dto.LoginRequestDTO;
import com.example.TicketingSystemBackend.dto.UserDTO;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        String jwt = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtResponseDTO(jwt));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
