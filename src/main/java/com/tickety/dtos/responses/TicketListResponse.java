package com.tickety.dtos.responses;

import com.tickety.enums.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketListResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the ticket")
                UUID id,
        @Schema(example = "CONFIRMED", description = "Current status of the ticket") TicketStatus status,
        @Schema(example = "Summer Music Festival", description = "Name of the associated event") String eventName,
        @Schema(example = "VIP Pass", description = "Name of the ticket type") String ticketTypeName,
        @Schema(example = "2026-01-15T14:30:00", description = "Date and time when the ticket was purchased")
                LocalDateTime purchasedAt,
        @Schema(example = "2026-07-15T18:00:00", description = "Start date and time of the event")
                LocalDateTime eventStartTime) {}
