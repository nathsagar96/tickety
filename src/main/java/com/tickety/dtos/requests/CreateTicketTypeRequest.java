package com.tickety.dtos.requests;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateTicketTypeRequest(
        @NotBlank(message = "Ticket type name is required")
                @Size(min = 3, max = 255, message = "Ticket type name must be between 3 and 255 characters")
                String name,
        @Size(max = 1000, message = "Description cannot exceed 1000 characters") String description,
        @NotNull(message = "Price is required")
                @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
                @Digits(
                        integer = 8,
                        fraction = 2,
                        message = "Price must have at most 8 integer digits and 2 decimal places")
                BigDecimal price,
        @NotNull(message = "Total available tickets is required")
                @Min(value = 1, message = "At least 1 ticket must be available")
                @Max(value = 100000, message = "Cannot exceed 100,000 tickets")
                Integer totalAvailable) {}
