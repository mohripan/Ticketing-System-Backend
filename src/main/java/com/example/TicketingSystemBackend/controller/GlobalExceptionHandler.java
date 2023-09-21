package com.example.TicketingSystemBackend.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.TicketingSystemBackend.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(StackOverflowError.class)
    public ResponseEntity<String> handleStackOverflowError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Stack overflow error occurred!");
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("message", ex.getMessage());
        errorBody.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<ErrorResponseDTO> handleJWTDecodeException(JWTDecodeException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
