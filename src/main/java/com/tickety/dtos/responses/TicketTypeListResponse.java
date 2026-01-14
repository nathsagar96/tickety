package com.tickety.dtos.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeListResponse(
        UUID id, String name, BigDecimal price, Integer availableCount, Integer totalAvailable) {}
