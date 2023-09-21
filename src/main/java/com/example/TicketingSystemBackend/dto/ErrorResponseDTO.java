package com.example.TicketingSystemBackend.dto;

public class ErrorResponseDTO {

    private String errorMessage;
    private Integer statusCode;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorResponseDTO(String errorMessage, Integer statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
