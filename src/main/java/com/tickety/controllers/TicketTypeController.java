package com.tickety.controllers;

import com.tickety.dtos.requests.CreateTicketTypeRequest;
import com.tickety.dtos.requests.UpdateTicketTypeRequest;
import com.tickety.dtos.responses.TicketTypeListResponse;
import com.tickety.dtos.responses.TicketTypeResponse;
import com.tickety.services.TicketTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/events/{eventId}/ticket-types")
@RequiredArgsConstructor
@Tag(name = "Ticket Types", description = "Ticket type management APIs for events")
@SecurityRequirement(name = "bearerAuth")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @Operation(summary = "Create ticket type", description = "Create a new ticket type for a specific event")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Ticket type created successfully",
                        content = @Content(schema = @Schema(implementation = TicketTypeResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
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
    public ResponseEntity<TicketTypeResponse> createTicketType(
            @PathVariable UUID eventId, @Valid @RequestBody CreateTicketTypeRequest request) {
        log.info("Creating ticket type for event: {}", eventId);
        TicketTypeResponse response = ticketTypeService.createTicketType(eventId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get ticket types", description = "Retrieve all ticket types for a specific event")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ticket types retrieved successfully",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema =
                                                                @Schema(
                                                                        implementation =
                                                                                TicketTypeListResponse.class)))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<List<TicketTypeListResponse>> getTicketTypes(@PathVariable UUID eventId) {
        log.info("Fetching ticket types for event: {}", eventId);
        List<TicketTypeListResponse> response = ticketTypeService.getTicketTypesByEvent(eventId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get ticket type by ID",
            description = "Retrieve detailed information about a specific ticket type")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ticket type retrieved successfully",
                        content = @Content(schema = @Schema(implementation = TicketTypeResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event or ticket type not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/{ticketTypeId}")
    public ResponseEntity<TicketTypeResponse> getTicketTypeById(
            @PathVariable UUID eventId, @PathVariable UUID ticketTypeId) {
        log.info("Fetching ticket type: {} for event: {}", ticketTypeId, eventId);
        TicketTypeResponse response = ticketTypeService.getTicketTypeById(eventId, ticketTypeId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update ticket type", description = "Update an existing ticket type with new information")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ticket type updated successfully",
                        content = @Content(schema = @Schema(implementation = TicketTypeResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event or ticket type not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
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

    @Operation(summary = "Delete ticket type", description = "Delete an existing ticket type permanently")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Ticket type deleted successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event or ticket type not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
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
    @DeleteMapping("/{ticketTypeId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> deleteTicketType(@PathVariable UUID eventId, @PathVariable UUID ticketTypeId) {
        log.info("Deleting ticket type: {} for event: {}", ticketTypeId, eventId);
        ticketTypeService.deleteTicketType(eventId, ticketTypeId);
        return ResponseEntity.noContent().build();
    }
}
