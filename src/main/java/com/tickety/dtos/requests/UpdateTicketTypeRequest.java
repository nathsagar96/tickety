package com.tickety.dtos.requests;

import java.util.UUID;

public record UpdateTicketTypeRequest(UUID id, String name, Double price, String description, Integer totalAvailable) {}
