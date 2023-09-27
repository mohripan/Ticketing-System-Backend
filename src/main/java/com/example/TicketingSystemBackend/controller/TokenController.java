package com.example.TicketingSystemBackend.controller;

import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.CustomUserDetailsService;
import com.example.TicketingSystemBackend.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class TokenController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestBody Map<String, String> tokenPayload) {
        String token = tokenPayload.get("token");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("isValid", false));
        }

        try {
            String username = jwtUtil.extractUsername(token);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // If you store JWTs in Redis upon login, you'd check its presence here.
            if (!redisService.exists(username)) {
                return ResponseEntity.ok(Map.of("isValid", false));
            }

            // Validate the token's structure, signature, and expiry
            if (!jwtUtil.validateToken(token, userDetails)) {
                return ResponseEntity.ok(Map.of("isValid", false));
            }

        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("isValid", false));
        }

        return ResponseEntity.ok(Map.of("isValid", true));
    }
}
