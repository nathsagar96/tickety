package com.tickety.configs;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication.getPrincipal().equals("anonymousUser")) {
                return Optional.of("SYSTEM");
            }

            if (authentication.getPrincipal() instanceof Jwt jwt) {
                String username = jwt.getClaimAsString("preferred_username");
                return Optional.ofNullable(username).or(() -> Optional.of(jwt.getSubject()));
            }

            return Optional.of(authentication.getName());
        };
    }
}
