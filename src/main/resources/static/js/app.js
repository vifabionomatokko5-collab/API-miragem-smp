const API_URL = "/api/v1/server";

const elements = {
    serverName: document.getElementById("server-name"),
    serverStatus: document.getElementById("server-status"),
    statusDot: document.getElementById("status-dot"),

    players: document.getElementById("players"),
    maxPlayers: document.getElementById("max-players"),
    version: document.getElementById("version"),

    lastUpdate: document.getElementById("last-update"),

    heroStatusDot: document.getElementById("hero-status-dot"),
    heroStatusText: document.getElementById("hero-status-text")
};


function setStatus(state, text) {

    if (elements.statusDot) {
        elements.statusDot.className = `status-dot ${state}`;
    }

    if (elements.heroStatusDot) {
        elements.heroStatusDot.className = `mini-dot ${state}`;
    }

    if (elements.serverStatus) {
        elements.serverStatus.textContent = text;
    }

    if (elements.heroStatusText) {
        elements.heroStatusText.textContent = text;
    }
}


function setText(element, value, fallback = "-") {

    if (!element) {
        return;
    }

    element.textContent =
        value === null ||
        value === undefined ||
        value === ""
            ? fallback
            : value;
}


async function updateServerStatus() {

    setStatus("loading", "Consultando servidor...");

    try {

        const response = await fetch(API_URL, {
            method: "GET",
            cache: "no-store",
            headers: {
                "Accept": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const data = await response.json();

        const online = Boolean(data.online);

        setText(
            elements.serverName,
            data.name,
            "Miragem SMP"
        );

        setText(
            elements.players,
            data.players,
            "0"
        );

        setText(
            elements.maxPlayers,
            data.maxPlayers,
            "0"
        );

        setText(
            elements.version,
            data.version,
            "-"
        );

        if (online) {

            setStatus(
                "online",
                "Servidor online"
            );

        } else {

            setStatus(
                "offline",
                "Servidor offline"
            );
        }

        if (elements.lastUpdate) {

            const now = new Date();

            elements.lastUpdate.textContent =
                "Última atualização: " +
                now.toLocaleTimeString(
                    "pt-BR",
                    {
                        hour: "2-digit",
                        minute: "2-digit",
                        second: "2-digit"
                    }
                );
        }

    } catch (error) {

        console.error(
            "[Miragem] Erro ao consultar servidor:",
            error
        );

        setStatus(
            "offline",
            "Servidor indisponível"
        );

        setText(elements.players, "-", "-");
        setText(elements.maxPlayers, "-", "-");
        setText(elements.version, "-", "-");

        if (elements.lastUpdate) {
            elements.lastUpdate.textContent =
                "API temporariamente indisponível";
        }
    }
}


/*
 * Atualização inicial.
 */
updateServerStatus();


/*
 * Atualiza o status a cada 30 segundos.
 */
setInterval(
    updateServerStatus,
    30000
);


/*
 * Pequena proteção contra erros de navegação.
 */
window.addEventListener(
    "error",
    event => {
        console.error(
            "[Miragem] Erro no site:",
            event.error || event.message
        );
    }
);