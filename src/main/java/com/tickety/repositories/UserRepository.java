package com.tickety.repositories;

import com.tickety.entities.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByKeycloakId(UUID keycloakId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByKeycloakId(UUID keycloakId);
}
