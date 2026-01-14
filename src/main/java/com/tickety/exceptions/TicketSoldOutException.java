package com.tickety.exceptions;

public class TicketSoldOutException extends BusinessException {
    public TicketSoldOutException(String ticketTypeName) {
        super("Tickets sold out for: " + ticketTypeName);
    }
}
