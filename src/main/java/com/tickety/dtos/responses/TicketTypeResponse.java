package com.tickety.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketTypeResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the ticket type")
                UUID id,
        @Schema(example = "VIP Pass", description = "Name of the ticket type") String name,
        @Schema(
                        example = "Access to VIP area with premium seating and amenities",
                        description = "Description of the ticket type")
                String description,
        @Schema(example = "199.99", description = "Price of the ticket") BigDecimal price,
        @Schema(example = "500", description = "Total number of tickets available for this type")
                Integer totalAvailable,
        @Schema(example = "450", description = "Number of tickets currently available for purchase")
                Integer availableCount,
        @Schema(
                        example = "7f3b1e2a-4f5c-6d7e-8f9a-0b1c2d3e4f5a",
                        description = "Unique identifier of the associated event")
                UUID eventId,
        @Schema(example = "Summer Music Festival", description = "Name of the associated event") String eventName,
        @Schema(example = "2026-01-10T14:30:00", description = "Date and time when the ticket type was created")
                LocalDateTime createdAt,
        @Schema(example = "2026-01-12T09:15:00", description = "Date and time when the ticket type was last updated")
                LocalDateTime updatedAt) {}
