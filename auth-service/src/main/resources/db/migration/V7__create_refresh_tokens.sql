CREATE TABLE refresh_tokens
(
    id                   UUID         NOT NULL,
    session_id           UUID         NOT NULL,
    token_hash           VARCHAR(255) NOT NULL,
    family_id            UUID         NOT NULL,
    status               VARCHAR(20)  NOT NULL,
    issued_at            TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    expires_at           TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    consumed_at          TIMESTAMP(6) WITHOUT TIME ZONE,
    revoked_at           TIMESTAMP(6) WITHOUT TIME ZONE,
    replaced_by_token_id UUID,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uk_refresh_tokens_hash UNIQUE (token_hash);

CREATE INDEX idx_refresh_tokens_family ON refresh_tokens (family_id);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT FK_REFRESH_TOKENS_SESSION FOREIGN KEY (session_id) REFERENCES auth_sessions (id);

CREATE INDEX idx_refresh_tokens_session ON refresh_tokens (session_id);