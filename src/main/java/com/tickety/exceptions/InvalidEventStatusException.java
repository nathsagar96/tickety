package com.tickety.exceptions;

public class InvalidEventStatusException extends BusinessException {
    public InvalidEventStatusException(String message) {
        super(message);
    }

    public static InvalidEventStatusException notPublished() {
        return new InvalidEventStatusException("Event is not published");
    }
}
