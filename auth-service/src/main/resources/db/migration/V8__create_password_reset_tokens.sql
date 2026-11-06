CREATE TABLE password_reset_tokens
(
    id         UUID         NOT NULL,
    account_id UUID         NOT NULL,
    token_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    expires_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    used_at    TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_password_reset_tokens PRIMARY KEY (id)
);

ALTER TABLE password_reset_tokens
    ADD CONSTRAINT uk_password_reset_token_hash UNIQUE (token_hash);

CREATE INDEX idx_password_reset_expiry ON password_reset_tokens (expires_at);

ALTER TABLE password_reset_tokens
    ADD CONSTRAINT FK_PASSWORD_RESET_ACCOUNT FOREIGN KEY (account_id) REFERENCES auth_accounts (id);

CREATE INDEX idx_password_reset_account ON password_reset_tokens (account_id);