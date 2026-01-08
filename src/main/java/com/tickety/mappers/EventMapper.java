package com.tickety.mappers;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.requests.CreateTicketTypeRequest;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.dtos.responses.TicketTypeResponse;
import com.tickety.entities.Event;
import com.tickety.entities.TicketType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    TicketType toTicketType(CreateTicketTypeRequest createTicketTypeRequest);

    Event toEvent(CreateEventRequest createEventRequest);

    EventResponse toEventResponse(Event event);

    List<EventResponse> toEventResponseList(List<Event> events);

    TicketTypeResponse toTicketTypeResponse(TicketType ticketType);
}
