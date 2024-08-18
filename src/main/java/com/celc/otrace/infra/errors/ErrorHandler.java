package com.celc.otrace.infra.errors;

import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.celc.otrace.domain.utils.ErrorDto;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> genericError(ResponseStatusException e) {
        return createErrorDto(e.getStatusCode().value(), e.getMessage());
    }

    private ResponseEntity<ErrorDto> createErrorDto(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorDto(status.value(), message));
    }


    private ResponseEntity<ErrorDto> createErrorDto(int status, String message) {
        return ResponseEntity.status(status).body(new ErrorDto(status, message));
    }

    
}
