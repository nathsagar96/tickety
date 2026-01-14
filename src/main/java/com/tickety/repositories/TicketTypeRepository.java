package com.tickety.repositories;

import com.tickety.entities.Event;
import com.tickety.entities.TicketType;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    List<TicketType> findByEvent(Event event);

    Optional<TicketType> findByIdAndEvent(UUID id, Event event);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tt FROM TicketType tt WHERE tt.id = :id")
    Optional<TicketType> findByIdWithLock(@Param("id") UUID id);
}
