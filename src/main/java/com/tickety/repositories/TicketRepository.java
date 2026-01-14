package com.tickety.repositories;

import com.tickety.entities.Ticket;
import com.tickety.entities.User;
import com.tickety.enums.TicketStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Page<Ticket> findByPurchaser(User purchaser, Pageable pageable);

    Optional<Ticket> findByIdAndPurchaser(UUID id, User purchaser);

    @Query("SELECT t FROM Ticket t WHERE t.ticketType.event.id = :eventId")
    List<Ticket> findByEventId(@Param("eventId") UUID eventId);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.ticketType.event.id = :eventId AND t.status = :status")
    Long countByEventIdAndStatus(@Param("eventId") UUID eventId, @Param("status") TicketStatus status);
}
