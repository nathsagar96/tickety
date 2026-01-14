package com.tickety.controllers;

import com.tickety.dtos.requests.ValidateTicketRequest;
import com.tickety.dtos.responses.ValidationListResponse;
import com.tickety.dtos.responses.ValidationResponse;
import com.tickety.services.ValidationService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/events/{eventId}/validations")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ValidationResponse> validateTicket(
            @PathVariable UUID eventId, @Valid @RequestBody ValidateTicketRequest request) {
        log.info("Validating ticket for event: {}", eventId);
        ValidationResponse response = validationService.validateTicket(eventId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STAFF', 'ORGANIZER')")
    public ResponseEntity<Page<ValidationListResponse>> getEventValidations(
            @PathVariable UUID eventId, @PageableDefault(size = 50) Pageable pageable) {
        log.info("Fetching validations for event: {}", eventId);
        Page<ValidationListResponse> response = validationService.getEventValidations(eventId, pageable);
        return ResponseEntity.ok(response);
    }
}
