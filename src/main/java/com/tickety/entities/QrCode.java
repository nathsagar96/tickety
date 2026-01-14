package com.tickety.entities;

import com.tickety.enums.QrCodeStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "qr_codes",
        indexes = {
            @Index(name = "idx_qr_code_value", columnList = "value", unique = true),
            @Index(name = "idx_qr_code_ticket", columnList = "ticket_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCode extends BaseEntity {

    @Column(name = "value", nullable = false, unique = true)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private QrCodeStatus status = QrCodeStatus.ACTIVE;

    @Lob
    @Column(name = "image_data", columnDefinition = "BYTEA")
    private byte[] imageData;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
}
