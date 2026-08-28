package br.com.miragem.api.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
    name = "link_codes",
    indexes = {
        @Index(name = "idx_link_code_code", columnList = "code", unique = true),
        @Index(name = "idx_link_code_minecraft_uuid", columnList = "minecraft_uuid")
    }
)
public class LinkCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 16)
    private String code;

    @Column(name = "minecraft_uuid", nullable = false, length = 36)
    private String minecraftUuid;

    @Column(name = "minecraft_name", nullable = false, length = 16)
    private String minecraftName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used", nullable = false)
    private boolean used;

    public LinkCode() {
    }

    public LinkCode(
            String code,
            String minecraftUuid,
            String minecraftName,
            Instant createdAt,
            Instant expiresAt
    ) {
        this.code = code;
        this.minecraftUuid = minecraftUuid;
        this.minecraftName = minecraftName;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.used = false;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getMinecraftUuid() {
        return minecraftUuid;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
