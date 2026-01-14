package com.tickety.exceptions;

public class SalesWindowException extends BusinessException {
    public SalesWindowException(String message) {
        super(message);
    }

    public static SalesWindowException notStarted() {
        return new SalesWindowException("Sales have not started yet");
    }

    public static SalesWindowException ended() {
        return new SalesWindowException("Sales have ended");
    }
}
