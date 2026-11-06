package com.fixmyride.auth.entity;

import com.fixmyride.auth.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "auth_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_auth_accounts_email",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "uk_auth_accounts_phone",
                        columnNames = "phone_number"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "email",
            nullable = false,
            length = 255
    )
    private String email;

    @Column(
            name = "phone_number",
            length = 20
    )
    private String phoneNumber;

    @Column(
            name = "password_hash",
            nullable = false,
            length = 255
    )
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private AccountStatus status;

    @Column(
            name = "email_verified",
            nullable = false
    )
    private boolean emailVerified;

    @Column(
            name = "phone_verified",
            nullable = false
    )
    private boolean phoneVerified;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;


    // Entity lifecycle callbacks

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = AccountStatus.PENDING_VERIFICATION;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }


    // Factory method

    public static AuthAccount create(
            String email,
            String phoneNumber,
            String passwordHash
    ) {
        AuthAccount account = new AuthAccount();

        account.email = email;
        account.phoneNumber = phoneNumber;
        account.passwordHash = passwordHash;
        account.status = AccountStatus.PENDING_VERIFICATION;
        account.emailVerified = false;
        account.phoneVerified = false;

        return account;
    }


    // Account lifecycle methods

    public void activate() {
        if (!emailVerified) {
            throw new IllegalStateException(
                    "Email must be verified before account activation"
            );
        }

        this.status = AccountStatus.ACTIVE;
    }

    public void disable() {
        this.status = AccountStatus.DISABLED;
    }

    public void lock() {
        if (this.status == AccountStatus.DISABLED) {
            throw new IllegalStateException(
                    "Disabled account cannot be temporarily locked"
            );
        }

        this.status = AccountStatus.TEMPORARILY_LOCKED;
    }

    public void unlock() {
        if (this.status != AccountStatus.TEMPORARILY_LOCKED) {
            throw new IllegalStateException(
                    "Account is not temporarily locked"
            );
        }

        this.status = AccountStatus.ACTIVE;
    }


    // Credential methods

    public void changePassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void markEmailVerified() {
        this.emailVerified = true;
    }

    public void markPhoneVerified() {
        this.phoneVerified = true;
    }


    // Login tracking

    public void updateLastLogin() {
        this.lastLoginAt = Instant.now();
    }


    // Business helper methods

    public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }

    public boolean isDisabled() {
        return this.status == AccountStatus.DISABLED;
    }
}
