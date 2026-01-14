package com.tickety.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.*;

@Entity
@Table(
        name = "users",
        indexes = {@Index(name = "idx_user_email", columnList = "email")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(name = "keycloak_id", nullable = false, unique = true)
    private UUID keycloakId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Event> organizedEvents = new HashSet<>();

    @OneToMany(mappedBy = "purchaser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Ticket> purchasedTickets = new HashSet<>();

    @ManyToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Event> staffedEvents = new HashSet<>();
}
