package br.com.miragem.api.service;

import br.com.miragem.api.model.AccountLink;
import br.com.miragem.api.model.LinkCode;
import br.com.miragem.api.repository.AccountLinkRepository;
import br.com.miragem.api.repository.LinkCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class LinkService {

    private static final String CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    private final LinkCodeRepository linkCodeRepository;
    private final AccountLinkRepository accountLinkRepository;

    public LinkService(
            LinkCodeRepository linkCodeRepository,
            AccountLinkRepository accountLinkRepository
    ) {
        this.linkCodeRepository = linkCodeRepository;
        this.accountLinkRepository = accountLinkRepository;
    }

    @Transactional
    public LinkCode createCode(
            String minecraftUuid,
            String minecraftName,
            int expirationMinutes
    ) {

        Optional<AccountLink> existing =
                accountLinkRepository.findByMinecraftUuid(minecraftUuid);

        if (existing.isPresent()) {
            throw new IllegalStateException(
                    "Esta conta do Minecraft já está vinculada."
            );
        }

        Optional<LinkCode> previous =
                linkCodeRepository
                        .findTopByMinecraftUuidAndUsedFalseOrderByCreatedAtDesc(
                                minecraftUuid
                        );

        if (previous.isPresent() && !previous.get().isExpired()) {
            return previous.get();
        }

        String code = generateUniqueCode();

        Instant now = Instant.now();

        LinkCode linkCode = new LinkCode(
                code,
                minecraftUuid,
                minecraftName,
                now,
                now.plus(Duration.ofMinutes(expirationMinutes))
        );

        return linkCodeRepository.save(linkCode);
    }

    @Transactional(readOnly = true)
    public LinkCode getCode(String code) {

        return linkCodeRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Código de vinculação não encontrado."
                        )
                );
    }

    @Transactional
    public AccountLink confirmLink(
            String code,
            String discordId
    ) {

        LinkCode linkCode = linkCodeRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Código de vinculação inválido."
                        )
                );

        if (linkCode.isUsed()) {
            throw new IllegalStateException(
                    "Este código já foi utilizado."
            );
        }

        if (linkCode.isExpired()) {
            throw new IllegalStateException(
                    "Este código expirou."
            );
        }

        if (accountLinkRepository.existsByMinecraftUuid(
                linkCode.getMinecraftUuid()
        )) {
            throw new IllegalStateException(
                    "Esta conta do Minecraft já está vinculada."
            );
        }

        if (accountLinkRepository.existsByDiscordId(discordId)) {
            throw new IllegalStateException(
                    "Esta conta do Discord já está vinculada."
            );
        }

        AccountLink accountLink = new AccountLink(
                linkCode.getMinecraftUuid(),
                linkCode.getMinecraftName(),
                discordId
        );

        linkCode.setUsed(true);

        linkCodeRepository.save(linkCode);

        return accountLinkRepository.save(accountLink);
    }

    @Transactional(readOnly = true)
    public Optional<AccountLink> getByMinecraftUuid(
            String minecraftUuid
    ) {
        return accountLinkRepository.findByMinecraftUuid(minecraftUuid);
    }

    @Transactional(readOnly = true)
    public Optional<AccountLink> getByDiscordId(
            String discordId
    ) {
        return accountLinkRepository.findByDiscordId(discordId);
    }

    @Transactional
    public void unlinkByMinecraftUuid(
            String minecraftUuid
    ) {

        AccountLink link = accountLinkRepository
                .findByMinecraftUuid(minecraftUuid)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Nenhuma conta vinculada."
                        )
                );

        accountLinkRepository.delete(link);
    }

    private String generateUniqueCode() {

        for (int attempt = 0; attempt < 100; attempt++) {

            String code = randomCode();

            if (linkCodeRepository.findByCode(code).isEmpty()) {
                return code;
            }
        }

        throw new IllegalStateException(
                "Não foi possível gerar um código de vinculação."
        );
    }

    private String randomCode() {

        StringBuilder raw = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            raw.append(
                    CHARACTERS.charAt(
                            RANDOM.nextInt(CHARACTERS.length())
                    )
            );
        }

        return raw.substring(0, 4) + "-" + raw.substring(4);
    }
}
