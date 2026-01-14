package com.tickety.exceptions;

public class TicketValidationException extends BusinessException {
    public TicketValidationException(String message) {
        super(message);
    }

    public static TicketValidationException alreadyUsed() {
        return new TicketValidationException("Ticket has already been used");
    }

    public static TicketValidationException cancelled() {
        return new TicketValidationException("Ticket has been cancelled");
    }

    public static TicketValidationException invalidEvent() {
        return new TicketValidationException("Ticket does not belong to this event");
    }
}
