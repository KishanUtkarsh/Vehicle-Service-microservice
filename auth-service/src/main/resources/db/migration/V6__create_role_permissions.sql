CREATE TABLE role_permissions
(
    id            UUID NOT NULL,
    role_id       UUID NOT NULL,
    permission_id UUID NOT NULL,
    assigned_at   TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (id)
);

ALTER TABLE role_permissions
    ADD CONSTRAINT uk_role_permissions UNIQUE (role_id, permission_id);

ALTER TABLE role_permissions
    ADD CONSTRAINT FK_ROLE_PERMISSIONS_PERMISSION FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT FK_ROLE_PERMISSIONS_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);

CREATE INDEX idx_role_permissions_role ON role_permissions (role_id);