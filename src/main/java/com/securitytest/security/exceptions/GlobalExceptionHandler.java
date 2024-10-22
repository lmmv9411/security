package com.securitytest.security.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.securitytest.security.exceptions.customs.BadRequestException;
import com.securitytest.security.exceptions.customs.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        final var message = new StringBuilder();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> message.append(error.getDefaultMessage()).append(" "));

        return createErrorResponse(message.toString().trim(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {

        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {

        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex) {

        return createErrorResponse(
                "Acceso denegado al recurso solicitado.",
                HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMessageNotReadable(
            HttpMessageNotReadableException ex) {

        return createErrorResponse("Json mal formado", HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("null")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(
            DataIntegrityViolationException ex) {

        String messageError = null;

        if (ex.getRootCause() != null) {
            messageError = ex.getRootCause().getMessage();
            int index = messageError.indexOf("for key");
            if (index != -1) {
                messageError = messageError.substring(0, index);
            }
        }

        return createErrorResponse(
                messageError != null ? messageError : "Datos duplicados.",
                HttpStatus.BAD_REQUEST);

    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("code", status.value());
        errors.put("timestamp", LocalDateTime.now());
        errors.put("message", message);
        return new ResponseEntity<>(errors, status);
    }
}
