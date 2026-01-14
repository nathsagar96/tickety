package com.tickety.dtos.responses;

import java.util.UUID;

public record TicketPurchaseResponse(UUID ticketId, String message, QrCodeResponse qrCode) {}
