package com.tickety.controllers;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.dtos.responses.PageResponse;
import com.tickety.services.EventService;
import com.tickety.utils.SecurityUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest createEventRequest) {

        UUID userId = securityUtils.getCurrentUserId();

        return new ResponseEntity<>(eventService.createEvent(userId, createEventRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<EventResponse>> getAllEvents(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        UUID userId = securityUtils.getCurrentUserId();

        PageResponse<EventResponse> events = eventService.getAllEventsForUser(userId, page, size);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
