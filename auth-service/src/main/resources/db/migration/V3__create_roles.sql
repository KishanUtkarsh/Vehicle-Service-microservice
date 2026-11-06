CREATE TABLE roles
(
    id          UUID        NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

ALTER TABLE roles
    ADD CONSTRAINT uk_roles_name UNIQUE (name);