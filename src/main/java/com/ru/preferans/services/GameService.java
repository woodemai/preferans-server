package com.ru.preferans.services;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.entities.game.GameState;
import com.ru.preferans.entities.player.Player;
import com.ru.preferans.entities.round.Round;
import com.ru.preferans.repositories.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository repository;

    public Game createGame(Player player) {
        List<Player> players = new ArrayList<>();
        players.add(player);
        var game = Game.builder()
                .startedTime(LocalDateTime.now())
                .endedTime(LocalDateTime.now())
                .state(GameState.CREATED)
                .players(players)
                .rounds(new ArrayList<>())
                        .build();
        return repository.save(game);
    }
    public Game connectPlayer(Player player, String gameId) {
        Game game = getGame(gameId);
        List<Player> players = game.getPlayers();
        players.add(player);
        game.setPlayers(players);
        return saveGame(game);
    }

    public Game disconnectPlayer(Player player, String gameId) {
        Game game = getGame(gameId);
        List<Player> players = game.getPlayers();
        players.remove(player);
        game.setPlayers(players);
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

    private Game getGame(String gameId) {
        return repository.findById(gameId)
                .orElseThrow(() -> gameNotFound(gameId));
    }
    public GameDto convertToDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .startedTime(game.getStartedTime().toString())
                .endedTime(game.getEndedTime().toString())
                .state(game.getState().toString())
                .playerIds(game.getPlayers().stream().map(Player::getId).toList())
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
}
