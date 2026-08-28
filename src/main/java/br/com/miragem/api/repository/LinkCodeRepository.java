package br.com.miragem.api.repository;

import br.com.miragem.api.model.LinkCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkCodeRepository extends JpaRepository<LinkCode, Long> {

    Optional<LinkCode> findByCode(String code);

    Optional<LinkCode> findTopByMinecraftUuidAndUsedFalseOrderByCreatedAtDesc(
            String minecraftUuid
    );
}
