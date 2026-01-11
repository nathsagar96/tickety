package com.tickety.dtos.requests;

import com.tickety.enums.EventStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UpdateEventRequest(
        UUID id,
        String name,
        Instant start,
        Instant end,
        String venue,
        Instant salesStart,
        Instant salesEnd,
        EventStatus status,
        List<UpdateTicketTypeRequest> ticketTypes) {}
