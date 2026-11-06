CREATE TABLE permissions
(
    id          UUID         NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

ALTER TABLE permissions
    ADD CONSTRAINT uk_permissions_name UNIQUE (name);