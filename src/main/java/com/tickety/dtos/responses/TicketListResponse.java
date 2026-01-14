package com.tickety.dtos.responses;

import com.tickety.enums.TicketStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketListResponse(
        UUID id,
        TicketStatus status,
        String eventName,
        String ticketTypeName,
        LocalDateTime purchasedAt,
        LocalDateTime eventStartTime) {}
