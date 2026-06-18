package com.team4.petstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleStockInsuficiente(StockInsuficienteException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse("Credenciales incorrectas", HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            if (errors.length() > 0) errors.append("; ");
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage());
        }
        return buildResponse(errors.toString(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse("Error interno del servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, int status) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        body.put("codigo", status);
        return ResponseEntity.status(status).body(body);
    }
}
