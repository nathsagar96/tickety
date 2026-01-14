package com.tickety.entities;

import com.tickety.enums.TicketStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(
        name = "tickets",
        indexes = {
            @Index(name = "idx_ticket_purchaser", columnList = "purchaser_id"),
            @Index(name = "idx_ticket_type", columnList = "ticket_type_id"),
            @Index(name = "idx_ticket_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private TicketStatus status = TicketStatus.PURCHASED;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "purchased_at", nullable = false)
    private LocalDateTime purchasedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchaser_id", nullable = false)
    private User purchaser;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<TicketValidation> validations = new HashSet<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<QrCode> qrCodes = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (purchasedAt == null) {
            purchasedAt = LocalDateTime.now();
        }
    }

    public boolean isUsed() {
        return status == TicketStatus.USED;
    }

    public boolean isCancelled() {
        return status == TicketStatus.CANCELLED;
    }

    public boolean isValid() {
        return status == TicketStatus.PURCHASED && !isUsed() && !isCancelled();
    }
}
