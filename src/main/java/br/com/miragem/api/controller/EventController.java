package br.com.miragem.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @PostMapping("/player-join")
    public ResponseEntity<Map<String, Object>> playerJoin(
            @RequestBody Map<String, Object> event
    ) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "event", "player-join",
                "receivedAt", Instant.now().toString(),
                "data", event
        ));
    }

    @PostMapping("/player-quit")
    public ResponseEntity<Map<String, Object>> playerQuit(
            @RequestBody Map<String, Object> event
    ) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "event", "player-quit",
                "receivedAt", Instant.now().toString(),
                "data", event
        ));
    }
}
