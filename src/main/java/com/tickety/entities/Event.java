package com.tickety.entities;

import com.tickety.enums.EventStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(
        name = "events",
        indexes = {
            @Index(name = "idx_event_status", columnList = "status"),
            @Index(name = "idx_event_start_time", columnList = "start_time"),
            @Index(name = "idx_event_organizer", columnList = "organizer_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "sales_start", nullable = false)
    private LocalDateTime salesStart;

    @Column(name = "sales_end", nullable = false)
    private LocalDateTime salesEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private EventStatus status = EventStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<TicketType> ticketTypes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_staff",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private Set<User> staff = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private Set<User> attendees = new HashSet<>();

    public void addTicketType(TicketType ticketType) {
        ticketTypes.add(ticketType);
        ticketType.setEvent(this);
    }

    public void removeTicketType(TicketType ticketType) {
        ticketTypes.remove(ticketType);
        ticketType.setEvent(null);
    }
}
