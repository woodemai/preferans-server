package com.ru.preferans.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final PlayerService playerService;
    private final GameService gameService;

    private void sendGameInfo(SocketIOClient senderClient, UUID gameId) {
        GameInfo gameInfo = gameService.getInfo(gameId);
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId.toString()).getClients()
        ) {
            client.sendEvent("info", gameInfo);
        }
    }
    private void sendAllReady(SocketIOClient senderClient, UUID gameId) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId.toString()).getClients()
        ) {
            client.sendEvent("all_ready");
        }
    }


    public void connectPlayer(SocketIOClient senderClient, UUID gameId, UUID playerId) {
        Game game = gameService.getById(gameId);
        playerService.connect(playerId, game);
        sendGameInfo(senderClient, gameId);
    }



    public void disconnectPlayer(SocketIOClient senderClient, UUID gameId, UUID playerId) {
        playerService.disconnect(playerId);
        sendGameInfo(senderClient, gameId);
    }

    public void switchReady(SocketIOClient client, UUID gameId, UUID playerId) {
        playerService.switchReady(playerId);
        sendGameInfo(client, gameId);
        handleAllReady(client, gameId);
    }

    private void handleAllReady(SocketIOClient client, UUID gameId) {
        if (playerService.checkAllReady(gameId)) {
            sendAllReady(client, gameId);
            gameService.start(gameId);
            sendGameInfo(client, gameId);
        }
    }
}
