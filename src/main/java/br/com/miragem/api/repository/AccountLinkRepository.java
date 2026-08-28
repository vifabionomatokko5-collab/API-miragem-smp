package br.com.miragem.api.repository;

import br.com.miragem.api.model.AccountLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountLinkRepository extends JpaRepository<AccountLink, Long> {

    Optional<AccountLink> findByMinecraftUuid(String minecraftUuid);

    Optional<AccountLink> findByDiscordId(String discordId);

    boolean existsByMinecraftUuid(String minecraftUuid);

    boolean existsByDiscordId(String discordId);
}
