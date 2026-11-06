package com.fixmyride.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "password_reset_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_password_reset_token_hash",
                        columnNames = "token_hash"
                )
        },
        indexes = {
                @Index(
                        name = "idx_password_reset_account",
                        columnList = "account_id"
                ),
                @Index(
                        name = "idx_password_reset_expiry",
                        columnList = "expires_at"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_password_reset_account"
            )
    )
    private AuthAccount account;

    @Column(
            name = "token_hash",
            nullable = false,
            unique = true,
            length = 255
    )
    private String tokenHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }


    public static PasswordResetToken create(
            AuthAccount account,
            String tokenHash,
            Instant expiresAt
    ) {
        PasswordResetToken token = new PasswordResetToken();

        token.account = account;
        token.tokenHash = tokenHash;
        token.expiresAt = expiresAt;

        return token;
    }


    public void markAsUsed() {
        if (!isValid()) {
            throw new IllegalStateException(
                    "Password reset token is no longer valid"
            );
        }

        this.usedAt = Instant.now();
    }


    public boolean isValid() {
        return this.usedAt == null && !isExpired();
    }


    public boolean isExpired() {
        return !this.expiresAt.isAfter(Instant.now());
    }
}
