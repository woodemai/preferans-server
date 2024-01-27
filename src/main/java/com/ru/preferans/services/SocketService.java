package com.ru.preferans.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.entities.bet.BetType;
import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameInfo;
import com.ru.preferans.entities.game.GameState;
import com.ru.preferans.entities.user.User;
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
    private final CardService cardService;

    private void sendGameEvent(SocketIOClient senderClient, UUID gameId, EventType eventType, Object... objects) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(gameId.toString()).getClients()
        ) {
            client.sendEvent(eventType.getType(), objects);
        }
    }

    public void connectPlayer(SocketIOClient senderClient, UUID gameId, UUID playerId) {
        Game game = gameService.getById(gameId);
        User player = playerService.getById(playerId);
        playerService.connect(player, game);
        sendGameEvent(senderClient, gameId, EventType.INFO, buildGameInfo(game));
        gameService.save(game);
        playerService.save(player);
    }

    public void disconnectPlayer(SocketIOClient senderClient, UUID gameId, UUID playerId) {
        playerService.disconnect(playerId);
        gameService.deleteIfNoPlayers(gameId);
        sendGameEvent(senderClient, gameId, EventType.INFO, gameService.getInfo(gameId));
    }

    public void switchReady(SocketIOClient client, UUID gameId, UUID playerId) {
        sendGameEvent(client, gameId, EventType.HANDLE_READY, playerId);
        playerService.switchReady(playerId);
        handleGameStart(client, gameId);
    }

    private void handleGameStart(SocketIOClient client, UUID gameId) {
        if (playerService.checkAllReady(gameId)) {
            sendGameEvent(client, gameId, EventType.ALL_READY);
            Game game = gameService.getById(gameId);
            List<Card> cards = cardService.getShuffleDeck();
            gameService.start(game, cards);
            sendGameEvent(client, gameId, EventType.INFO, buildGameInfo(game));
            playerService.saveAll(game.getPlayers());
            gameService.save(game);
        }
    }

    private GameInfo buildGameInfo(Game game) {
        return GameInfo.builder()
                .game(gameService.convertToDto(game))
                .users(playerService.convertListToDto(game.getPlayers()))
                .build();
    }

    public void handleBet(SocketIOClient client, UUID gameId, UUID playerId, Bet bet) {
        sendGameEvent(client, gameId, EventType.NEXT_TURN);
        Game game = gameService.getById(gameId);
        boolean allBet = playerService.setBet(
               bet,
                game.getPlayers(),
                playerId);
        if (allBet) {
            int passed = playerService.handleAllPassed(game.getPlayers());
            if (passed == 3) {
                sendGameEvent(client, gameId, EventType.MOVE_PURCHASE);
                sendGameEvent(client, gameId, EventType.HANDLE_STATE, GameState.GAMEPLAY);
                gameService.nextTurn(game);
                gameService.setState(GameState.GAMEPLAY, game.getCurrentPlayerIndex(), game);
            } else if (passed == 2) {
                for (User player : game.getPlayers()) {
                    if (player.getBet().getType() != BetType.PASS) {
                        gameService.movePurchaseToPlayer(game, playerId);
                        sendGameEvent(client, gameId, EventType.HANDLE_WIN, player.getId());
                        sendGameEvent(client, gameId, EventType.HANDLE_STATE, GameState.DROPPING);
                        gameService.setState(GameState.DROPPING, (short) ((game.getCurrentPlayerIndex() + 2) % 3), game);
                        break;
                    }
                }
            } else {
                gameService.nextTurn(game);
                // todo:
            }
        }
        gameService.save(game);
    }

    public void handleCard(SocketIOClient client, UUID gameId, UUID playerId, Card card) {
        sendGameEvent(client, gameId, EventType.MOVE, gameService.getMoveInfo(playerId, card));
        Game game = gameService.getById(gameId);
        gameService.handleBribeWinner(game, playerId, card);
        playerService.removeCard(game.getPlayers(),playerId, card);
        if (playerService.allMoved(game.getPlayers())) {
            sendGameEvent(client, gameId, EventType.BRIBE_END, game.getBribeWinnerId());
            gameService.handleBribeEnd(game);
            if (gameService.handleRoundEnd(game.getPlayers())) {
                List<Card> cards = cardService.getShuffleDeck();
                gameService.start(game, cards);
                sendGameEvent(client, gameId, EventType.INFO, buildGameInfo(game));
            }
        } else {
            gameService.addCard(gameId, card);
            gameService.nextTurn(game);
        }
    }

    public void handleDrop(SocketIOClient client, UUID gameId, UUID playerId, Card card) {
        sendGameEvent(client, gameId, EventType.DROP, playerId, card);
        int cardQuantity = playerService.handleDropCard(playerId, card);
        if (cardQuantity == 10) {
            sendGameEvent(client, gameId, EventType.HANDLE_STATE, GameState.GAMEPLAY);
        }
    }
}