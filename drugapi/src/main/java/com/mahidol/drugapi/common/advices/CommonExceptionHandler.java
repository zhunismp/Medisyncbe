package com.mahidol.drugapi.common.advices;

import com.mahidol.drugapi.common.exceptions.BindingError;
import com.mahidol.drugapi.common.exceptions.InternalServerError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<?> internalServerErrorHandler(InternalServerError e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", e.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindingError.class)
    public ResponseEntity<?> invalidRequestHandler(BindingError e) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : e.getMessages()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(Map.of("errorMessages", errorMap), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> emptyResultDataAccessExceptionHandler(EmptyResultDataAccessException e) {
        return new ResponseEntity<>(Map.of("errorMessages", "Drug id doesn't exists"), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EntityNotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
        return new ResponseEntity<>(Map.of("errorMessages", e), HttpStatus.BAD_REQUEST);
    }
}
