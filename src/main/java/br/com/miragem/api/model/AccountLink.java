package br.com.miragem.api.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
    name = "account_links",
    indexes = {
        @Index(
            name = "idx_account_minecraft_uuid",
            columnList = "minecraft_uuid",
            unique = true
        ),
        @Index(
            name = "idx_account_discord_id",
            columnList = "discord_id",
            unique = true
        )
    }
)
public class AccountLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "minecraft_uuid",
        nullable = false,
        unique = true,
        length = 36
    )
    private String minecraftUuid;

    @Column(
        name = "minecraft_name",
        nullable = false,
        length = 16
    )
    private String minecraftName;

    @Column(
        name = "discord_id",
        nullable = false,
        unique = true,
        length = 32
    )
    private String discordId;

    @Column(
        name = "created_at",
        nullable = false
    )
    private Instant createdAt;

    @Column(
        name = "linked_at",
        nullable = false
    )
    private Instant linkedAt;

    public AccountLink() {
    }

    public AccountLink(
            String minecraftUuid,
            String minecraftName,
            String discordId
    ) {
        this.minecraftUuid = minecraftUuid;
        this.minecraftName = minecraftName;
        this.discordId = discordId;
        this.createdAt = Instant.now();
        this.linkedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getMinecraftUuid() {
        return minecraftUuid;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public String getDiscordId() {
        return discordId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLinkedAt() {
        return linkedAt;
    }

    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }
}
