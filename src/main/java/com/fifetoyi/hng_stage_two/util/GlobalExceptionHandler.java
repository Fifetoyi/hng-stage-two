package com.fifetoyi.hng_stage_two.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, List<ValidationException.FieldError>>> handleValidationException(ValidationException ex) {
        Map<String, List<ValidationException.FieldError>> body = new HashMap<>();
        body.put("errors", ex.getErrors());
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
