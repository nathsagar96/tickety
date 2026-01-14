package com.tickety.controllers;

import com.tickety.dtos.responses.PublishedEventResponse;
import com.tickety.services.EventService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<PublishedEventResponse>> getPublishedEvents(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching published events");
        Page<PublishedEventResponse> response = eventService.getPublishedEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<PublishedEventResponse> getPublishedEventById(@PathVariable UUID eventId) {
        log.info("Fetching published event: {}", eventId);
        PublishedEventResponse response = eventService.getPublishedEventById(eventId);
        return ResponseEntity.ok(response);
    }
}
