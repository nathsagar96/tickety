package com.tickety.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record PublishedEventResponse(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the event")
                UUID id,
        @Schema(example = "Summer Music Festival", description = "Name of the event") String name,
        @Schema(
                        example = "An annual music festival featuring top artists from around the world.",
                        description = "Description of the event")
                String description,
        @Schema(example = "Central Park, New York", description = "Location of the event") String venue,
        @Schema(example = "2026-07-15T18:00:00", description = "Start date and time of the event")
                LocalDateTime startTime,
        @Schema(example = "2026-07-17T23:00:00", description = "End date and time of the event") LocalDateTime endTime,
        @Schema(example = "2026-01-01T00:00:00", description = "Ticket sales start date and time")
                LocalDateTime salesStart,
        @Schema(example = "2026-07-14T23:59:59", description = "Ticket sales end date and time") LocalDateTime salesEnd,
        @Schema(example = "John Doe", description = "Name of the event organizer") String organizerName) {}
