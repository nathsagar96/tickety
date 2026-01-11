package com.tickety.mappers;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.entities.Event;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    Event toEvent(CreateEventRequest createEventRequest);

    EventResponse toEventResponse(Event event);

    List<EventResponse> toEventResponseList(List<Event> events);
}
