package com.tickety.exceptions;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public static UnauthorizedAccessException event() {
        return new UnauthorizedAccessException("You don't have access to this event");
    }

    public static UnauthorizedAccessException ticket() {
        return new UnauthorizedAccessException("You don't have access to this ticket");
    }
}
