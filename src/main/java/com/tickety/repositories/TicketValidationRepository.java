package com.tickety.repositories;

import com.tickety.entities.TicketValidation;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {

    @Query("SELECT tv FROM TicketValidation tv WHERE tv.ticket.ticketType.event.id = :eventId")
    Page<TicketValidation> findByEventId(@Param("eventId") UUID eventId, Pageable pageable);
}
