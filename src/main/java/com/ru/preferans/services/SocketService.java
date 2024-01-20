package com.ru.preferans.services;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final PlayerService playerService;

    public void sendUsers(SocketIOClient senderClient, String gameId) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId).getClients()
        ) {
            client.sendEvent("users");
        }
    }


    public void connectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
        playerService.connect(playerId, gameId);
        sendAllReady(senderClient, gameId);
        sendUsers(senderClient, gameId);
    }

    public void disconnectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
        playerService.disconnect(playerId);
        sendUsers(senderClient, gameId);
    }

    public void switchReady(SocketIOClient client, String gameId, String playerId) {
        playerService.switchReady(playerId);
        sendUsers(client, gameId);
        sendAllReady(client, gameId);
    }

    public void sendAllReady(SocketIOClient senderClient, String gameId) {
        if (playerService.checkAllReady(gameId)) {
            for (
                    SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId).getClients()
            ) {
                client.sendEvent("all_ready");
            }
        }
    }
}

