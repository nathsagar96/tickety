package com.tickety.dtos.responses;

import com.tickety.enums.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the event")
                UUID id,
        @Schema(example = "Summer Music Festival", description = "Name of the event") String name,
        @Schema(
                        example = "An annual music festival featuring top artists from around the world.",
                        description = "Detailed description of the event")
                String description,
        @Schema(example = "Central Park, New York", description = "Location where the event will take place")
                String venue,
        @Schema(example = "2026-07-15T18:00:00", description = "Date and time when the event starts")
                LocalDateTime startTime,
        @Schema(example = "2026-07-17T23:00:00", description = "Date and time when the event ends")
                LocalDateTime endTime,
        @Schema(example = "2026-01-01T00:00:00", description = "Date and time when ticket sales start")
                LocalDateTime salesStart,
        @Schema(example = "2026-07-14T23:59:59", description = "Date and time when ticket sales end")
                LocalDateTime salesEnd,
        @Schema(example = "PUBLISHED", description = "Current status of the event") EventStatus status,
        @Schema(
                        example = "7f3b1e2a-4f5c-6d7e-8f9a-0b1c2d3e4f5a",
                        description = "Unique identifier of the event organizer")
                UUID organizerId,
        @Schema(example = "John Doe", description = "Name of the event organizer") String organizerName,
        @Schema(example = "2026-01-10T14:30:00", description = "Date and time when the event was created")
                LocalDateTime createdAt,
        @Schema(example = "2026-01-12T09:15:00", description = "Date and time when the event was last updated")
                LocalDateTime updatedAt) {}
