package com.ru.preferans.services;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private static final String NOT_FOUND_MESSAGE = "User with ID '%s' not found";

    private final UserRepository repository;
    private final CardService cardService;


    public List<UserDto> getDTOs(UUID gameId) {
        List<User> players = repository.findByGame_Id(gameId);
        return players.stream().map(this::convertToDTO).toList();
    }

    public List<User> getPlayers(UUID gameId) {
        return repository.findByGame_Id(gameId);
    }

    public UserDto convertToDTO(User user) {
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .score(user.getScore())
                .ready(user.isReady())
                .build();
        if (user.getCards() != null) {
            dto.setCards(cardService.convertListToDto(user.getCards()));
        }
        return dto;
    }


    public void connect(UUID playerId, Game game) {

        long playersQuantity = repository.countByGame_Id(game.getId());

        if (playersQuantity > 3) return;

        repository.updateGameById(game, playerId);

    }


    public void disconnect(UUID id) {
        repository.updateReadyAndGameById(id);
    }

    public void switchReady(UUID id) {
        User player = getById(id);
        player.setReady(!player.isReady());
        repository.save(player);
    }


    public boolean checkAllReady(UUID gameId) {
        List<User> players = repository.findByGame_Id(gameId);

        if (players.size() != 3) {
            return false;
        }
        for (User user : players) {
            if (!user.isReady()) {
                return false;
            }
        }
        return true;
    }

    private User getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));

    }

    public void save(User player) {
        repository.save(player);
    }

    public long getGamePlayersQuantity(UUID id) {
        return repository.countByGame_Id(id);
    }

}
