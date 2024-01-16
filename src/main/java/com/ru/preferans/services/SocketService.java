package com.ru.preferans.services;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final PlayerService playerService;
    private final GameService gameService;

    public void sendUsers(SocketIOClient senderClient, String gameId) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId).getClients()
        ) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {

                client.sendEvent("users");
            }
        }
    }



    public void connectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
         playerService.connect(playerId, gameId);
        sendUsers(senderClient, gameId);
    }

    public void disconnectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
         playerService.disconnect(playerId);
        sendUsers(senderClient, gameId);
    }
}
