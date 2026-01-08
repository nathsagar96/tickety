package com.tickety.dtos.responses;

import java.time.Instant;
import java.util.UUID;

public record TicketTypeResponse(
        UUID id,
        String name,
        String description,
        Double price,
        Integer totalAvailable,
        Instant createdAt,
        Instant updatedAt) {}
