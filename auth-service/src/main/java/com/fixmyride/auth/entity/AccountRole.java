package com.fixmyride.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "account_roles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_account_roles",
                        columnNames = {"account_id", "role_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_account_roles_account",
                        columnList = "account_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_account_roles_account")
    )
    private AuthAccount account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "role_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_account_roles_role")
    )
    private Role role;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private Instant assignedAt;

    @Column(name = "assigned_by")
    private UUID assignedBy;

    @PrePersist
    protected void onCreate() {
        this.assignedAt = Instant.now();
    }

    public static AccountRole assign(
            AuthAccount account,
            Role role,
            UUID assignedBy
    ) {
        AccountRole accountRole = new AccountRole();

        accountRole.account = account;
        accountRole.role = role;
        accountRole.assignedBy = assignedBy;

        return accountRole;
    }
}
