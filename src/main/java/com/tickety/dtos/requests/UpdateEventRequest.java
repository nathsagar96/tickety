package com.tickety.dtos.requests;

import com.tickety.enums.EventStatus;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record UpdateEventRequest(
        @Size(min = 3, max = 255, message = "Event name must be between 3 and 255 characters") String name,
        @Size(max = 5000, message = "Description cannot exceed 5000 characters") String description,
        @Size(max = 500, message = "Venue cannot exceed 500 characters") String venue,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime salesStart,
        LocalDateTime salesEnd,
        EventStatus status) {}
