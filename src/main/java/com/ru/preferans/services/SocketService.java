package com.ru.preferans.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final PlayerService playerService;
    private final GameService gameService;

    private void sendGameInfo(SocketIOClient senderClient, String gameId) {
        GameInfo gameInfo = gameService.getInfo(gameId);
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId).getClients()
        ) {
            client.sendEvent("info", gameInfo);
        }
    }


    public void connectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
        Game game = gameService.getById(gameId);
        playerService.connect(playerId, game);
        sendGameInfo(senderClient, gameId);
    }



    public void disconnectPlayer(SocketIOClient senderClient, String gameId, String playerId) {
        playerService.disconnect(playerId);
        sendGameInfo(senderClient, gameId);
    }

    public void switchReady(SocketIOClient client, String gameId, String playerId) {
        playerService.switchReady(playerId);
        sendGameInfo(client, gameId);
        handleAllReady(client, gameId);
    }

    private void handleAllReady(SocketIOClient client, String gameId) {
        if (playerService.checkAllReady(gameId)) {
            gameService.start(gameId);
            sendGameInfo(client, gameId);
        }
    }
}
