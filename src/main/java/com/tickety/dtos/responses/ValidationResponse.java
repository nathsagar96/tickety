package com.tickety.dtos.responses;

import com.tickety.enums.TicketValidationStatus;
import com.tickety.enums.ValidationMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record ValidationResponse(
        @Schema(
                        example = "550e8400-e29b-41d4-a716-446655440000",
                        description = "Unique identifier of the validation record")
                UUID id,
        @Schema(example = "VALID", description = "Status of the ticket validation") TicketValidationStatus status,
        @Schema(example = "QR_CODE", description = "Method used for validation") ValidationMethod validationMethod,
        @Schema(example = "2026-07-15T17:45:00", description = "Date and time when the validation occurred")
                LocalDateTime validatedAt,
        @Schema(example = "John Doe", description = "Name of the validator") String validatedBy,
        @Schema(example = "Ticket validated at main entrance", description = "Additional notes about the validation")
                String notes,
        @Schema(example = "Ticket validation successful", description = "Validation result message") String message,
        @Schema(description = "Information about the validated ticket") TicketInfo ticket) {
    @Schema(description = "Information about the validated ticket")
    public record TicketInfo(
            @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Unique identifier of the ticket")
                    UUID id,
            @Schema(example = "VIP Pass", description = "Name of the ticket type") String ticketTypeName,
            @Schema(example = "Summer Music Festival", description = "Name of the associated event") String eventName,
            @Schema(example = "John Smith", description = "Name of the ticket purchaser") String purchaserName) {}
}
