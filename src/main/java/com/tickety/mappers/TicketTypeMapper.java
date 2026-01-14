package com.tickety.mappers;

import com.tickety.dtos.requests.CreateTicketTypeRequest;
import com.tickety.dtos.requests.UpdateTicketTypeRequest;
import com.tickety.dtos.responses.TicketTypeListResponse;
import com.tickety.dtos.responses.TicketTypeResponse;
import com.tickety.entities.TicketType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketTypeMapper {

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "availableCount", source = "totalAvailable")
    TicketType toEntity(CreateTicketTypeRequest request);

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    TicketTypeResponse toResponse(TicketType ticketType);

    TicketTypeListResponse toListResponse(TicketType ticketType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "availableCount", ignore = true)
    void updateEntityFromRequest(UpdateTicketTypeRequest request, @MappingTarget TicketType ticketType);
}
