CREATE TABLE auth_accounts
(
    id             UUID         NOT NULL,
    email          VARCHAR(255) NOT NULL,
    phone_number   VARCHAR(20),
    password_hash  VARCHAR(255) NOT NULL,
    status         VARCHAR(30)  NOT NULL,
    email_verified BOOLEAN      NOT NULL,
    phone_verified BOOLEAN      NOT NULL,
    version        BIGINT       NOT NULL,
    created_at     TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    last_login_at  TIMESTAMP(6) WITHOUT TIME ZONE,
    CONSTRAINT pk_auth_accounts PRIMARY KEY (id)
);

ALTER TABLE auth_accounts
    ADD CONSTRAINT uk_auth_accounts_email UNIQUE (email);

ALTER TABLE auth_accounts
    ADD CONSTRAINT uk_auth_accounts_phone UNIQUE (phone_number);