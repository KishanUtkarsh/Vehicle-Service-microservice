package com.fixmyride.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "role_permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_role_permissions",
                        columnNames = {"role_id", "permission_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_role_permissions_role",
                        columnList = "role_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "role_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_permissions_role")
    )
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "permission_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_permissions_permission")
    )
    private Permission permission;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private Instant assignedAt;


    @PrePersist
    protected void onCreate() {
        this.assignedAt = Instant.now();
    }


    public static RolePermission assign(
            Role role,
            Permission permission
    ) {
        RolePermission rolePermission = new RolePermission();

        rolePermission.role = role;
        rolePermission.permission = permission;

        return rolePermission;
    }
}
