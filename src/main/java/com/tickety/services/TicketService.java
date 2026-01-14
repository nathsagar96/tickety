package com.tickety.services;

import com.tickety.dtos.responses.QrCodeResponse;
import com.tickety.dtos.responses.TicketListResponse;
import com.tickety.dtos.responses.TicketPurchaseResponse;
import com.tickety.dtos.responses.TicketResponse;
import com.tickety.entities.QrCode;
import com.tickety.entities.Ticket;
import com.tickety.entities.TicketType;
import com.tickety.entities.User;
import com.tickety.enums.EventStatus;
import com.tickety.enums.TicketStatus;
import com.tickety.exceptions.*;
import com.tickety.mappers.TicketMapper;
import com.tickety.repositories.TicketRepository;
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
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketTypeService ticketTypeService;
    private final UserService userService;
    private final QrCodeService qrCodeService;
    private final TicketMapper ticketMapper;

    @Transactional
    public TicketPurchaseResponse purchaseTicket(UUID publishedEventId, UUID ticketTypeId) {
        User purchaser = userService.getCurrentUser();

        // Lock the ticket type row to prevent overselling
        TicketType ticketType = ticketTypeService.findByIdWithLock(ticketTypeId);

        // Validate event is published
        if (ticketType.getEvent().getStatus() != EventStatus.PUBLISHED) {
            throw InvalidEventStatusException.notPublished();
        }

        // Validate event ID matches
        if (!ticketType.getEvent().getId().equals(publishedEventId)) {
            throw new BusinessException("Ticket type does not belong to this event");
        }

        // Validate sales window
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(ticketType.getEvent().getSalesStart())) {
            throw SalesWindowException.notStarted();
        }
        if (now.isAfter(ticketType.getEvent().getSalesEnd())) {
            throw SalesWindowException.ended();
        }

        // Check availability
        if (!ticketType.hasAvailableTickets()) {
            throw new TicketSoldOutException(ticketType.getName());
        }

        // Create ticket
        Ticket ticket = Ticket.builder()
                .ticketType(ticketType)
                .purchaser(purchaser)
                .purchasePrice(ticketType.getPrice())
                .status(TicketStatus.PURCHASED)
                .purchasedAt(LocalDateTime.now())
                .build();

        // Decrement available count
        ticketType.decrementAvailableCount();

        // Save ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // Generate QR code
        QrCode qrCode = qrCodeService.generateQrCode(savedTicket);

        log.info(
                "Ticket {} purchased by user {} for event {}",
                savedTicket.getId(),
                purchaser.getId(),
                publishedEventId);

        QrCodeResponse qrCodeResponse = new QrCodeResponse(
                qrCode.getId(), qrCode.getValue(), java.util.Base64.getEncoder().encodeToString(qrCode.getImageData()));

        return new TicketPurchaseResponse(savedTicket.getId(), "Ticket purchased successfully", qrCodeResponse);
    }

    @Transactional(readOnly = true)
    public Page<TicketListResponse> getMyTickets(Pageable pageable) {
        User user = userService.getCurrentUser();
        Page<Ticket> tickets = ticketRepository.findByPurchaser(user, pageable);
        return tickets.map(ticketMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicketById(UUID ticketId) {
        User user = userService.getCurrentUser();
        Ticket ticket =
                ticketRepository.findByIdAndPurchaser(ticketId, user).orElseThrow(UnauthorizedAccessException::ticket);

        return ticketMapper.toResponse(ticket);
    }

    @Transactional(readOnly = true)
    public QrCodeResponse getTicketQrCode(UUID ticketId) {
        User user = userService.getCurrentUser();
        Ticket ticket =
                ticketRepository.findByIdAndPurchaser(ticketId, user).orElseThrow(UnauthorizedAccessException::ticket);

        return qrCodeService.getQrCodeByTicket(ticket);
    }

    @Transactional(readOnly = true)
    public Ticket findById(UUID ticketId) {
        return ticketRepository
                .findById(ticketId)
                .orElseThrow(() -> ResourceNotFoundException.ticket(ticketId.toString()));
    }
}
