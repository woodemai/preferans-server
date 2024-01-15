package com.ru.preferans.services;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerService {

    private final UserRepository repository;
    private final GameService gameService;
    public List<UserDto> getPlayers(String gameId) {
        List<User> players = repository.findByGame_Id(gameId);
        return players.stream().map(this::convertToDto).toList();
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .score(user.getScore())
                .ready(user.isReady())
                .gameId(user.getGame().getId())
                .build();
    }

    public UserDto disconnect(User user) {
        return convertToDto(repository.save(user));
    }

    public List<UserDto> connect(String playerId, String gameId) {
        Game game = gameService.getGame(gameId);
        User player = repository.findById(playerId).orElseThrow();

        player.setGame(game);
        List<User> players = game.getPlayers();
        players.add(player);
        game.setPlayers(players);
        gameService.save(game);

        repository.save(player);

        return game.getPlayers().stream().map(this::convertToDto).toList();
    }

    public List<UserDto> disconnect(String playerId, String gameId) {
        Game game = gameService.getGame(gameId);
        User player = repository.findById(playerId).orElseThrow();

        player.setGame(null);
        repository.save(player);

        return game.getPlayers().stream().map(this::convertToDto).toList();
    }
}
