package com.tickety.services;

import com.tickety.dtos.requests.CreateEventRequest;
import com.tickety.dtos.requests.CreateTicketTypeRequest;
import com.tickety.dtos.responses.EventResponse;
import com.tickety.entities.Event;
import com.tickety.entities.TicketType;
import com.tickety.entities.User;
import com.tickety.exceptions.BusinessException;
import com.tickety.exceptions.UserNotFoundException;
import com.tickety.mappers.EventMapper;
import com.tickety.repositories.EventRepository;
import com.tickety.repositories.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public EventResponse createEvent(UUID organizerId, CreateEventRequest request) {
        User organizer = userRepository
                .findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("Organizer with ID " + organizerId + " not found"));

        validateEventDates(request);

        Event event = eventMapper.toEvent(request);
        event.setOrganizer(organizer);

        createTicketTypes(event, request.ticketTypes());
        Event savedEvent = eventRepository.save(event);

        return eventMapper.toEventResponse(savedEvent);
    }

    private void validateEventDates(CreateEventRequest request) {
        if (request.start().isAfter(request.end())) {
            throw new BusinessException("Event start date must be before end date");
        }

        if (request.salesStart() != null && request.salesEnd() != null) {
            if (request.salesStart().isAfter(request.salesEnd())) {
                throw new BusinessException("Sales start date must be before sales end date");
            }

            if (request.salesStart().isAfter(request.start())
                    || request.salesEnd().isBefore(request.end())) {
                throw new BusinessException("Sales period must be within event period");
            }
        } else if (request.salesStart() != null || request.salesEnd() != null) {
            throw new BusinessException("Both sales start and sales end dates must be provided together");
        }
    }

    private void createTicketTypes(Event event, List<CreateTicketTypeRequest> ticketTypeRequests) {
        List<TicketType> ticketTypes = ticketTypeRequests.stream()
                .map(request -> {
                    TicketType ticketType = eventMapper.toTicketType(request);
                    ticketType.setEvent(event);
                    return ticketType;
                })
                .toList();

        event.setTicketTypes(ticketTypes);
    }
}
