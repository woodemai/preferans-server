package com.ru.preferans.services;

import com.ru.preferans.entities.EntityType;
import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.entities.bet.BetType;
import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {


    private final UserRepository repository;
    private final ErrorService errorService;


    public List<UserDto> getDTOs(UUID gameId) {
        List<User> players = repository.findByGame_IdOrderByEmailAsc(gameId);
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
            dto.setCards(user.getCards());
        }
        return dto;
    }


    public void connect(User player, Game game) {
        if (game.getPlayers().size() >= 3) return;
        Set<User> players = game.getPlayers();
        players.add(player);
        game.setPlayers(players);
        player.setGame(game);
    }


    public void disconnect(UUID id) {
        repository.reset(id);
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

    public User getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> errorService.getEntityNotFoundException(EntityType.USER, id));

    }

    public void save(User player) {
        repository.save(player);
    }

    public short getGamePlayersQuantity(UUID id) {
        return repository.countByGame_Id(id);
    }

    public boolean setBet(Bet bet, Set<User> players, UUID playerId) {
        boolean allBet = true;
        for(User player : players) {
            if (player.getId().equals(playerId)) {
                player.setBet(bet);
            } else if (player.getBet() == null) {
                allBet = false;
            }
        }
        return allBet;
    }

    public int handleAllPassed(Set<User> players) {
        int passed = 0;
        for (User player : players) {
            if (player.getBet() != null && player.getBet().getType() == BetType.PASS) {
                passed++;
            }
        }
        return passed;
    }

    public void removeCard(Set<User> players, UUID playerId, Card card) {
        for(User player : players) {
            if(player.getId().equals(playerId)) {
                player.getCards().removeIf(card1 -> card1.getId().equals(card.getId()));
            }
        }
    }

    public boolean allBet(UUID gameId) {
        return !repository.existsByGame_IdAndBetNull(gameId);
    }

    public boolean allMoved(Set<User> players) {
        int cardQuantity = players.stream().toList().getFirst().getCards().size();
        for (User player: players) {
            if (player.getCards().size() != cardQuantity) {
                return false;
            }
        }
        return true;
    }

    public void handleScore(UUID playerId) {

        if (playerId != null) {
            User player = getById(playerId);
            player.setScore(player.getScore() + 1);
            save(player);
        }
    }

    public int handleDropCard(UUID playerId, Card card) {
        User player = getById(playerId);
        Set<Card> cards = player.getCards();
        cards.removeIf(card1 -> card1.getRank() == card.getRank() && card1.getSuit() == card.getSuit());
        save(player);
        return cards.size();
    }

    public void saveAll(Set<User> players) {
        for (User player : players) {
            repository.save(player);
        }
    }

    public List<UserDto> convertListToDto(Set<User> players) {
        return players.stream().map(this::convertToDTO).toList();
    }

    public void setBetById(Bet bet, UUID id) {
        repository.updateBetById(bet, id);
    }
}
