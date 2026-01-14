package com.tickety.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateTicketTypeRequest(
        @Schema(example = "Updated VIP Pass", description = "Updated name of the ticket type")
                @Size(min = 3, max = 255, message = "Ticket type name must be between 3 and 255 characters")
                String name,
        @Schema(
                        example = "Updated access to VIP area with premium seating",
                        description = "Updated description of the ticket type")
                @Size(max = 1000, message = "Description cannot exceed 1000 characters")
                String description,
        @Schema(example = "249.99", description = "Updated price of the ticket")
                @DecimalMin(value = "0.0", message = "Price must be non-negative")
                @Digits(
                        integer = 8,
                        fraction = 2,
                        message = "Price must have at most 8 integer digits and 2 decimal places")
                BigDecimal price,
        @Schema(example = "600", description = "Updated total number of tickets available")
                @Min(value = 1, message = "At least 1 ticket must be available")
                @Max(value = 100000, message = "Cannot exceed 100,000 tickets")
                Integer totalAvailable) {}
