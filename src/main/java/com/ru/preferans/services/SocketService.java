package com.ru.preferans.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.ru.preferans.entities.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final PlayerService playerService;
    private final GameService gameService;

    public void addPlayer(SocketIOClient senderClient, String gameId, UserDto player) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId).getClients()
        ) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {

                client.sendEvent("add_player", player);
            }
        }
    }
    public void removePlayer(SocketIOClient senderClient, String gameId, UserDto player) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId).getClients()
        ) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {

                client.sendEvent("remove_player", player);
            }
        }
    }

    public void connectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
        UserDto user = playerService.connect(playerId, gameId);
        addPlayer(senderClient, gameId, user);
    }

    public void disconnectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
        UserDto user = playerService.disconnect(playerId, gameId);
        removePlayer(senderClient, gameId, user);
    }
}
