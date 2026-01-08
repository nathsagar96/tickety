package com.tickety.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateTicketTypeRequest(
        @NotBlank(message = "Ticket type name is required")
                @Size(min = 2, max = 50, message = "Ticket type name must be between 2 and 50 characters")
                String name,
        @NotNull(message = "Price is required") @PositiveOrZero(message = "Price must be zero or greater") Double price,
        @Size(max = 500, message = "Description must not exceed 500 characters") String description,
        @NotNull(message = "Total available tickets must be specified")
                @PositiveOrZero(message = "Total available tickets must be zero or greater")
                Integer totalAvailable) {}
