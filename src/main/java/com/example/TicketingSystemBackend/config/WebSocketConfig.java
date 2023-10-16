package com.example.TicketingSystemBackend.config;

import com.example.TicketingSystemBackend.model.Customer;
import com.example.TicketingSystemBackend.model.User;
import com.example.TicketingSystemBackend.repository.CustomerRepository;
import com.example.TicketingSystemBackend.security.util.JwtUtil;
import com.example.TicketingSystemBackend.service.CustomUserDetailsService;
import com.example.TicketingSystemBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/replies/websocket")
                .setAllowedOrigins("http://localhost:5173")
                .setHandshakeHandler(handshakeHandler())
                .addInterceptors(webSocketAuthInterceptor)
                .withSockJS();
    }

    private DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request,
                                              WebSocketHandler wsHandler,
                                              Map<String, Object> attributes) {
                String tokenParam = request.getURI().getQuery();
                String tokenOrEmail = tokenParam.split("=")[1];

                // Case: User (Token provided)
                if (jwtUtil.isToken(tokenOrEmail)) {
                    String userEmail = jwtUtil.extractUsername(tokenOrEmail);
                    User authenticatedUser = userService.getUserByEmail(userEmail);
                    if (authenticatedUser != null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                        if (jwtUtil.validateToken(tokenOrEmail, userDetails)) {
                            return new UsernamePasswordAuthenticationToken(
                                    authenticatedUser,
                                    null,
                                    userDetails.getAuthorities());
                        }
                    }
                }
                // Case: Customer (Email provided)
                else {
                    Customer customer = customerRepository.findByEmail(tokenOrEmail);
                    if (customer != null) {
                        return new Principal() {
                            @Override
                            public String getName() {
                                return customer.getEmail();
                            }
                        };
                    }
                }
                return null;
            }
        };
    }

}
