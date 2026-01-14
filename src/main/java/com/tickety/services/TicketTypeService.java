package com.tickety.services;

import com.tickety.dtos.requests.CreateTicketTypeRequest;
import com.tickety.dtos.requests.UpdateTicketTypeRequest;
import com.tickety.dtos.responses.TicketTypeListResponse;
import com.tickety.dtos.responses.TicketTypeResponse;
import com.tickety.entities.Event;
import com.tickety.entities.TicketType;
import com.tickety.exceptions.ResourceNotFoundException;
import com.tickety.mappers.TicketTypeMapper;
import com.tickety.repositories.TicketTypeRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final EventService eventService;
    private final TicketTypeMapper ticketTypeMapper;

    @Transactional
    public TicketTypeResponse createTicketType(UUID eventId, CreateTicketTypeRequest request) {
        Event event = eventService.findById(eventId);

        TicketType ticketType = ticketTypeMapper.toEntity(request);
        event.addTicketType(ticketType);

        TicketType savedTicketType = ticketTypeRepository.save(ticketType);
        log.info("Created ticket type {} for event {}", savedTicketType.getId(), eventId);

        return ticketTypeMapper.toResponse(savedTicketType);
    }

    @Transactional(readOnly = true)
    public List<TicketTypeListResponse> getTicketTypesByEvent(UUID eventId) {
        Event event = eventService.findById(eventId);
        List<TicketType> ticketTypes = ticketTypeRepository.findByEvent(event);

        return ticketTypes.stream().map(ticketTypeMapper::toListResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketTypeResponse getTicketTypeById(UUID eventId, UUID ticketTypeId) {
        Event event = eventService.findById(eventId);
        TicketType ticketType = ticketTypeRepository
                .findByIdAndEvent(ticketTypeId, event)
                .orElseThrow(() -> ResourceNotFoundException.ticketType(ticketTypeId.toString()));

        return ticketTypeMapper.toResponse(ticketType);
    }

    @Transactional
    public TicketTypeResponse updateTicketType(UUID eventId, UUID ticketTypeId, UpdateTicketTypeRequest request) {
        Event event = eventService.findById(eventId);
        TicketType ticketType = ticketTypeRepository
                .findByIdAndEvent(ticketTypeId, event)
                .orElseThrow(() -> ResourceNotFoundException.ticketType(ticketTypeId.toString()));

        ticketTypeMapper.updateEntityFromRequest(request, ticketType);

        if (request.totalAvailable() != null && request.totalAvailable() < ticketType.getTotalAvailable()) {
            int soldCount = ticketType.getTotalAvailable() - ticketType.getAvailableCount();
            ticketType.setAvailableCount(request.totalAvailable() - soldCount);
        }

        TicketType updatedTicketType = ticketTypeRepository.save(ticketType);
        log.info("Updated ticket type {}", ticketTypeId);

        return ticketTypeMapper.toResponse(updatedTicketType);
    }

    @Transactional
    public void deleteTicketType(UUID eventId, UUID ticketTypeId) {
        Event event = eventService.findById(eventId);
        TicketType ticketType = ticketTypeRepository
                .findByIdAndEvent(ticketTypeId, event)
                .orElseThrow(() -> ResourceNotFoundException.ticketType(ticketTypeId.toString()));

        event.removeTicketType(ticketType);
        ticketTypeRepository.delete(ticketType);

        log.info("Deleted ticket type {}", ticketTypeId);
    }

    @Transactional(readOnly = true)
    public TicketType findById(UUID ticketTypeId) {
        return ticketTypeRepository
                .findById(ticketTypeId)
                .orElseThrow(() -> ResourceNotFoundException.ticketType(ticketTypeId.toString()));
    }

    @Transactional
    public TicketType findByIdWithLock(UUID ticketTypeId) {
        return ticketTypeRepository
                .findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> ResourceNotFoundException.ticketType(ticketTypeId.toString()));
    }
}
