CREATE TABLE email_verification_tokens
(
    id          UUID         NOT NULL,
    account_id  UUID         NOT NULL,
    token_hash  VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    expires_at  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    verified_at TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_email_verification_tokens PRIMARY KEY (id)
);

ALTER TABLE email_verification_tokens
    ADD CONSTRAINT uk_email_verification_token_hash UNIQUE (token_hash);

CREATE INDEX idx_email_verification_expiry ON email_verification_tokens (expires_at);

ALTER TABLE email_verification_tokens
    ADD CONSTRAINT FK_EMAIL_VERIFICATION_ACCOUNT FOREIGN KEY (account_id) REFERENCES auth_accounts (id);

CREATE INDEX idx_email_verification_account ON email_verification_tokens (account_id);