package com.tickety.services;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.requests.CreateTicketTypeRequest;
import com.tickety.dtos.requests.UpdateEventRequest;
import com.tickety.dtos.requests.UpdateTicketTypeRequest;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.dtos.responses.PageResponse;
import com.tickety.entities.Event;
import com.tickety.entities.TicketType;
import com.tickety.entities.User;
import com.tickety.exceptions.EntityNotFoundException;
import com.tickety.exceptions.ValidationException;
import com.tickety.mappers.EventMapper;
import com.tickety.mappers.TicketTypeMapper;
import com.tickety.repositories.EventRepository;
import com.tickety.repositories.UserRepository;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final TicketTypeMapper ticketTypeMapper;

    @Transactional
    public EventResponse createEvent(UUID organizerId, CreateEventRequest request) {
        User organizer = userRepository
                .findById(organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Organizer not found"));

        validateEventDates(request);

        Event event = eventMapper.toEvent(request);
        event.setOrganizer(organizer);

        createTicketTypes(event, request.ticketTypes());
        Event savedEvent = eventRepository.save(event);

        return eventMapper.toEventResponse(savedEvent);
    }

    private void validateEventDates(CreateEventRequest request) {
        if (request.start().isAfter(request.end())) {
            throw new ValidationException("Event start date must be before end date");
        }

        if (request.salesStart() != null && request.salesEnd() != null) {
            if (request.salesStart().isAfter(request.salesEnd())) {
                throw new ValidationException("Sales start date must be before sales end date");
            }

            if (request.salesStart().isAfter(request.start())
                    || request.salesEnd().isBefore(request.end())) {
                throw new ValidationException("Sales period must be within event period");
            }
        } else if (request.salesStart() != null || request.salesEnd() != null) {
            throw new ValidationException("Both sales start and sales end dates must be provided together");
        }
    }

    private void createTicketTypes(Event event, List<CreateTicketTypeRequest> ticketTypeRequests) {
        List<TicketType> ticketTypes = ticketTypeRequests.stream()
                .map(request -> {
                    TicketType ticketType = ticketTypeMapper.toTicketType(request);
                    ticketType.setEvent(event);
                    return ticketType;
                })
                .toList();

        event.setTicketTypes(ticketTypes);
    }

    public PageResponse<EventResponse> getAllEventsForOrganizer(UUID organizerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = eventRepository.findByOrganizerId(organizerId, pageable);

        List<EventResponse> eventResponses = eventMapper.toEventResponseList(eventsPage.getContent());

        return new PageResponse<>(
                eventResponses,
                eventsPage.getNumber(),
                eventsPage.getSize(),
                eventsPage.getTotalElements(),
                eventsPage.getTotalPages(),
                eventsPage.isFirst(),
                eventsPage.isLast());
    }

    public EventResponse getEventForOrganizer(UUID eventId, UUID organizerId) {
        Optional<Event> eventOptional = eventRepository.findByIdAndOrganizerId(eventId, organizerId);

        Event event = eventOptional.orElseThrow(() -> new EntityNotFoundException("Event not found for organizer"));

        return eventMapper.toEventResponse(event);
    }

    @Transactional
    public EventResponse updateEventForOrganizer(UUID eventId, UUID organizerId, UpdateEventRequest request) {
        if (request.id() == null) {
            throw new ValidationException("Request must contain an ID");
        }

        if (!request.id().equals(eventId)) {
            throw new ValidationException("Event ID in request must match the URL parameter");
        }

        validateEventDates(request);

        Event existingEvent = eventRepository
                .findByIdAndOrganizerId(eventId, organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found for organizer"));

        if (request.name() != null) {
            existingEvent.setName(request.name());
        }
        if (request.start() != null) {
            existingEvent.setStart(request.start());
        }
        if (request.end() != null) {
            existingEvent.setEnd(request.end());
        }
        if (request.venue() != null) {
            existingEvent.setVenue(request.venue());
        }
        if (request.salesStart() != null) {
            existingEvent.setSalesStart(request.salesStart());
        }
        if (request.salesEnd() != null) {
            existingEvent.setSalesEnd(request.salesEnd());
        }
        if (request.status() != null) {
            existingEvent.setStatus(request.status());
        }

        if (request.ticketTypes() != null) {
            Map<UUID, TicketType> existingTicketTypes = existingEvent.getTicketTypes().stream()
                    .collect(Collectors.toMap(TicketType::getId, ticketType -> ticketType));

            List<TicketType> updatedTicketTypes = request.ticketTypes().stream()
                    .map(ticketTypeRequest -> {
                        TicketType ticketType;
                        if (ticketTypeRequest.id() != null) {
                            ticketType = existingTicketTypes.get(ticketTypeRequest.id());
                            if (ticketType == null) {
                                ticketType = ticketTypeMapper.toTicketType(ticketTypeRequest);
                                ticketType.setEvent(existingEvent);
                            } else {
                                ticketType.setName(ticketTypeRequest.name());
                                ticketType.setPrice(ticketTypeRequest.price());
                                ticketType.setDescription(ticketTypeRequest.description());
                                ticketType.setTotalAvailable(ticketTypeRequest.totalAvailable());
                            }
                        } else {
                            ticketType = ticketTypeMapper.toTicketType(ticketTypeRequest);
                            ticketType.setEvent(existingEvent);
                        }
                        return ticketType;
                    })
                    .toList();

            Set<UUID> requestTicketTypeIds = request.ticketTypes().stream()
                    .map(UpdateTicketTypeRequest::id)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            existingEvent
                    .getTicketTypes()
                    .removeIf(ticketType ->
                            ticketType.getId() != null && !requestTicketTypeIds.contains(ticketType.getId()));

            existingEvent.getTicketTypes().addAll(updatedTicketTypes);
        }

        Event savedEvent = eventRepository.save(existingEvent);

        return eventMapper.toEventResponse(savedEvent);
    }

    private void validateEventDates(UpdateEventRequest request) {
        if (request.start() != null && request.end() != null) {
            if (request.start().isAfter(request.end())) {
                throw new ValidationException("Event start date must be before end date");
            }
        }

        if (request.salesStart() != null && request.salesEnd() != null) {
            if (request.salesStart().isAfter(request.salesEnd())) {
                throw new ValidationException("Sales start date must be before sales end date");
            }

            assert request.start() != null;
            assert request.end() != null;
            if (request.salesStart().isAfter(request.start())
                    || request.salesEnd().isBefore(request.end())) {
                throw new ValidationException("Sales period must be within event period");
            }
        } else if (request.salesStart() != null || request.salesEnd() != null) {
            throw new ValidationException("Both sales start and sales end dates must be provided together");
        }
    }

    @Transactional
    public void deleteEventForOrganizer(UUID eventId, UUID organizerId) {
        Event event = eventRepository
                .findByIdAndOrganizerId(eventId, organizerId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found for organizer"));

        eventRepository.delete(event);
    }
}
