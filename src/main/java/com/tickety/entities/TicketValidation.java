package com.tickety.entities;

import com.tickety.enums.TicketValidationStatus;
import com.tickety.enums.ValidationMethod;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(
        name = "ticket_validations",
        indexes = {
            @Index(name = "idx_validation_ticket", columnList = "ticket_id"),
            @Index(name = "idx_validation_time", columnList = "validated_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketValidation extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketValidationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_method", nullable = false)
    private ValidationMethod validationMethod;

    @Column(name = "validated_at", nullable = false)
    private LocalDateTime validatedAt;

    @Column(name = "validated_by")
    private String validatedBy;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @PrePersist
    public void prePersist() {
        if (validatedAt == null) {
            validatedAt = LocalDateTime.now();
        }
    }
}
