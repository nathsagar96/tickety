package com.tickety.dtos.responses;

import com.tickety.enums.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventListResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the event")
                UUID id,
        @Schema(example = "Summer Music Festival", description = "Name of the event") String name,
        @Schema(example = "Central Park, New York", description = "Location of the event") String venue,
        @Schema(example = "2026-07-15T18:00:00", description = "Start date and time of the event")
                LocalDateTime startTime,
        @Schema(example = "PUBLISHED", description = "Current status of the event") EventStatus status,
        @Schema(example = "John Doe", description = "Name of the event organizer") String organizerName) {}
