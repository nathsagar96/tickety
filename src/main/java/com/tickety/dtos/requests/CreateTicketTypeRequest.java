package com.tickety.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateTicketTypeRequest(
        @Schema(example = "VIP Pass", description = "Name of the ticket type")
                @NotBlank(message = "Ticket type name is required")
                @Size(min = 3, max = 255, message = "Ticket type name must be between 3 and 255 characters")
                String name,
        @Schema(
                        example = "Access to VIP area with premium seating and amenities",
                        description = "Description of the ticket type")
                @Size(max = 1000, message = "Description cannot exceed 1000 characters")
                String description,
        @Schema(example = "199.99", description = "Price of the ticket")
                @NotNull(message = "Price is required")
                @DecimalMin(value = "0.0", message = "Price must be non-negative")
                @Digits(
                        integer = 8,
                        fraction = 2,
                        message = "Price must have at most 8 integer digits and 2 decimal places")
                BigDecimal price,
        @Schema(example = "500", description = "Total number of tickets available for this type")
                @NotNull(message = "Total available tickets is required")
                @Min(value = 1, message = "At least 1 ticket must be available")
                @Max(value = 100000, message = "Cannot exceed 100,000 tickets")
                Integer totalAvailable) {}
