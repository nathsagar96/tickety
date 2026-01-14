package com.tickety.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeListResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the ticket type")
                UUID id,
        @Schema(example = "VIP Pass", description = "Name of the ticket type") String name,
        @Schema(example = "199.99", description = "Price of the ticket") BigDecimal price,
        @Schema(example = "450", description = "Number of tickets currently available for purchase")
                Integer availableCount,
        @Schema(example = "500", description = "Total number of tickets available for this type")
                Integer totalAvailable) {}
