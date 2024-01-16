package com.ru.preferans.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.ru.preferans.services.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule {

    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.socketService = socketService;
        server.addConnectListener(this.onConnected());
        server.addDisconnectListener(this.onDisconnected());
    }


    private ConnectListener onConnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String gameId = String.join("", params.get("gameId"));
            String playerId = String.join("", params.get("playerId"));
            client.joinRoom(gameId);
            socketService.connectPlayer(client, gameId, playerId);
            log.info("Socket ID[{}] - room[{}]", client.getSessionId().toString(), gameId);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String gameId = String.join("", params.get("gameId"));
            String playerId = String.join("", params.get("playerId"));
            client.leaveRoom(gameId);
            socketService.disconnectPlayer(client, gameId, playerId);
            log.info("Socket ID[{}] - room[{}] disconnected to chat module through", client.getSessionId().toString(), gameId);
        };
    }
}
