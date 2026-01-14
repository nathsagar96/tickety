package com.tickety.exceptions;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_ERROR_URL = "https://api.tickety.com/errors";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/not-found"));
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex) {
        log.error("Business rule violation: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/business-rule-violation"));
        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketSoldOutException.class)
    public ResponseEntity<ProblemDetail> handleTicketSoldOut(TicketSoldOutException ex) {
        log.error("Ticket sold out: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/ticket-sold-out"));
        problemDetail.setTitle("Ticket Sold Out");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SalesWindowException.class)
    public ResponseEntity<ProblemDetail> handleSalesWindow(SalesWindowException ex) {
        log.error("Sales window error: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/sales-window-error"));
        problemDetail.setTitle("Sales Window Error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEventStatusException.class)
    public ResponseEntity<ProblemDetail> handleInvalidEventStatus(InvalidEventStatusException ex) {
        log.error("Invalid event status: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/invalid-event-status"));
        problemDetail.setTitle("Invalid Event Status");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        log.error("Unauthorized access: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/unauthorized-access"));
        problemDetail.setTitle("Unauthorized Access");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, "You don't have permission to access this resource");
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/access-denied"));
        problemDetail.setTitle("Access denied");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TicketValidationException.class)
    public ResponseEntity<ProblemDetail> handleTicketValidation(TicketValidationException ex) {
        log.error("Ticket validation error: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/ticket-validation-error"));
        problemDetail.setTitle("Ticket Validation Error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<ProblemDetail> handleQrCodeGeneration(QrCodeGenerationException ex) {
        log.error("QR code generation error: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate QR code");
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/qr-code-generation-error"));
        problemDetail.setTitle("QR Code Generation Error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());

        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + ": " + errorMessage);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation error");
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/validation-error"));
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
        problemDetail.setType(URI.create(BASE_ERROR_URL + "/generic-error"));
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
