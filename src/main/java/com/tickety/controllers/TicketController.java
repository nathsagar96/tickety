package com.tickety.controllers;

import com.tickety.dtos.responses.QrCodeResponse;
import com.tickety.dtos.responses.TicketListResponse;
import com.tickety.dtos.responses.TicketPurchaseResponse;
import com.tickety.dtos.responses.TicketResponse;
import com.tickety.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Ticket management APIs for users")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Purchase ticket", description = "Purchase a ticket for a specific event and ticket type")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Ticket purchased successfully",
                        content = @Content(schema = @Schema(implementation = TicketPurchaseResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Event or ticket type not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Business rule violation, sales window error, or ticket sold out",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized - authentication required",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "409",
                        description = "Ticket sold out",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping("/published-events/{eventId}/ticket-types/{ticketTypeId}/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketPurchaseResponse> purchaseTicket(
            @PathVariable UUID eventId, @PathVariable UUID ticketTypeId) {
        log.info("Purchasing ticket for event: {} and ticket type: {}", eventId, ticketTypeId);
        TicketPurchaseResponse response = ticketService.purchaseTicket(eventId, ticketTypeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get my tickets",
            description = "Retrieve a paginated list of tickets purchased by the current user")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Tickets retrieved successfully",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema = @Schema(implementation = TicketListResponse.class)))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized - authentication required",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/tickets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TicketListResponse>> getMyTickets(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching my tickets");
        Page<TicketListResponse> response = ticketService.getMyTickets(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get ticket by ID", description = "Retrieve detailed information about a specific ticket")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ticket retrieved successfully",
                        content = @Content(schema = @Schema(implementation = TicketResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Ticket not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized - authentication required",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - unauthorized access to ticket",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/tickets/{ticketId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable UUID ticketId) {
        log.info("Fetching ticket: {}", ticketId);
        TicketResponse response = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get ticket QR code", description = "Retrieve QR code for a specific ticket")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "QR code retrieved successfully",
                        content = @Content(schema = @Schema(implementation = QrCodeResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Ticket not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized - authentication required",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - unauthorized access to ticket",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "500",
                        description = "QR code generation error",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/tickets/{ticketId}/qr-code")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QrCodeResponse> getTicketQrCode(@PathVariable UUID ticketId) {
        log.info("Fetching QR code for ticket: {}", ticketId);
        QrCodeResponse response = ticketService.getTicketQrCode(ticketId);
        return ResponseEntity.ok(response);
    }
}
