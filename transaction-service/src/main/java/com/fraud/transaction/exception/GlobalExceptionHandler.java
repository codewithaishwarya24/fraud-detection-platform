package com.fraud.transaction.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
//        String errorMessage = ex.getBindingResult()
//                                .getFieldErrors()
//                                .stream()
//                                .map(error -> error.getDefaultMessage())
//                                .findFirst()
//                                .orElse("Invalid input");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
}
