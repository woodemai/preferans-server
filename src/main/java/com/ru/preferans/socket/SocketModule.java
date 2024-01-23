package com.ru.preferans.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.services.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class SocketModule {

    private static final String GAME_ID = "gameId";
    private static final String PLAYER_ID = "playerId";

    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.socketService = socketService;
        server.addConnectListener(this.onConnected());
        server.addDisconnectListener(this.onDisconnected());
        server.addEventListener("switch_ready", Game.class, this.onSwitchReady());
        server.addEventListener("send_bet", Bet.class, this.onBet());
        server.addEventListener("send_card", Card.class, this.onCard());

    }


    private ConnectListener onConnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            UUID gameId = getParam(params, GAME_ID);
            UUID playerId = getParam(params, PLAYER_ID);
            client.joinRoom(gameId.toString());
            socketService.connectPlayer(client, gameId, playerId);
            log.info("USER [{}] connected to game [{}]", playerId, gameId);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            UUID gameId = getParam(params, GAME_ID);
            UUID playerId = getParam(params, PLAYER_ID);
            client.leaveRoom(gameId.toString());
            socketService.disconnectPlayer(client, gameId, playerId);
            log.info("USER [{}] disconnected from game [{}]", playerId, gameId);
        };
    }

    private DataListener<Game> onSwitchReady() {
        return (client, game, ackRequest) -> {
            var params = client.getHandshakeData().getUrlParams();
            UUID gameId = getParam(params, GAME_ID);
            UUID playerId = getParam(params, PLAYER_ID);
            socketService.switchReady(client, gameId, playerId);
        };
    }
    private DataListener<Bet> onBet() {
        return (client, bet, ackRequest) -> {
            var params = client.getHandshakeData().getUrlParams();
            UUID gameId = getParam(params, GAME_ID);
            UUID playerId = getParam(params, PLAYER_ID);
            socketService.handleBet(client, gameId, playerId, bet);
        };
    }
    private DataListener<Card> onCard() {
        return (client, card, ackRequest) -> {
            var params = client.getHandshakeData().getUrlParams();
            UUID gameId = getParam(params, GAME_ID);
            UUID playerId = getParam(params, PLAYER_ID);
            socketService.handleCard(client, gameId, playerId, card);
        };
    }


    private UUID getParam(Map<String, List<String>> params, String param) {
        return UUID.fromString(String.join("", params.get(param)));
    }
}
