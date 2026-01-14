package com.tickety.mappers;

import com.tickety.dtos.responses.TicketListResponse;
import com.tickety.dtos.responses.TicketResponse;
import com.tickety.entities.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    @Mapping(source = "ticketType.id", target = "ticketType.id")
    @Mapping(source = "ticketType.name", target = "ticketType.name")
    @Mapping(source = "ticketType.description", target = "ticketType.description")
    @Mapping(source = "ticketType.event.id", target = "event.id")
    @Mapping(source = "ticketType.event.name", target = "event.name")
    @Mapping(source = "ticketType.event.venue", target = "event.venue")
    @Mapping(source = "ticketType.event.startTime", target = "event.startTime")
    @Mapping(source = "ticketType.event.endTime", target = "event.endTime")
    @Mapping(target = "qrCode", expression = "java(mapQrCode(ticket))")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(source = "ticketType.event.name", target = "eventName")
    @Mapping(source = "ticketType.name", target = "ticketTypeName")
    @Mapping(source = "ticketType.event.startTime", target = "eventStartTime")
    TicketListResponse toListResponse(Ticket ticket);

    default TicketResponse.QrCodeInfo mapQrCode(Ticket ticket) {
        if (ticket.getQrCodes() == null || ticket.getQrCodes().isEmpty()) {
            return null;
        }
        var qrCode = ticket.getQrCodes().iterator().next();
        return new TicketResponse.QrCodeInfo(qrCode.getId(), qrCode.getValue());
    }
}
