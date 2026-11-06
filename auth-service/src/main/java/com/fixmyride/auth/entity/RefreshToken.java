package com.fixmyride.auth.entity;

import com.fixmyride.auth.enums.TokenStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_refresh_tokens_hash",
                        columnNames = "token_hash"
                )
        },
        indexes = {
                @Index(
                        name = "idx_refresh_tokens_session",
                        columnList = "session_id"
                ),
                @Index(
                        name = "idx_refresh_tokens_family",
                        columnList = "family_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "session_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_refresh_tokens_session")
    )
    private AuthSession session;

    @Column(name = "token_hash", nullable = false, unique = true, length = 255)
    private String tokenHash;

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TokenStatus status;

    @Column(name = "issued_at", nullable = false, updatable = false)
    private Instant issuedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "consumed_at")
    private Instant consumedAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "replaced_by_token_id")
    private UUID replacedByTokenId;


    @PrePersist
    protected void onCreate() {
        this.issuedAt = Instant.now();

        if (this.status == null) {
            this.status = TokenStatus.ACTIVE;
        }
    }


    public static RefreshToken create(
            AuthSession session,
            String tokenHash,
            UUID familyId,
            Instant expiresAt
    ) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.session = session;
        refreshToken.tokenHash = tokenHash;
        refreshToken.familyId = familyId;
        refreshToken.expiresAt = expiresAt;
        refreshToken.status = TokenStatus.ACTIVE;

        return refreshToken;
    }


    public void consume(UUID replacementTokenId) {
        if (this.status != TokenStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only active refresh tokens can be consumed"
            );
        }

        if (isExpired()) {
            throw new IllegalStateException(
                    "Refresh token has expired"
            );
        }

        this.status = TokenStatus.CONSUMED;
        this.consumedAt = Instant.now();
        this.replacedByTokenId = replacementTokenId;
    }


    public void revoke() {
        if (this.status == TokenStatus.REVOKED) {
            return;
        }

        this.status = TokenStatus.REVOKED;
        this.revokedAt = Instant.now();
    }


    public boolean isValid() {
        return this.status == TokenStatus.ACTIVE
                && !isExpired();
    }


    public boolean isExpired() {
        return !this.expiresAt.isAfter(Instant.now());
    }
}
