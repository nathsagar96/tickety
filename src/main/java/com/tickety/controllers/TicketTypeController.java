package com.tickety.controllers;

import com.tickety.dtos.requests.CreateTicketTypeRequest;
import com.tickety.dtos.requests.UpdateTicketTypeRequest;
import com.tickety.dtos.responses.TicketTypeListResponse;
import com.tickety.dtos.responses.TicketTypeResponse;
import com.tickety.services.TicketTypeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/events/{eventId}/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TicketTypeResponse> createTicketType(
            @PathVariable UUID eventId, @Valid @RequestBody CreateTicketTypeRequest request) {
        log.info("Creating ticket type for event: {}", eventId);
        TicketTypeResponse response = ticketTypeService.createTicketType(eventId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TicketTypeListResponse>> getTicketTypes(@PathVariable UUID eventId) {
        log.info("Fetching ticket types for event: {}", eventId);
        List<TicketTypeListResponse> response = ticketTypeService.getTicketTypesByEvent(eventId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{ticketTypeId}")
    public ResponseEntity<TicketTypeResponse> getTicketTypeById(
            @PathVariable UUID eventId, @PathVariable UUID ticketTypeId) {
        log.info("Fetching ticket type: {} for event: {}", ticketTypeId, eventId);
        TicketTypeResponse response = ticketTypeService.getTicketTypeById(eventId, ticketTypeId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{ticketTypeId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<TicketTypeResponse> updateTicketType(
            @PathVariable UUID eventId,
            @PathVariable UUID ticketTypeId,
            @Valid @RequestBody UpdateTicketTypeRequest request) {
        log.info("Updating ticket type: {} for event: {}", ticketTypeId, eventId);
        TicketTypeResponse response = ticketTypeService.updateTicketType(eventId, ticketTypeId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{ticketTypeId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteTicketType(@PathVariable UUID eventId, @PathVariable UUID ticketTypeId) {
        log.info("Deleting ticket type: {} for event: {}", ticketTypeId, eventId);
        ticketTypeService.deleteTicketType(eventId, ticketTypeId);
        return ResponseEntity.noContent().build();
    }
}
