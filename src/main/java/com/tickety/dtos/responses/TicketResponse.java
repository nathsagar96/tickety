package com.tickety.dtos.responses;

import com.tickety.enums.TicketStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        TicketStatus status,
        BigDecimal purchasePrice,
        LocalDateTime purchasedAt,
        TicketTypeInfo ticketType,
        EventInfo event,
        QrCodeInfo qrCode) {
    public record TicketTypeInfo(UUID id, String name, String description) {}

    public record EventInfo(UUID id, String name, String venue, LocalDateTime startTime, LocalDateTime endTime) {}

    public record QrCodeInfo(UUID id, String value) {}
}
