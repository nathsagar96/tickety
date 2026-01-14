package com.tickety.dtos.requests;

import com.tickety.enums.ValidationMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ValidateTicketRequest(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "ID of the ticket to validate")
                @NotNull(message = "Ticket ID is required")
                UUID ticketId,
        @Schema(example = "QR123456789", description = "QR code value for validation") String qrCodeValue,
        @Schema(example = "QR_CODE", description = "Method used for validation")
                @NotNull(message = "Validation method is required")
                ValidationMethod validationMethod,
        @Schema(example = "Ticket validated at main entrance", description = "Additional notes about the validation")
                String notes) {}
