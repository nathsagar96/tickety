package com.tickety.mappers;

import com.tickety.dtos.requests.CreateTicketTypeRequest;
import com.tickety.dtos.requests.UpdateTicketTypeRequest;
import com.tickety.dtos.responses.TicketTypeResponse;
import com.tickety.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketTypeMapper {

    TicketType toTicketType(CreateTicketTypeRequest createTicketTypeRequest);

    TicketType toTicketType(UpdateTicketTypeRequest updateTicketTypeRequest);

    TicketTypeResponse toTicketTypeResponse(TicketType ticketType);
}
