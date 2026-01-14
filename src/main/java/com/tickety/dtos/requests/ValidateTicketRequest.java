package com.tickety.dtos.requests;

import com.tickety.enums.ValidationMethod;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ValidateTicketRequest(
        @NotNull(message = "Ticket ID is required") UUID ticketId,
        String qrCodeValue,
        @NotNull(message = "Validation method is required") ValidationMethod validationMethod,
        String notes) {}
