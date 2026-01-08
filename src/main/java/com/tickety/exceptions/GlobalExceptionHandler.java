package com.tickety.exceptions;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex) {
        log.error("Business exception: {}", ex.getMessage());

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setType(URI.create("https://api.tickety.com/errors/business-error"));

        return problemDetail;
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("UserNotFoundException: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Not Found");
        problemDetail.setType(URI.create("https://api.tickety.com/errors/not-found"));

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.tickety.com/errors/internal-error"));

        return problemDetail;
    }
}
