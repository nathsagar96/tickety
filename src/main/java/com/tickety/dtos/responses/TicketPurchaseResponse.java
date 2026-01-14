package com.tickety.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record TicketPurchaseResponse(
        @Schema(
                        example = "550e8400-e29b-41d4-a716-446655440000",
                        description = "Unique identifier of the purchased ticket")
                UUID ticketId,
        @Schema(example = "Ticket purchased successfully", description = "Purchase confirmation message")
                String message,
        @Schema(description = "QR code information for the ticket") QrCodeResponse qrCode) {}
