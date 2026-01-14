package com.tickety.mappers;

import com.tickety.dtos.responses.ValidationListResponse;
import com.tickety.dtos.responses.ValidationResponse;
import com.tickety.entities.TicketValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ValidationMapper {

    @Mapping(source = "ticket.id", target = "ticket.id")
    @Mapping(source = "ticket.ticketType.name", target = "ticket.ticketTypeName")
    @Mapping(source = "ticket.ticketType.event.name", target = "ticket.eventName")
    @Mapping(source = "ticket.purchaser.username", target = "ticket.purchaserName")
    @Mapping(target = "message", ignore = true)
    ValidationResponse toResponse(TicketValidation validation);

    @Mapping(source = "ticket.ticketType.name", target = "ticketTypeName")
    @Mapping(source = "ticket.purchaser.username", target = "purchaserName")
    ValidationListResponse toListResponse(TicketValidation validation);
}
