package br.com.miragem.api.controller;

import br.com.miragem.api.model.ServerStatus;
import br.com.miragem.api.service.ServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/server")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping
    public Map<String, Object> server() {
        ServerStatus status = serverService.getStatus();

        return Map.of(
                "name", "Miragem SMP",
                "online", status.online(),
                "players", status.players(),
                "maxPlayers", status.maxPlayers(),
                "version", status.version()
        );
    }

    @PostMapping("/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @RequestBody ServerStatus newStatus
    ) {
        serverService.updateStatus(newStatus);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Status do servidor atualizado.",
                "server", serverService.getStatus()
        ));
    }
}
