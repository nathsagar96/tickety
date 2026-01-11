package com.tickety.controllers;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.requests.UpdateEventRequest;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.dtos.responses.PageResponse;
import com.tickety.services.EventService;
import com.tickety.utils.SecurityUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest createEventRequest) {

        UUID organizerId = securityUtils.getCurrentUserId();

        EventResponse response = eventService.createEvent(organizerId, createEventRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<EventResponse>> getAllEvents(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        UUID organizerId = securityUtils.getCurrentUserId();

        PageResponse<EventResponse> pageResponse = eventService.getAllEventsForOrganizer(organizerId, page, size);

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID eventId) {

        UUID organizerId = securityUtils.getCurrentUserId();

        EventResponse response = eventService.getEventForOrganizer(eventId, organizerId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable UUID eventId, @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        UUID organizerId = securityUtils.getCurrentUserId();

        EventResponse response = eventService.updateEventForOrganizer(eventId, organizerId, updateEventRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        UUID organizerId = securityUtils.getCurrentUserId();

        eventService.deleteEventForOrganizer(eventId, organizerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
