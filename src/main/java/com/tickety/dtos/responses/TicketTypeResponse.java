package com.tickety.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketTypeResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer totalAvailable,
        Integer availableCount,
        UUID eventId,
        String eventName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
