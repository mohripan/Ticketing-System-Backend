package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.dto.JwtResponseDTO;
import com.example.TicketingSystemBackend.dto.LoginRequestDTO;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.TokenService;
import com.example.TicketingSystemBackend.service.UserService;
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

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('VIEW_USER')")
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
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        String jwt = token.split(" ")[1];

        String email = jwtUtil.extractUsername(jwt);

        tokenService.invalidateToken(email);

        return ResponseEntity.ok().body("Logged out successfully");
    }
}
