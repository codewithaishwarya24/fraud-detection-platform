package com.fraud.transaction.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 *
 * <p>Centralizes exception handling logic and converts exceptions into {@link ApiError}
 * responses wrapped in {@link ResponseEntity}. Handles common Spring and Jakarta validation
 * exceptions as well as a catch-all for unexpected exceptions.</p>
 *
 * @see org.springframework.web.bind.annotation.RestControllerAdvice
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 * @see com.fraud.transaction.exception.ApiError
 * @see org.springframework.http.ResponseEntity
 * @see org.springframework.http.HttpStatus
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle not-found resources.
     *
     * <p>This handler responds with HTTP 404 (NOT_FOUND) when an {@link EntityNotFoundException}
     * is thrown by the application.</p>
     *
     * @param ex the {@link EntityNotFoundException} that was thrown
     * @param request the {@link HttpServletRequest} for the current request (used to capture the request URI)
     * @return a {@link ResponseEntity} containing an {@link ApiError} with HTTP status {@link HttpStatus#NOT_FOUND}
     * @see jakarta.persistence.EntityNotFoundException
     * @see com.fraud.transaction.exception.ApiError
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.debug("Entity not found: {}", ex.getMessage());
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle illegal arguments (bad input).
     *
     * <p>Returns HTTP 400 (BAD_REQUEST) with an {@link ApiError} when an {@link IllegalArgumentException}
     * is raised due to invalid input or state.</p>
     *
     * @param ex the {@link IllegalArgumentException} that was thrown
     * @param request the {@link HttpServletRequest} for the current request
     * @return a {@link ResponseEntity} containing an {@link ApiError} with HTTP status {@link HttpStatus#BAD_REQUEST}
     * @see java.lang.IllegalArgumentException
     * @see com.fraud.transaction.exception.ApiError
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        log.debug("Bad request: {}", ex.getMessage());
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle JSON parse errors / unreadable body.
     *
     * <p>Handles {@link HttpMessageNotReadableException} (malformed JSON or unreadable request body)
     * and returns HTTP 400 (BAD_REQUEST) with a generic message.</p>
     *
     * @param ex the {@link HttpMessageNotReadableException} that was thrown
     * @param request the {@link HttpServletRequest} for the current request
     * @return a {@link ResponseEntity} containing an {@link ApiError} with HTTP status {@link HttpStatus#BAD_REQUEST}
     * @see org.springframework.http.converter.HttpMessageNotReadableException
     * @see com.fraud.transaction.exception.ApiError
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed JSON request: {}", ex.getMessage());
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Malformed JSON request", request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle {@code @Valid} bean validation failures (request body).
     *
     * <p>Converts {@link MethodArgumentNotValidException} into an {@link ApiError} that contains
     * a list of field-level validation messages and returns HTTP 400 (BAD_REQUEST).</p>
     *
     * @param ex the {@link MethodArgumentNotValidException} containing binding and validation errors
     * @param request the {@link HttpServletRequest} for the current request
     * @return a {@link ResponseEntity} containing an {@link ApiError} with HTTP status {@link HttpStatus#BAD_REQUEST}
     * @see org.springframework.web.bind.MethodArgumentNotValidException
     * @see com.fraud.transaction.exception.ApiError
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> formatFieldError(fieldError))
                .collect(Collectors.toList());

        String message = "Validation failed for request body";
        log.debug("Validation failure: {} - {}", message, fieldErrors);

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
        error.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle validation errors coming from method parameters / path variables (ConstraintViolation).
     *
     * <p>Converts {@link ConstraintViolationException} into an {@link ApiError} listing the violated constraints
     * and returns HTTP 400 (BAD_REQUEST).</p>
     *
     * @param ex the {@link ConstraintViolationException} containing constraint violations
     * @param request the {@link HttpServletRequest} for the current request
     * @return a {@link ResponseEntity} containing an {@link ApiError} with HTTP status {@link HttpStatus#BAD_REQUEST}
     * @see jakarta.validation.ConstraintViolationException
     * @see com.fraud.transaction.exception.ApiError
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> violations = ex.getConstraintViolations()
                .stream()
                .map(this::formatConstraintViolation)
                .collect(Collectors.toList());

        String message = "Validation failed for request parameters";
        log.debug("Constraint violations: {}", violations);

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
        error.setFieldErrors(violations);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all for other exceptions.
     *
     * <p>Logs the unexpected exception and returns HTTP 500 (INTERNAL_SERVER_ERROR) with an {@link ApiError}.
     * Note: consider returning a less detailed message in production to avoid leaking internal details.</p>
     *
     * @param ex the unexpected {@link Exception} that was thrown
     * @param request the {@link HttpServletRequest} for the current request
     * @return a {@link ResponseEntity} containing an {@link ApiError} with HTTP status {@link HttpStatus#INTERNAL_SERVER_ERROR}
     * @see java.lang.Exception
     * @see com.fraud.transaction.exception.ApiError
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception caught: ", ex);

        // For production, you might want to return a less detailed message.
        String message = ex.getMessage() != null ? ex.getMessage() : "Internal server error";

        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ----------------- Helpers ----------------- */
    /**
     * Formats a field error into a readable string.
     *
     * <p>Converts a {@link FieldError} object into a string representation
     * in the format "fieldName: errorMessage". This is used to provide
     * user-friendly validation error messages.</p>
     *
     * @param fe the {@link FieldError} object containing the field name and error message
     * @return a formatted string in the format "fieldName: errorMessage"
     */
    private String formatFieldError(FieldError fe) {
        return fe.getField() + ": " + fe.getDefaultMessage();
    }

    /**
     * Formats a constraint violation into a readable string.
     *
     * <p>Converts a {@link ConstraintViolation} object into a string representation
     * in the format "propertyPath: errorMessage". This is used to provide
     * user-friendly validation error messages for request parameters or path variables.</p>
     *
     * @param cv the {@link ConstraintViolation} object containing the property path and error message
     * @return a formatted string in the format "propertyPath: errorMessage"
     */
    private String formatConstraintViolation(ConstraintViolation<?> cv) {
        // ConstraintViolation.getPropertyPath might look like "createTransaction.arg0.transactionId" depending on where validation happened.
        // Keep only the tail part for readability.
        String path = cv.getPropertyPath().toString();
        return path + ": " + cv.getMessage();
    }
}
