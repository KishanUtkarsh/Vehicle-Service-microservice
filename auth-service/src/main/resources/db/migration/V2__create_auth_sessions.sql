CREATE TABLE auth_sessions
(
    id             UUID        NOT NULL,
    account_id     UUID        NOT NULL,
    client_type    VARCHAR(20) NOT NULL,
    device_id      VARCHAR(255),
    device_name    VARCHAR(150),
    os_version     VARCHAR(100),
    app_version    VARCHAR(50),
    browser        VARCHAR(100),
    ip_address     VARCHAR(45),
    user_agent     VARCHAR(1000),
    country        VARCHAR(100),
    city           VARCHAR(100),
    status         VARCHAR(20) NOT NULL,
    created_at     TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    last_active_at TIMESTAMP(6) WITHOUT TIME ZONE,
    expires_at     TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    revoked_at     TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_auth_sessions PRIMARY KEY (id)
);

CREATE INDEX idx_auth_sessions_device ON auth_sessions (device_id);

ALTER TABLE auth_sessions
    ADD CONSTRAINT FK_SESSION_ACCOUNT FOREIGN KEY (account_id) REFERENCES auth_accounts (id);

CREATE INDEX idx_auth_sessions_account ON auth_sessions (account_id);