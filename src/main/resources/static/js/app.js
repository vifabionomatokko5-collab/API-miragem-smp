const API_URL = "/api/v1/server";

async function updateServerStatus() {

    const statusText =
        document.getElementById("server-status");

    const statusDot =
        document.getElementById("status-dot");

    const players =
        document.getElementById("players");

    const maxPlayers =
        document.getElementById("max-players");

    const version =
        document.getElementById("version");

    const serverName =
        document.getElementById("server-name");

    const lastUpdate =
        document.getElementById("last-update");

    try {

        const response = await fetch(API_URL, {
            cache: "no-store"
        });

        if (!response.ok) {
            throw new Error(
                `HTTP ${response.status}`
            );
        }

        const data = await response.json();

        serverName.textContent =
            data.name || "Miragem SMP";

        players.textContent =
            data.players ?? 0;

        maxPlayers.textContent =
            data.maxPlayers ?? 0;

        version.textContent =
            data.version || "-";

        if (data.online) {

            statusText.textContent =
                "Servidor online";

            statusDot.className =
                "status-dot online";

        } else {

            statusText.textContent =
                "Servidor offline";

            statusDot.className =
                "status-dot offline";
        }

        lastUpdate.textContent =
            "Última atualização: " +
            new Date().toLocaleTimeString(
                "pt-BR"
            );

    } catch (error) {

        console.error(
            "Erro ao consultar servidor:",
            error
        );

        statusText.textContent =
            "Não foi possível consultar o servidor";

        statusDot.className =
            "status-dot offline";

        players.textContent = "-";
        maxPlayers.textContent = "-";
        version.textContent = "-";

        lastUpdate.textContent =
            "API temporariamente indisponível";
    }
}

updateServerStatus();

setInterval(
    updateServerStatus,
    30000
);
