package com.tickety.mappers;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.requests.UpdateEventRequest;
import com.tickety.dtos.responses.EventListResponse;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.dtos.responses.PublishedEventResponse;
import com.tickety.entities.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "attendees", ignore = true)
    Event toEntity(CreateEventRequest request);

    @Mapping(source = "organizer.id", target = "organizerId")
    @Mapping(source = "organizer.username", target = "organizerName")
    EventResponse toResponse(Event event);

    @Mapping(source = "organizer.username", target = "organizerName")
    EventListResponse toListResponse(Event event);

    @Mapping(source = "organizer.username", target = "organizerName")
    PublishedEventResponse toPublishedResponse(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    @Mapping(target = "staff", ignore = true)
    @Mapping(target = "attendees", ignore = true)
    void updateEntityFromRequest(UpdateEventRequest request, @MappingTarget Event event);
}
