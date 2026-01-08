package com.tickety.dtos.requests;

import com.tickety.enums.EventStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

public record CreateEventRequest(
        @NotBlank(message = "Event name is required")
                @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
                String name,
        @NotNull(message = "Event start date is required") @Future(message = "Event start date must be in the future")
                Instant start,
        @NotNull(message = "Event end date is required") @Future(message = "Event end date must be in the future")
                Instant end,
        @NotBlank(message = "Venue information is required")
                @Size(min = 5, max = 200, message = "Venue information must be between 5 and 200 characters")
                String venue,
        @Future(message = "Sales start date must be in the future") Instant salesStart,
        @Future(message = "Sales end date must be in the future") Instant salesEnd,
        @NotNull(message = "Event status must be provided") EventStatus status,
        @NotEmpty(message = "At least one ticket type is required")
                @Size(min = 1, max = 10, message = "Event must have between 1 and 10 ticket types")
                @Valid
                List<CreateTicketTypeRequest> ticketTypes) {}
