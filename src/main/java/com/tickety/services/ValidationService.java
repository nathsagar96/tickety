package com.tickety.services;

import com.tickety.dtos.requests.ValidateTicketRequest;
import com.tickety.dtos.responses.ValidationListResponse;
import com.tickety.dtos.responses.ValidationResponse;
import com.tickety.entities.QrCode;
import com.tickety.entities.Ticket;
import com.tickety.entities.TicketValidation;
import com.tickety.enums.TicketStatus;
import com.tickety.enums.TicketValidationStatus;
import com.tickety.exceptions.TicketValidationException;
import com.tickety.mappers.ValidationMapper;
import com.tickety.repositories.TicketValidationRepository;
import java.time.LocalDateTime;
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
public class ValidationService {

    private final TicketValidationRepository validationRepository;
    private final TicketService ticketService;
    private final QrCodeService qrCodeService;
    private final UserService userService;
    private final ValidationMapper validationMapper;

    @Transactional
    public ValidationResponse validateTicket(UUID eventId, ValidateTicketRequest request) {
        Ticket ticket;

        // Find ticket by QR code or ticket ID
        if (request.qrCodeValue() != null && !request.qrCodeValue().isBlank()) {
            QrCode qrCode = qrCodeService.findByValue(request.qrCodeValue());
            ticket = qrCode.getTicket();
        } else {
            ticket = ticketService.findById(request.ticketId());
        }

        // Validate ticket belongs to event
        if (!ticket.getTicketType().getEvent().getId().equals(eventId)) {
            throw TicketValidationException.invalidEvent();
        }

        // Determine validation status
        TicketValidationStatus validationStatus;
        String message;

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            validationStatus = TicketValidationStatus.CANCELLED;
            message = "Ticket has been cancelled";
        } else if (ticket.getStatus() == TicketStatus.USED) {
            validationStatus = TicketValidationStatus.ALREADY_USED;
            message = "Ticket has already been used";
        } else if (ticket.getStatus() == TicketStatus.PURCHASED && ticket.isValid()) {
            validationStatus = TicketValidationStatus.VALID;
            message = "Ticket is valid";

            // Mark ticket as used
            ticket.setStatus(TicketStatus.USED);
        } else {
            validationStatus = TicketValidationStatus.INVALID;
            message = "Ticket is invalid";
        }

        // Get current user from JWT
        String validatedBy = userService.getCurrentUser().getUsername();

        // Create validation record
        TicketValidation validation = TicketValidation.builder()
                .ticket(ticket)
                .status(validationStatus)
                .validationMethod(request.validationMethod())
                .validatedAt(LocalDateTime.now())
                .validatedBy(validatedBy)
                .notes(request.notes())
                .build();

        TicketValidation savedValidation = validationRepository.save(validation);

        log.info("Validated ticket {} for event {} - Status: {}", ticket.getId(), eventId, validationStatus);

        ValidationResponse response = validationMapper.toResponse(savedValidation);
        return new ValidationResponse(
                response.id(),
                response.status(),
                response.validationMethod(),
                response.validatedAt(),
                response.validatedBy(),
                response.notes(),
                message,
                response.ticket());
    }

    @Transactional(readOnly = true)
    public Page<ValidationListResponse> getEventValidations(UUID eventId, Pageable pageable) {
        Page<TicketValidation> validations = validationRepository.findByEventId(eventId, pageable);
        return validations.map(validationMapper::toListResponse);
    }
}
