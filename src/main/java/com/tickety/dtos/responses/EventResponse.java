package com.tickety.dtos.responses;

import com.tickety.enums.EventStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String name,
        Instant start,
        Instant end,
        String venue,
        Instant salesStart,
        Instant salesEnd,
        EventStatus status,
        List<TicketTypeResponse> ticketTypes,
        Instant createdAt,
        Instant updatedAt) {}
