package com.tickety.dtos.requests;

import com.tickety.enums.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateEventRequest(
        @Schema(example = "Summer Music Festival", description = "Name of the event")
                @NotBlank(message = "Event name is required")
                @Size(min = 3, max = 255, message = "Event name must be between 3 and 255 characters")
                String name,
        @Schema(
                        example = "An annual music festival featuring top artists from around the world.",
                        description = "Detailed description of the event")
                @Size(max = 5000, message = "Description cannot exceed 5000 characters")
                String description,
        @Schema(example = "Central Park, New York", description = "Location where the event will take place")
                @NotBlank(message = "Venue is required")
                @Size(max = 500, message = "Venue cannot exceed 500 characters")
                String venue,
        @Schema(example = "2026-07-15T18:00:00", description = "Date and time when the event starts")
                @NotNull(message = "Start time is required")
                @Future(message = "Start time must be in the future")
                LocalDateTime startTime,
        @Schema(example = "2026-07-17T23:00:00", description = "Date and time when the event ends")
                @NotNull(message = "End time is required")
                LocalDateTime endTime,
        @Schema(example = "2026-01-01T00:00:00", description = "Date and time when ticket sales start")
                @NotNull(message = "Sales start time is required")
                LocalDateTime salesStart,
        @Schema(example = "2026-07-14T23:59:59", description = "Date and time when ticket sales end")
                @NotNull(message = "Sales end time is required")
                LocalDateTime salesEnd,
        @Schema(example = "PUBLISHED", description = "Current status of the event") EventStatus status) {}
