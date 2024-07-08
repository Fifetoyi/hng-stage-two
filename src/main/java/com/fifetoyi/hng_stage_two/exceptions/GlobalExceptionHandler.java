package com.fifetoyi.hng_stage_two.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, DuplicateResourceException.class})
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
        }

        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
