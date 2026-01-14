package com.tickety.filters;

import com.tickety.entities.User;
import com.tickety.repositories.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProvisioningFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Jwt jwt) {

            try {
                provisionUser(jwt);
            } catch (Exception e) {
                log.error("Error provisioning user from JWT", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void provisionUser(Jwt jwt) {
        String subjectId = jwt.getSubject();
        UUID keycloakId = UUID.fromString(subjectId);

        if (!userRepository.existsByKeycloakId(keycloakId)) {
            String username = jwt.getClaimAsString("preferred_username");
            String email = jwt.getClaimAsString("email");
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");

            User user = User.builder()
                    .keycloakId(keycloakId)
                    .username(username != null ? username : email)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();

            userRepository.save(user);
            log.info("Provisioned new user: {} ({})", username, keycloakId);
        }
    }
}
