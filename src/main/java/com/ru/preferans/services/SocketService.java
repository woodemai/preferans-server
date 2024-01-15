package com.ru.preferans.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final PlayerService playerService;

    public void sendUsers(SocketIOClient senderClient, List<UserDto> users, String gameId) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId).getClients()
        ) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("get_users", users);
            }
        }
    }

    public void connectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
        List<UserDto> dtos = playerService.connect(playerId, gameId);
        sendUsers(senderClient, dtos, gameId);
    }
    public void disconnectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
        List<UserDto> dtos = playerService.disconnect(playerId, gameId);
        sendUsers(senderClient, dtos, gameId);
    }
}
