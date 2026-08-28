package br.com.miragem.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/server")
public class ServerController {

    @GetMapping
    public Map<String, Object> server() {
        return Map.of(
                "name", "Miragem SMP",
                "online", false,
                "players", 0,
                "maxPlayers", 100,
                "version", "1.21.x",
                "message", "Integração Minecraft será adicionada na próxima etapa."
        );
    }
}
