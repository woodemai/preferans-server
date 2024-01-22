package com.ru.preferans.services;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.entities.game.GameInfo;
import com.ru.preferans.entities.game.GameState;
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
        var game = Game.builder()
                .state(GameState.CREATED)
                .players(new LinkedHashSet<>())
                .rounds(new ArrayList<>())
                .build();
        return save(game);
    }

    public void start(String id) {
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
        game.setState(GameState.STARTED);
        repository.save(game);
    }

    private Game save(Game game) {
        return repository.save(game);
    }

    public Game getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> getNotFoundExc(id));
    }

    public GameDto convertToDto(Game game) {
        long size = playerService.getGamePlayersQuantity(game.getId());
        return GameDto.builder()
                .id(game.getId())
                .state(game.getState().toString())
                .size((short) size)
                .build();
    }

    private EntityNotFoundException getNotFoundExc(String id) {
        return new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, id));
    }

    public List<Game> getAll() {
        return repository.findAll();
    }

    public GameInfo getInfo(String id) {
        List<UserDto> users = playerService.getDtos(id);
        GameDto game = convertToDto(getById(id));
        return GameInfo.builder()
                .users(users)
                .game(game)
                .build();
    }
}
