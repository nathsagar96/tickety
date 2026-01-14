package com.tickety.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException user(String identifier) {
        return new ResourceNotFoundException("User not found: " + identifier);
    }

    public static ResourceNotFoundException event(String id) {
        return new ResourceNotFoundException("Event not found: " + id);
    }

    public static ResourceNotFoundException ticketType(String id) {
        return new ResourceNotFoundException("Ticket type not found: " + id);
    }

    public static ResourceNotFoundException ticket(String id) {
        return new ResourceNotFoundException("Ticket not found: " + id);
    }

    public static ResourceNotFoundException qrCode(String value) {
        return new ResourceNotFoundException("QR code not found: " + value);
    }
}
