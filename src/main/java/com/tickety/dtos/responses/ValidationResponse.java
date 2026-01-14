package com.tickety.dtos.responses;

import com.tickety.enums.TicketValidationStatus;
import com.tickety.enums.ValidationMethod;
import java.time.LocalDateTime;
import java.util.UUID;

public record ValidationResponse(
        UUID id,
        TicketValidationStatus status,
        ValidationMethod validationMethod,
        LocalDateTime validatedAt,
        String validatedBy,
        String notes,
        String message,
        TicketInfo ticket) {
    public record TicketInfo(UUID id, String ticketTypeName, String eventName, String purchaserName) {}
}
