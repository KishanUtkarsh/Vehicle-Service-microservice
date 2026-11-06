CREATE TABLE account_roles
(
    id          UUID NOT NULL,
    account_id  UUID NOT NULL,
    role_id     UUID NOT NULL,
    assigned_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    assigned_by UUID,
    CONSTRAINT pk_account_roles PRIMARY KEY (id)
);

ALTER TABLE account_roles
    ADD CONSTRAINT uk_account_roles UNIQUE (account_id, role_id);

ALTER TABLE account_roles
    ADD CONSTRAINT FK_ACCOUNT_ROLES_ACCOUNT FOREIGN KEY (account_id) REFERENCES auth_accounts (id);

CREATE INDEX idx_account_roles_account ON account_roles (account_id);

ALTER TABLE account_roles
    ADD CONSTRAINT FK_ACCOUNT_ROLES_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);