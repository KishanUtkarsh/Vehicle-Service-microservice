package com.fixmyride.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permissions_name",
                        columnNames = "name"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }


    public static Permission create(String name, String description) {
        Permission permission = new Permission();

        permission.name = name;
        permission.description = description;

        return permission;
    }


    public void updateDescription(String description) {
        this.description = description;
    }
}
