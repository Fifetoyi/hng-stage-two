package com.fifetoyi.hng_stage_two.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, DuplicateResourceException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleExceptions(Exception ex) {
        List<Map<String, String>> errors = new ArrayList<>();

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationEx = (MethodArgumentNotValidException) ex;
            for (FieldError fieldError : validationEx.getBindingResult().getFieldErrors()) {
                Map<String, String> error = new HashMap<>();
                error.put("field", fieldError.getField());
                error.put("message", fieldError.getDefaultMessage());
                errors.add(error);
            }
        } else if (ex instanceof DuplicateResourceException) {
            Map<String, String> error = new HashMap<>();
            error.put("field", "email");
            error.put("message", ex.getMessage());
            errors.add(error);
        } else if (ex instanceof HttpMessageNotReadableException) {
            List<String> missingFields = Arrays.asList("firstName", "lastName", "email", "password");
                for (String field : missingFields) {
                    Map<String, String> error = new HashMap<>();
                    error.put("field", field);
                    error.put("message", field + " is required");
                    errors.add(error);
                }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public final ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
//        String errorMessage = "Request body is missing or invalid";
//        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
//    }
//
//    class ErrorResponse {
//        private HttpStatus status;
//        private String message;
//
//        public ErrorResponse(HttpStatus status, String message) {
//            this.status = status;
//            this.message = message;
//        }
//
//        // Getters and setters
//    }

}
