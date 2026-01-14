package com.tickety.controllers;

import com.tickety.dtos.responses.QrCodeResponse;
import com.tickety.dtos.responses.TicketListResponse;
import com.tickety.dtos.responses.TicketPurchaseResponse;
import com.tickety.dtos.responses.TicketResponse;
import com.tickety.services.TicketService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/published-events/{eventId}/ticket-types/{ticketTypeId}/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketPurchaseResponse> purchaseTicket(
            @PathVariable UUID eventId, @PathVariable UUID ticketTypeId) {
        log.info("Purchasing ticket for event: {} and ticket type: {}", eventId, ticketTypeId);
        TicketPurchaseResponse response = ticketService.purchaseTicket(eventId, ticketTypeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/tickets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TicketListResponse>> getMyTickets(@PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching my tickets");
        Page<TicketListResponse> response = ticketService.getMyTickets(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tickets/{ticketId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable UUID ticketId) {
        log.info("Fetching ticket: {}", ticketId);
        TicketResponse response = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tickets/{ticketId}/qr-code")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<QrCodeResponse> getTicketQrCode(@PathVariable UUID ticketId) {
        log.info("Fetching QR code for ticket: {}", ticketId);
        QrCodeResponse response = ticketService.getTicketQrCode(ticketId);
        return ResponseEntity.ok(response);
    }
}
