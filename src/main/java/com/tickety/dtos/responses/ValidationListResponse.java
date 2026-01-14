package com.tickety.dtos.responses;

import com.tickety.enums.TicketValidationStatus;
import com.tickety.enums.ValidationMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record ValidationListResponse(
        @Schema(
                        example = "550e8400-e29b-41d4-a716-446655440000",
                        description = "Unique identifier of the validation record")
                UUID id,
        @Schema(example = "VALID", description = "Status of the ticket validation") TicketValidationStatus status,
        @Schema(example = "QR_CODE", description = "Method used for validation") ValidationMethod validationMethod,
        @Schema(example = "2026-07-15T17:45:00", description = "Date and time when the validation occurred")
                LocalDateTime validatedAt,
        @Schema(example = "VIP Pass", description = "Name of the ticket type") String ticketTypeName,
        @Schema(example = "John Smith", description = "Name of the ticket purchaser") String purchaserName) {}
