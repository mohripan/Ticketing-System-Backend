package com.example.TicketingSystemBackend.dto;

public class JwtResponseDTO {

    private String jwtToken;

    public JwtResponseDTO(String token) {
        this.jwtToken = token;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
