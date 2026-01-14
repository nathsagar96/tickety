package com.tickety.repositories;

import com.tickety.entities.Ticket;
import com.tickety.entities.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Page<Ticket> findByPurchaser(User purchaser, Pageable pageable);

    Optional<Ticket> findByIdAndPurchaser(UUID id, User purchaser);
}
