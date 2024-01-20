package com.ru.preferans.services;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.entities.game.GameState;
import com.ru.preferans.entities.round.Round;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.repositories.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private static final String NOT_FOUND_MESSAGE = "Game with id '%s' not found";

    private final GameRepository repository;

    public Game create() {
        var game = Game.builder()
                .state(GameState.CREATED)
                .players(new LinkedHashSet<>())
                .rounds(new ArrayList<>())
                .build();
        return repository.save(game);
    }

    public Game start(String gameId) {
        Game game = getById(gameId);
        game.setState(GameState.STARTED);
        return save(game);
    }

    private Game save(Game game) {
        return repository.save(game);
    }

    public Game getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> getNotFoundExc(id));
    }

    public GameDto convertToDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .state(game.getState().toString())
                .playerIds(game.getPlayers().stream().map(User::getId).toList())
                .roundIds(game.getRounds().stream().map(Round::getId).toList())
                .build();
    }

    public Game end(String gameId) {
        Game game = getById(gameId);
        game.setState(GameState.ENDED);
        return save(game);
    }

    private EntityNotFoundException getNotFoundExc(String id) {
        return new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, id));
    }

    public List<Game> getAll() {
        return repository.findAll();
    }
}
