package com.fixmyride.auth.entity;

import com.fixmyride.auth.enums.ClientType;
import com.fixmyride.auth.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "auth_sessions",
        indexes = {
                @Index(
                        name = "idx_auth_sessions_account",
                        columnList = "account_id"
                ),
                @Index(
                        name = "idx_auth_sessions_device",
                        columnList = "device_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_session_account")
    )
    private AuthAccount account;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false, length = 20)
    private ClientType clientType;

    @Column(name = "device_id", length = 255)
    private String deviceId;

    @Column(name = "device_name", length = 150)
    private String deviceName;

    @Column(name = "os_version", length = 100)
    private String osVersion;

    @Column(name = "app_version", length = 50)
    private String appVersion;

    @Column(name = "browser", length = 100)
    private String browser;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 1000)
    private String userAgent;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "city", length = 100)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SessionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "last_active_at")
    private Instant lastActiveAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.lastActiveAt = this.createdAt;

        if (this.status == null) {
            this.status = SessionStatus.ACTIVE;
        }
    }

    public static AuthSession create(
            AuthAccount account,
            ClientType clientType,
            String deviceId,
            String deviceName,
            String osVersion,
            String appVersion,
            String browser,
            String ipAddress,
            String userAgent,
            Instant expiresAt
    ) {
        AuthSession session = new AuthSession();

        session.account = account;
        session.clientType = clientType;
        session.deviceId = deviceId;
        session.deviceName = deviceName;
        session.osVersion = osVersion;
        session.appVersion = appVersion;
        session.browser = browser;
        session.ipAddress = ipAddress;
        session.userAgent = userAgent;
        session.expiresAt = expiresAt;
        session.status = SessionStatus.ACTIVE;

        return session;
    }

    public void updateLastActive() {
        this.lastActiveAt = Instant.now();
    }

    public void revoke() {
        this.status = SessionStatus.REVOKED;
        this.revokedAt = Instant.now();
    }

    public boolean isActive() {
        return this.status == SessionStatus.ACTIVE
                && this.expiresAt.isAfter(Instant.now());
    }
}
