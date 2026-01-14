package com.tickety.services;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.requests.UpdateEventRequest;
import com.tickety.dtos.responses.EventListResponse;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.dtos.responses.PublishedEventResponse;
import com.tickety.entities.Event;
import com.tickety.entities.User;
import com.tickety.enums.EventStatus;
import com.tickety.exceptions.ResourceNotFoundException;
import com.tickety.exceptions.UnauthorizedAccessException;
import com.tickety.mappers.EventMapper;
import com.tickety.repositories.EventRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        User organizer = userService.getCurrentUser();

        Event event = eventMapper.toEntity(request);
        event.setOrganizer(organizer);

        if (event.getStatus() == null) {
            event.setStatus(EventStatus.DRAFT);
        }

        Event savedEvent = eventRepository.save(event);
        log.info("Created event {} by organizer {}", savedEvent.getId(), organizer.getId());

        return eventMapper.toResponse(savedEvent);
    }

    @Transactional(readOnly = true)
    public Page<EventListResponse> getMyEvents(Pageable pageable) {
        User organizer = userService.getCurrentUser();
        Page<Event> events = eventRepository.findByOrganizer(organizer, pageable);
        return events.map(eventMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID eventId) {
        Event event = findEventByIdAndVerifyAccess(eventId);
        return eventMapper.toResponse(event);
    }

    @Transactional
    public EventResponse updateEvent(UUID eventId, UpdateEventRequest request) {
        Event event = findEventByIdAndVerifyAccess(eventId);

        eventMapper.updateEntityFromRequest(request, event);
        Event updatedEvent = eventRepository.save(event);

        log.info("Updated event {}", eventId);
        return eventMapper.toResponse(updatedEvent);
    }

    @Transactional
    public void deleteEvent(UUID eventId) {
        Event event = findEventByIdAndVerifyAccess(eventId);

        eventRepository.delete(event);
        log.info("Deleted event {}", eventId);
    }

    @Transactional(readOnly = true)
    public Page<PublishedEventResponse> getPublishedEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findPublishedEvents(EventStatus.PUBLISHED, pageable);
        return events.map(eventMapper::toPublishedResponse);
    }

    @Transactional(readOnly = true)
    public PublishedEventResponse getPublishedEventById(UUID eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> ResourceNotFoundException.event(eventId.toString()));

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw ResourceNotFoundException.event(eventId.toString());
        }

        return eventMapper.toPublishedResponse(event);
    }

    @Transactional(readOnly = true)
    public Event findById(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> ResourceNotFoundException.event(eventId.toString()));
    }

    private Event findEventByIdAndVerifyAccess(UUID eventId) {
        User currentUser = userService.getCurrentUser();
        return eventRepository
                .findByIdAndOrganizer(eventId, currentUser)
                .orElseThrow(UnauthorizedAccessException::event);
    }
}
