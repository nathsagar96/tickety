package com.tickety.dtos.requests;

import com.tickety.enums.EventStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateEventRequest(
        @NotBlank(message = "Event name is required")
                @Size(min = 3, max = 255, message = "Event name must be between 3 and 255 characters")
                String name,
        @Size(max = 5000, message = "Description cannot exceed 5000 characters") String description,
        @NotBlank(message = "Venue is required") @Size(max = 500, message = "Venue cannot exceed 500 characters")
                String venue,
        @NotNull(message = "Start time is required") @Future(message = "Start time must be in the future")
                LocalDateTime startTime,
        @NotNull(message = "End time is required") LocalDateTime endTime,
        @NotNull(message = "Sales start time is required") LocalDateTime salesStart,
        @NotNull(message = "Sales end time is required") LocalDateTime salesEnd,
        EventStatus status) {}
