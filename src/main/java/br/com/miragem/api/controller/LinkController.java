package br.com.miragem.api.controller;

import br.com.miragem.api.model.AccountLink;
import br.com.miragem.api.model.LinkCode;
import br.com.miragem.api.service.LinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/link")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCode(
            @Valid @RequestBody CreateLinkRequest request
    ) {

        try {

            LinkCode linkCode = linkService.createCode(
                    request.minecraftUuid(),
                    request.minecraftName(),
                    request.expirationMinutes()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "code", linkCode.getCode(),
                            "minecraftUuid", linkCode.getMinecraftUuid(),
                            "minecraftName", linkCode.getMinecraftName(),
                            "createdAt", linkCode.getCreatedAt(),
                            "expiresAt", linkCode.getExpiresAt()
                    )
            );

        } catch (IllegalStateException exception) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "success", false,
                            "message", exception.getMessage()
                    ));
        }
    }

    @GetMapping("/status/{code}")
    public ResponseEntity<?> status(
            @PathVariable String code
    ) {

        try {

            LinkCode linkCode = linkService.getCode(code);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "code", linkCode.getCode(),
                            "used", linkCode.isUsed(),
                            "expired", linkCode.isExpired(),
                            "minecraftUuid", linkCode.getMinecraftUuid(),
                            "minecraftName", linkCode.getMinecraftName(),
                            "createdAt", linkCode.getCreatedAt(),
                            "expiresAt", linkCode.getExpiresAt()
                    )
            );

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", exception.getMessage()
                    ));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(
            @Valid @RequestBody ConfirmLinkRequest request
    ) {

        try {

            AccountLink link = linkService.confirmLink(
                    request.code(),
                    request.discordId()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Conta vinculada com sucesso.",
                            "link", Map.of(
                                    "minecraftUuid",
                                    link.getMinecraftUuid(),
                                    "minecraftName",
                                    link.getMinecraftName(),
                                    "discordId",
                                    link.getDiscordId(),
                                    "linkedAt",
                                    link.getLinkedAt()
                            )
                    )
            );

        } catch (IllegalArgumentException | IllegalStateException exception) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "success", false,
                            "message", exception.getMessage()
                    ));
        }
    }

    @GetMapping("/minecraft/{minecraftUuid}")
    public ResponseEntity<?> getMinecraftLink(
            @PathVariable String minecraftUuid
    ) {

        Optional<AccountLink> link =
                linkService.getByMinecraftUuid(minecraftUuid);

        if (link.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "linked", false,
                            "message", "Conta não vinculada."
                    ));
        }

        AccountLink accountLink = link.get();

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "linked", true,
                        "minecraftUuid",
                        accountLink.getMinecraftUuid(),
                        "minecraftName",
                        accountLink.getMinecraftName(),
                        "discordId",
                        accountLink.getDiscordId(),
                        "linkedAt",
                        accountLink.getLinkedAt()
                )
        );
    }

    @GetMapping("/discord/{discordId}")
    public ResponseEntity<?> getDiscordLink(
            @PathVariable String discordId
    ) {

        Optional<AccountLink> link =
                linkService.getByDiscordId(discordId);

        if (link.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "linked", false,
                            "message", "Conta não vinculada."
                    ));
        }

        AccountLink accountLink = link.get();

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "linked", true,
                        "minecraftUuid",
                        accountLink.getMinecraftUuid(),
                        "minecraftName",
                        accountLink.getMinecraftName(),
                        "discordId",
                        accountLink.getDiscordId(),
                        "linkedAt",
                        accountLink.getLinkedAt()
                )
        );
    }

    @DeleteMapping("/minecraft/{minecraftUuid}")
    public ResponseEntity<?> unlink(
            @PathVariable String minecraftUuid
    ) {

        try {

            linkService.unlinkByMinecraftUuid(minecraftUuid);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Conta desvinculada com sucesso.",
                            "timestamp", Instant.now()
                    )
            );

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", exception.getMessage()
                    ));
        }
    }

    public record CreateLinkRequest(

            @NotBlank
            @Size(max = 36)
            String minecraftUuid,

            @NotBlank
            @Size(max = 16)
            String minecraftName,

            int expirationMinutes
    ) {

        public CreateLinkRequest {

            if (expirationMinutes <= 0) {
                expirationMinutes = 5;
            }

            if (expirationMinutes > 60) {
                expirationMinutes = 60;
            }
        }
    }

    public record ConfirmLinkRequest(

            @NotBlank
            @Size(min = 9, max = 9)
            String code,

            @NotBlank
            @Size(max = 32)
            String discordId
    ) {
    }
}
