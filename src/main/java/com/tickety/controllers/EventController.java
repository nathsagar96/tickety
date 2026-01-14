package com.tickety.controllers;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.requests.UpdateEventRequest;
import com.tickety.dtos.responses.EventListResponse;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Event management APIs for organizers")
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Create a new event", description = "Create a new event with the provided details")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Event created successfully",
                        content = @Content(schema = @Schema(implementation = EventResponse.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Business rule violation",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - Organizer role required",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        log.info("Creating event: {}", request.name());
        EventResponse response = eventService.createEvent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get my events",
            description = "Retrieve a paginated list of events created by the current organizer")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Events retrieved successfully",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema = @Schema(implementation = EventListResponse.class)))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - Organizer role required",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Page<EventListResponse>> getMyEvents(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching my events");
        Page<EventListResponse> response = eventService.getMyEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get event by ID", description = "Retrieve detailed information about a specific event")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Event retrieved successfully",
                        content = @Content(schema = @Schema(implementation = EventResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - Organizer role required or unauthorized access",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> getEventById(@PathVariable UUID eventId) {
        log.info("Fetching event: {}", eventId);
        EventResponse response = eventService.getEventById(eventId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update event", description = "Update an existing event with new information")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Event updated successfully",
                        content = @Content(schema = @Schema(implementation = EventResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Business rule violation or invalid event status",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - Organizer role required or unauthorized access",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable UUID eventId, @Valid @RequestBody UpdateEventRequest request) {
        log.info("Updating event: {}", eventId);
        EventResponse response = eventService.updateEvent(eventId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete event", description = "Delete an existing event permanently")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Business rule violation or invalid event status",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - Organizer role required or unauthorized access",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        log.info("Deleting event: {}", eventId);
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
