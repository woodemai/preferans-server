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
            List<Card> userCards = cards.subList(start, end);
            player.setCards(userCards);
            playerService.save(player);
            start += 10;
            end += 10;
        }
        game.setTableCards(cards.subList(30, 32));
        game.setState(GameState.TRADING);
        repository.save(game);
    }

    private short getNextPlayerIndex(short currentIndex) {
        if (currentIndex < 3 - 1) {
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
                .cards(cardService.convertListToDto(game.getTableCards()))
                .currentPlayerIndex(game.getCurrentPlayerIndex())
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

    public void setState(GameState state, UUID gameId) {
        repository.updateStateAndCurrentPlayerIndexById(state, (short) 0,gameId);
    }

    public boolean allBet(UUID gameId) {
        return repository.existsByIdAndCurrentPlayerIndex(gameId, (short) 2);
    }

    public void addCard(UUID gameId, Card card) {
        Game game = getById(gameId);
        List<Card> cards = game.getTableCards();
        cards.add(card);
        game.setTableCards(cards);
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
}
