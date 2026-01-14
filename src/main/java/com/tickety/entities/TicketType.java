package com.tickety.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(
        name = "ticket_types",
        indexes = {@Index(name = "idx_ticket_type_event", columnList = "event_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketType extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "total_available", nullable = false)
    private Integer totalAvailable;

    @Column(name = "available_count", nullable = false)
    private Integer availableCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "ticketType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Ticket> tickets = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (availableCount == null) {
            availableCount = totalAvailable;
        }
    }

    public boolean hasAvailableTickets() {
        return availableCount != null && availableCount > 0;
    }

    public void decrementAvailableCount() {
        if (availableCount > 0) {
            availableCount--;
        }
    }

    public void incrementAvailableCount() {
        if (availableCount < totalAvailable) {
            availableCount++;
        }
    }
}
