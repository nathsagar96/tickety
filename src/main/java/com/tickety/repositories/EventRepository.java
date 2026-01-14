package com.tickety.repositories;

import com.tickety.entities.Event;
import com.tickety.entities.User;
import com.tickety.enums.EventStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, UUID> {

    Page<Event> findByOrganizer(User organizer, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.status = :status")
    Page<Event> findPublishedEvents(@Param("status") EventStatus status, Pageable pageable);

    Optional<Event> findByIdAndOrganizer(UUID id, User organizer);
}
