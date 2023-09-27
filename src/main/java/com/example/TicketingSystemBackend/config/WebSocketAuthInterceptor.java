package com.example.TicketingSystemBackend.config;

import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
@DependsOn("jwtUtil")
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private JwtUtil jwtUtil;

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = extractToken(request);
        if(token == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String email = jwtUtil.extractUsername(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        boolean hasManagerRole = userDetails.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_MANAGER".equals(auth.getAuthority()));

        if (jwtUtil.validateToken(token, userDetails) && hasManagerRole) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities()));
            return true;
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private String extractToken(ServerHttpRequest request) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(request.getURI());
        MultiValueMap<String, String> queryParams = uriComponentsBuilder.build().getQueryParams();
        List<String> tokenList = queryParams.get("token");
        if (tokenList != null && !tokenList.isEmpty()) {
            return tokenList.get(0);
        }
        return null;
    }

    @PostConstruct
    public void init() {
        System.out.println("WebSocketAuthInterceptor initialized");
    }
}
