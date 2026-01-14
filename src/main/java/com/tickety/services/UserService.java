package com.tickety.services;

import com.tickety.entities.User;
import com.tickety.exceptions.ResourceNotFoundException;
import com.tickety.repositories.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String subjectId = jwt.getSubject();
            UUID keycloakId = UUID.fromString(subjectId);

            return userRepository
                    .findByKeycloakId(keycloakId)
                    .orElseThrow(() -> ResourceNotFoundException.user(keycloakId.toString()));
        }

        throw new IllegalStateException("Invalid authentication principal");
    }
}
