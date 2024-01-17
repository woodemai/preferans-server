package com.ru.preferans.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.ru.preferans.entities.game.Game;
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
        server.addEventListener("switch_ready", Game.class, this.onSwitchReady());
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

    private DataListener<Game> onSwitchReady() {
        return (client, game, ackRequest) -> {
            var params = client.getHandshakeData().getUrlParams();
            String gameId = String.join("", params.get("gameId"));
            String playerId = String.join("", params.get("playerId"));
            socketService.switchReady(client, gameId, playerId);
        };
    }
}
