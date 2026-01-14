package com.tickety.dtos.requests;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateTicketTypeRequest(
        @Size(min = 3, max = 255, message = "Ticket type name must be between 3 and 255 characters") String name,
        @Size(max = 1000, message = "Description cannot exceed 1000 characters") String description,
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
                @Digits(
                        integer = 8,
                        fraction = 2,
                        message = "Price must have at most 8 integer digits and 2 decimal places")
                BigDecimal price,
        @Min(value = 1, message = "At least 1 ticket must be available")
                @Max(value = 100000, message = "Cannot exceed 100,000 tickets")
                Integer totalAvailable) {}
