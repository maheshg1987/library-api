package com.libraryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("errors", errorMessages);
        errors.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.put("error", "Validation Error");
        response.put("messages", ex.getFieldErrors());
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
