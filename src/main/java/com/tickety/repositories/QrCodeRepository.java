package com.tickety.repositories;

import com.tickety.entities.QrCode;
import com.tickety.entities.Ticket;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

    Optional<QrCode> findByValue(String value);

    Optional<QrCode> findByTicket(Ticket ticket);

    boolean existsByValue(String value);
}
