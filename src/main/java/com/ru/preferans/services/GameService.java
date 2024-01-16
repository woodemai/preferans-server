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
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository repository;

    public Game create() {
        var game = Game.builder()
                .state(GameState.CREATED)
                .players(new ArrayList<>())
                .rounds(new ArrayList<>())
                .build();
        return repository.save(game);
    }

    public Game connectPlayer(User player, Game game) {
        List<User> players = game.getPlayers();
        if (!players.contains(player)) {
            players.add(player);
            player.setGame(game);
        }
        game.setPlayers(players);
        return saveGame(game);
    }

    public Game disconnectPlayer(User player, Game game) {
        List<User> players = game.getPlayers();
        players.remove(player);
        player.setGame(null);
        game.setPlayers(players);
        if (game.getPlayers().isEmpty()) {
            repository.delete(game);
            return null;
        }
        return saveGame(game);
    }

    public Game startGame(String id) {
        Game game = getGame(id);
        game.setState(GameState.STARTED);
        return saveGame(game);
    }

    private Game saveGame(Game game) {
        return repository.save(game);
    }

    public Game getGame(String gameId) {
        return repository.findById(gameId)
                .orElseThrow(() -> gameNotFound(gameId));
    }

    public GameDto convertToDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .state(game.getState().toString())
                .playerIds(game.getPlayers().stream().map(User::getId).toList())
                .roundIds(game.getRounds().stream().map(Round::getId).toList())
                .build();
    }

    public Game endGame(String gameId) {
        Game game = getGame(gameId);
        game.setState(GameState.ENDED);
        return saveGame(game);
    }

    private EntityNotFoundException gameNotFound(String gameId) {
        return new EntityNotFoundException("Game with id " + gameId + " was not found");
    }

    public List<Game> getAll() {
        return repository.findAll();
    }

    public Game save(Game game) {
        return repository.save(game);
    }
}
