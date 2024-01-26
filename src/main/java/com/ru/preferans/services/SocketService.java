package com.ru.preferans.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.entities.bet.BetType;
import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameState;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.socket.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocketService {


    private final PlayerService playerService;
    private final GameService gameService;
    private final BetService betService;

    private void sendGameEvent(SocketIOClient senderClient, UUID gameId, EventType eventType, Object... objects) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId.toString()).getClients()
        ) {
            client.sendEvent(eventType.getType(), objects);
        }
    }

    public void connectPlayer(SocketIOClient senderClient, UUID gameId, UUID playerId) {
        Game game = gameService.getById(gameId);
        playerService.connect(playerId, game);
        sendGameEvent(senderClient, gameId, EventType.INFO, gameService.getInfo(gameId));
    }

    public void disconnectPlayer(SocketIOClient senderClient, UUID gameId, UUID playerId) {
        playerService.disconnect(playerId);
        gameService.deleteIfNoPlayers(gameId);
        sendGameEvent(senderClient, gameId, EventType.INFO, gameService.getInfo(gameId));
    }

    public void switchReady(SocketIOClient client, UUID gameId, UUID playerId) {
        sendGameEvent(client, gameId, EventType.HANDLE_READY, playerId);
        playerService.switchReady(playerId);
        handleAllReady(client, gameId);
    }

    private void handleAllReady(SocketIOClient client, UUID gameId) {
        if (playerService.checkAllReady(gameId)) {
            sendGameEvent(client, gameId, EventType.ALL_READY);
            gameService.start(gameId);
            sendGameEvent(client, gameId, EventType.INFO, gameService.getInfo(gameId));
        }
    }

    public void handleBet(SocketIOClient client, UUID gameId, UUID playerId, Bet bet) {
        sendGameEvent(client, gameId, EventType.NEXT_TURN);
        Game game = gameService.getById(gameId);
        gameService.nextTurn(game);
        playerService.setBet(betService.get(bet.getType(), bet.getSuit(), bet.getValue()), playerId);
        boolean allBet = playerService.allBet(gameId);
        if (allBet) {
            List<User> players = playerService.getPlayers(gameId);
            int passed = playerService.handleAllPassed(players);
            if (passed == 3) {
                sendGameEvent(client, gameId, EventType.MOVE_PURCHASE);
                sendGameEvent(client, gameId, EventType.HANDLE_STATE, GameState.GAMEPLAY);
                gameService.setState(GameState.GAMEPLAY, game);
            } else if (passed == 2) {
                for (User player : players) {
                    if (player.getBet().getType() != BetType.PASS) {
                        gameService.movePurchaseToPlayer(game, playerId);
                        sendGameEvent(client, gameId, EventType.HANDLE_WIN, player.getId());
                        sendGameEvent(client, gameId, EventType.HANDLE_STATE, GameState.GAMEPLAY);
                        gameService.setState(GameState.GAMEPLAY, game);
                    }
                }
            } else {
                // todo:
            }
        }
    }

    public void handleCard(SocketIOClient client, UUID gameId, UUID playerId, Card card) {
        sendGameEvent(client, gameId, EventType.MOVE, gameService.getMoveInfo(playerId, card));
        Game game = gameService.getById(gameId);
        gameService.handleBribeWinner(game, playerId, card);
        playerService.removeCard(playerId, card);
        List<User> players = playerService.getPlayers(gameId);
        if (playerService.allMoved(players)) {
            sendGameEvent(client, gameId, EventType.BRIBE_END, game.getBribeWinnerId());
            gameService.handleBribeEnd(game);
            if (gameService.handleRoundEnd(players)) {
                gameService.start(gameId);
                sendGameEvent(client, gameId, EventType.INFO, gameService.getInfo(gameId));
            }
        } else {
            gameService.addCard(gameId, card);
            gameService.nextTurn(game);
        }
    }

    public void dropCard(SocketIOClient client, UUID gameId, UUID playerId, UserDto userDto) {
        playerService.handleDropCard(userDto);
        sendGameEvent(client, gameId, EventType.INFO, gameService.getInfo(gameId));
    }
}