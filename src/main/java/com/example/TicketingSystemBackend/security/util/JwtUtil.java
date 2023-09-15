package com.example.TicketingSystemBackend.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.TicketingSystemBackend.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "d0GysNffHo";

    private DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public Integer extractRoleID(String token) {
        return decodeToken(token).getClaim("roleID").asInt();
    }

    public Integer extractDepartmentID(String token) {
        return decodeToken(token).getClaim("departmentID").asInt();
    }


    public Date extractExpiration(String token) {
        return decodeToken(token).getExpiresAt();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, UserDTO userDTO) {
        JWTCreator.Builder builder = JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("roleID", userDTO.getRoleID())
                .withClaim("departmentID", userDTO.getDepartmentID())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10));  // Token validity

        return builder.sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            DecodedJWT jwt = decodeToken(token);
            return jwt.getSubject().equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
