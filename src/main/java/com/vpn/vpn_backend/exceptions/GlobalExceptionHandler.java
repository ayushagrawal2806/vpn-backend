package com.vpn.vpn_backend.exceptions;


import com.vpn.vpn_backend.common.ErrorResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex){
        return buildErrorResponse(
                ex.getMessage(),
                "RESOURCE_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                null
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex){
        return buildErrorResponse(
                "Authentication failed",
                "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED,
                null
        );
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex){
        return buildErrorResponse(
                "Invalid or expired token",
                "JWT_ERROR",
                HttpStatus.UNAUTHORIZED,
                null
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex){
        return buildErrorResponse(
                ex.getMessage(),
                "FORBIDDEN",
                HttpStatus.FORBIDDEN,
                null
        );
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return buildErrorResponse(
                "Validation failed",
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST,
                errors
        );
    }




    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex){
        return buildErrorResponse(
                ex.getMessage(),
                "INVALID_STATE",
                HttpStatus.BAD_REQUEST,
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        return buildErrorResponse(
                ex.getMessage(),
                "BAD_REQUEST",
                HttpStatus.BAD_REQUEST,
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex){
        return buildErrorResponse(
                "Something went wrong",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        );
    }



    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String message,
            String errorCode,
            HttpStatus status,
            Map<String,String> errors
    ){

        ErrorResponse error = ErrorResponse.builder()
                .message(message)
                .errorCode(errorCode)
                .status(status.value())
                .errors(errors)
                .build();

        return new ResponseEntity<>(error, status);
    }
}