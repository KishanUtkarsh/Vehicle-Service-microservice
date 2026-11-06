package com.fixmyride.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "email_verification_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_email_verification_token_hash",
                        columnNames = "token_hash"
                )
        },
        indexes = {
                @Index(
                        name = "idx_email_verification_account",
                        columnList = "account_id"
                ),
                @Index(
                        name = "idx_email_verification_expiry",
                        columnList = "expires_at"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_email_verification_account"
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

    @Column(name = "verified_at")
    private Instant verifiedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }


    public static EmailVerificationToken create(
            AuthAccount account,
            String tokenHash,
            Instant expiresAt
    ) {
        EmailVerificationToken token = new EmailVerificationToken();

        token.account = account;
        token.tokenHash = tokenHash;
        token.expiresAt = expiresAt;

        return token;
    }


    public void markAsVerified() {
        if (!isValid()) {
            throw new IllegalStateException(
                    "Email verification token is no longer valid"
            );
        }

        this.verifiedAt = Instant.now();
    }


    public boolean isValid() {
        return this.verifiedAt == null && !isExpired();
    }


    public boolean isExpired() {
        return !this.expiresAt.isAfter(Instant.now());
    }
}
