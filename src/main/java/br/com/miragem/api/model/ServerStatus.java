package br.com.miragem.api.model;

public record ServerStatus(
        boolean online,
        int players,
        int maxPlayers,
        String version
) {
}
