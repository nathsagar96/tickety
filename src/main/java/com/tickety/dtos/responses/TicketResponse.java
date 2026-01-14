package com.tickety.dtos.responses;

import com.tickety.enums.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the ticket")
                UUID id,
        @Schema(example = "CONFIRMED", description = "Current status of the ticket") TicketStatus status,
        @Schema(example = "199.99", description = "Price at which the ticket was purchased") BigDecimal purchasePrice,
        @Schema(example = "2026-01-15T14:30:00", description = "Date and time when the ticket was purchased")
                LocalDateTime purchasedAt,
        @Schema(description = "Information about the ticket type") TicketTypeInfo ticketType,
        @Schema(description = "Information about the event") EventInfo event,
        @Schema(description = "QR code information for the ticket") QrCodeInfo qrCode) {
    @Schema(description = "Information about the ticket type")
    public record TicketTypeInfo(
            @Schema(
                            example = "550e8400-e29b-41d4-a716-446655440000",
                            description = "Unique identifier of the ticket type")
                    UUID id,
            @Schema(example = "VIP Pass", description = "Name of the ticket type") String name,
            @Schema(
                            example = "Access to VIP area with premium seating and amenities",
                            description = "Description of the ticket type")
                    String description) {}

    @Schema(description = "Information about the event")
    public record EventInfo(
            @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the event")
                    UUID id,
            @Schema(example = "Summer Music Festival", description = "Name of the event") String name,
            @Schema(example = "Central Park, New York", description = "Location of the event") String venue,
            @Schema(example = "2026-07-15T18:00:00", description = "Start date and time of the event")
                    LocalDateTime startTime,
            @Schema(example = "2026-07-17T23:00:00", description = "End date and time of the event")
                    LocalDateTime endTime) {}

    @Schema(description = "QR code information for the ticket")
    public record QrCodeInfo(
            @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the QR code")
                    UUID id,
            @Schema(example = "QR123456789", description = "QR code value") String value) {}
}
