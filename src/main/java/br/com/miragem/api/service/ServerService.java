package br.com.miragem.api.service;

import br.com.miragem.api.model.ServerStatus;
import org.springframework.stereotype.Service;

@Service
public class ServerService {

    private ServerStatus status = new ServerStatus(
            false,
            0,
            100,
            "1.21.x"
    );

    public ServerStatus getStatus() {
        return status;
    }

    public void updateStatus(ServerStatus newStatus) {
        this.status = newStatus;
    }
}
