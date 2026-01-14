package com.tickety.dtos.requests;

import com.tickety.enums.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record UpdateEventRequest(
        @Schema(example = "Updated Summer Music Festival", description = "Updated name of the event")
                @Size(min = 3, max = 255, message = "Event name must be between 3 and 255 characters")
                String name,
        @Schema(
                        example = "Updated description with more details about the festival.",
                        description = "Updated description of the event")
                @Size(max = 5000, message = "Description cannot exceed 5000 characters")
                String description,
        @Schema(example = "Updated Central Park, New York", description = "Updated location of the event")
                @Size(max = 500, message = "Venue cannot exceed 500 characters")
                String venue,
        @Schema(example = "2026-07-16T18:00:00", description = "Updated start date and time") LocalDateTime startTime,
        @Schema(example = "2026-07-18T23:00:00", description = "Updated end date and time") LocalDateTime endTime,
        @Schema(example = "2026-01-02T00:00:00", description = "Updated ticket sales start date and time")
                LocalDateTime salesStart,
        @Schema(example = "2026-07-15T23:59:59", description = "Updated ticket sales end date and time")
                LocalDateTime salesEnd,
        @Schema(example = "UPDATED", description = "Updated status of the event") EventStatus status) {}
