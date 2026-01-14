package com.tickety.dtos.responses;

import com.tickety.enums.EventStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventListResponse(
        UUID id, String name, String venue, LocalDateTime startTime, EventStatus status, String organizerName) {}
