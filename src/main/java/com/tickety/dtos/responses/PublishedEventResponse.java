package com.tickety.dtos.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record PublishedEventResponse(
        UUID id,
        String name,
        String description,
        String venue,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime salesStart,
        LocalDateTime salesEnd,
        String organizerName) {}
