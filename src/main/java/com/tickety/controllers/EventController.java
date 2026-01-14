package com.tickety.controllers;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.requests.UpdateEventRequest;
import com.tickety.dtos.responses.EventListResponse;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.services.EventService;
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
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        log.info("Creating event: {}", request.name());
        EventResponse response = eventService.createEvent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Page<EventListResponse>> getMyEvents(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching my events");
        Page<EventListResponse> response = eventService.getMyEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> getEventById(@PathVariable UUID eventId) {
        log.info("Fetching event: {}", eventId);
        EventResponse response = eventService.getEventById(eventId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable UUID eventId, @Valid @RequestBody UpdateEventRequest request) {
        log.info("Updating event: {}", eventId);
        EventResponse response = eventService.updateEvent(eventId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        log.info("Deleting event: {}", eventId);
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
