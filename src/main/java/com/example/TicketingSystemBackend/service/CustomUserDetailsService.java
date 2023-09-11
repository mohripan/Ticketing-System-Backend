package com.example.TicketingSystemBackend.service;

import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.model.UserRole;
import com.example.TicketingSystemBackend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        UserRole userRole = UserRole.fromRoleId(user.getRole().getRoleId());

        System.out.println(userRole.name());

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.name()));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getEncryptedPassword())
                .authorities(authorities)
                .build();
    }
}
