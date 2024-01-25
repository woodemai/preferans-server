package com.ru.preferans.services;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.card.CardDto;
import com.ru.preferans.entities.game.*;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.repositories.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameService {

    private static final String NOT_FOUND_MESSAGE = "Game with id '%s' not found";

    private final GameRepository repository;
    private final CardService cardService;
    private final PlayerService playerService;

    public Game create() {
        return save(Game.builder().build());
    }

    public void start(UUID id) {
        List<User> players = playerService.getPlayers(id);
        List<Card> cards = cardService.getShuffleDeck();
        Game game = getById(id);

        int start = 0;
        int end = 10;

        for (User player : players) {
            Set<Card> userCards = new HashSet<>(cards.subList(start, end));
            player.setCards(userCards);
            player.setScore(0);
            playerService.save(player);
            start += 10;
            end += 10;
        }
        game.setPurchase(new HashSet<>(cards.subList(30, 32)));
        game.setState(GameState.TRADING);
        repository.save(game);
    }

    private short getNextPlayerIndex(short currentIndex) {
        if (currentIndex < 2) {
            return ++currentIndex;
        } else {
            return 0;
        }
    }

    private Game save(Game game) {
        return repository.save(game);
    }

    public Game getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> getNotFoundExc(id));
    }

    public GameDto convertToDto(Game game) {
        short size = playerService.getGamePlayersQuantity(game.getId());
        return GameDto.builder()
                .id(game.getId())
                .state(game.getState())
                .size(size)
                .purchase(game.getPurchase())
                .tableDeck(game.getTableDeck())
                .currentPlayerIndex(game.getCurrentPlayerIndex())
                .bribeWinnerCard(game.getBribeWinnerCard())
                .build();
    }

    private EntityNotFoundException getNotFoundExc(UUID id) {
        return new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, id));
    }

    public List<Game> getAll() {
        return repository.findAll();
    }

    public GameInfo getInfo(UUID id) {
        List<UserDto> users = playerService.getDTOs(id);
        GameDto game = convertToDto(getById(id));
        return GameInfo.builder()
                .users(users)
                .game(game)
                .build();
    }

    public void nextTurn(UUID gameId) {
        Game game = getById(gameId);
        game.setCurrentPlayerIndex(getNextPlayerIndex(game.getCurrentPlayerIndex()));
        save(game);
    }

    public void movePurchaseToTable(Game game) {
        if (!game.getPurchase().isEmpty()) {
            Card card = game.getPurchase().stream().toList().getFirst();
            game.getPurchase().remove(card);
            Set<Card> tableDeck = game.getTableDeck();
            tableDeck.add(card);
            game.setBribeWinnerCard(card);
            game.setTableDeck(tableDeck);
        }
    }

    public void setState(GameState state, UUID gameId) {
        Game game = getById(gameId);
        movePurchaseToTable(game);
        game.setState(state);
        game.setCurrentPlayerIndex((short) 0);
        save(game);
    }

    public void addCard(UUID gameId, Card card) {
        Game game = getById(gameId);
        game.getTableDeck().add(card);
        save(game);
    }

    public MoveInfo getMoveInfo(UUID playerId, Card card) {
        return MoveInfo.builder()
                .playerId(playerId.toString())
                .card(CardDto.builder()
                        .id(card.getId())
                        .suit(card.getSuit())
                        .rank(card.getRank())
                        .build())
                .build();
    }

    public void handleBribeEnd(Game game) {
        playerService.handleScore(game.getBribeWinnerId());
        game.getTableDeck().clear();
        game.setBribeWinnerCard(null);
        game.setBribeWinnerId(null);
        movePurchaseToTable(game);
        save(game);
    }

    public void handleBribeWinner(UUID gameId, UUID playerId, Card card) {
        Game game = getById(gameId);
        Card bribeWinnerCard = game.getBribeWinnerCard();
        if (bribeWinnerCard == null || bribeWinnerCard.getSuit() == card.getSuit() && (bribeWinnerCard.getRank().getValue() < card.getRank().getValue())) {
            game.setBribeWinnerCard(card);
            game.setBribeWinnerId(playerId);
        }
        save(game);
    }

    public boolean handleRoundEnd(List<User> players) {
        return players.getFirst().getCards().isEmpty();
    }

    public void deleteIfNoPlayers(UUID gameId) {
        repository.deleteByPlayersEmptyAndId(gameId);
    }
}
