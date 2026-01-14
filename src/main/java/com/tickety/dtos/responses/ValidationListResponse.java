package com.tickety.dtos.responses;

import com.tickety.enums.TicketValidationStatus;
import com.tickety.enums.ValidationMethod;
import java.time.LocalDateTime;
import java.util.UUID;

public record ValidationListResponse(
        UUID id,
        TicketValidationStatus status,
        ValidationMethod validationMethod,
        LocalDateTime validatedAt,
        String ticketTypeName,
        String purchaserName) {}
