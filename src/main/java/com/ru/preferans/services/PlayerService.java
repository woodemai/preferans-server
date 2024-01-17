package com.ru.preferans.services;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final UserRepository repository;
    private final GameService gameService;

    public List<UserDto> getPlayers(String gameId) {
        List<User> players = repository.findByGame_Id(gameId);
        return players.stream().map(this::convertToDto).toList();
    }

    private UserDto convertToDto(User user) {
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .score(user.getScore())
                .ready(user.isReady())
                .build();
        if (user.getGame() != null) {
            dto.setGameId(user.getGame().getId());
        }
        return dto;
    }


    public UserDto connect(String playerId, String gameId) {
        Game game = gameService.getGame(gameId);
        User player = repository.findById(playerId).orElseThrow();
        player.setGame(game);

        return convertToDto(repository.save(player));
    }

    public void disconnect(String playerId) {
        User player = repository.findById(playerId).orElseThrow();
        player.setGame(null);
        repository.save(player);


    }

    public void switchReady(String playerId) {
        User player = repository.findById(playerId).orElseThrow();
        player.setReady(!player.isReady());
        repository.save(player);
    }
}
