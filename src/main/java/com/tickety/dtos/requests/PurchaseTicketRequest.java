package com.tickety.dtos.requests;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PurchaseTicketRequest(@NotNull(message = "Ticket type ID is required") UUID ticketTypeId) {}
