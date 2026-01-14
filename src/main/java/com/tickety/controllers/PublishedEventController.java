package com.tickety.controllers;

import com.tickety.dtos.responses.PublishedEventResponse;
import com.tickety.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/published-events")
@RequiredArgsConstructor
@Tag(name = "Published Events", description = "Public APIs for accessing published events")
public class PublishedEventController {

    private final EventService eventService;

    @Operation(summary = "Get published events", description = "Retrieve a paginated list of all published events")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Published events retrieved successfully",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema =
                                                                @Schema(
                                                                        implementation =
                                                                                PublishedEventResponse.class))))
            })
    @GetMapping
    public ResponseEntity<Page<PublishedEventResponse>> getPublishedEvents(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching published events");
        Page<PublishedEventResponse> response = eventService.getPublishedEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get published event by ID",
            description = "Retrieve detailed information about a specific published event")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Published event retrieved successfully",
                        content = @Content(schema = @Schema(implementation = PublishedEventResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/{eventId}")
    public ResponseEntity<PublishedEventResponse> getPublishedEventById(@PathVariable UUID eventId) {
        log.info("Fetching published event: {}", eventId);
        PublishedEventResponse response = eventService.getPublishedEventById(eventId);
        return ResponseEntity.ok(response);
    }
}
