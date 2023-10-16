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
        String tokenOrEmail = extractTokenOrEmail(request);

        if(tokenOrEmail == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        if (jwtUtil.isToken(tokenOrEmail)) {

            String email = jwtUtil.extractUsername(tokenOrEmail);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            boolean hasStaffRole = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> "ROLE_STAFF".equals(auth.getAuthority()));

            if (jwtUtil.validateToken(tokenOrEmail, userDetails) && hasStaffRole) {

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities()));
                return true;
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private String extractTokenOrEmail(ServerHttpRequest request) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(request.getURI());
        MultiValueMap<String, String> queryParams = uriComponentsBuilder.build().getQueryParams();

        // Extract token or email parameter from the request
        List<String> tokenList = queryParams.get("token");
        List<String> emailList = queryParams.get("email");

        if (tokenList != null && !tokenList.isEmpty()) {
            return tokenList.get(0);
        } else if (emailList != null && !emailList.isEmpty()) {
            return emailList.get(0);
        }
        return null;
    }

    @PostConstruct
    public void init() {
        System.out.println("WebSocketAuthInterceptor initialized");
    }
}
