CREATE TABLE outbox_events
(
    id             UUID         NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id   VARCHAR(100) NOT NULL,
    event_type     VARCHAR(100) NOT NULL,
    event_version  INTEGER      NOT NULL,
    payload        TEXT         NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    retry_count    INTEGER      NOT NULL,
    created_at     TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    published_at   TIMESTAMP(6) WITHOUT TIME ZONE,
    next_retry_at  TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_outbox_events PRIMARY KEY (id)
);

CREATE INDEX idx_outbox_events_aggregate ON outbox_events (aggregate_type, aggregate_id);

CREATE INDEX idx_outbox_events_status_created ON outbox_events (status, created_at);