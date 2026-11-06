package com.fixmyride.auth.entity;

import com.fixmyride.auth.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "outbox_events",
        indexes = {
                @Index(
                        name = "idx_outbox_events_status_created",
                        columnList = "status, created_at"
                ),
                @Index(
                        name = "idx_outbox_events_aggregate",
                        columnList = "aggregate_type, aggregate_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "event_version", nullable = false)
    private int eventVersion;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OutboxStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();

        if (this.status == null) {
            this.status = OutboxStatus.PENDING;
        }
    }


    public static OutboxEvent create(
            String aggregateType,
            String aggregateId,
            String eventType,
            int eventVersion,
            String payload
    ) {
        OutboxEvent event = new OutboxEvent();

        event.aggregateType = aggregateType;
        event.aggregateId = aggregateId;
        event.eventType = eventType;
        event.eventVersion = eventVersion;
        event.payload = payload;
        event.status = OutboxStatus.PENDING;
        event.retryCount = 0;

        return event;
    }


    public void markPublished() {
        this.status = OutboxStatus.PUBLISHED;
        this.publishedAt = Instant.now();
        this.nextRetryAt = null;
    }


    public void markFailed() {
        this.status = OutboxStatus.FAILED;
    }


    public void incrementRetryCount() {
        this.retryCount++;
    }


    public void scheduleRetry(Instant retryAt) {
        this.status = OutboxStatus.PENDING;
        this.nextRetryAt = retryAt;
    }
}
