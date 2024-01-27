package com.ru.preferans.services;

import com.ru.preferans.entities.EntityType;
import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.card.CardDto;
import com.ru.preferans.entities.game.*;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {


    private final GameRepository repository;
    private final PlayerService playerService;
    private final ErrorService errorService;

    public Game create() {
        return save(Game.builder().build());
    }

    public void
    start(Game game, List<Card> cards) {
        int start = 0;
        int end = 10;
        for (User player : game.getPlayers()) {
            Set<Card> userCards = new HashSet<>(cards.subList(start, end));
            player.setCards(userCards);
            player.setScore(0);
            start += 10;
            end += 10;
        }
        game.setPurchase(new HashSet<>(cards.subList(30, 32)));
        game.setState(GameState.TRADING);
    }
    public Page<Game> getGames(int pageNumber, int pageSize, Sort sort) {

        return repository.findAll(PageRequest.of(pageNumber, pageSize, sort));
    }

    private short getNextPlayerIndex(short currentIndex) {
        if (currentIndex < 2) {
            return ++currentIndex;
        } else {
            return 0;
        }
    }

    public Game save(Game game) {
        return repository.save(game);
    }

    public Game getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> errorService.getEntityNotFoundException(EntityType.GAME, id));
    }

    public GameDto convertToDto(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .state(game.getState())
                .size((short) game.getPlayers().size())
                .purchase(game.getPurchase())
                .tableDeck(game.getTableDeck())
                .currentPlayerIndex(game.getCurrentPlayerIndex())
                .bribeWinnerCard(game.getBribeWinnerCard())
                .build();
    }

    public GameInfo getInfo(UUID id) {
        List<UserDto> users = playerService.getDTOs(id);
        GameDto game = convertToDto(getById(id));
        return GameInfo.builder()
                .users(users)
                .game(game)
                .build();
    }

    public void nextTurn(Game game) {
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

    public void setState(GameState state, short currentPlayerIndex, Game game) {
        movePurchaseToTable(game);
        game.setState(state);
        game.setCurrentPlayerIndex(currentPlayerIndex);
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

    public void handleBribeWinner(Game game, UUID playerId, Card card) {
        Card bribeWinnerCard = game.getBribeWinnerCard();
        if (bribeWinnerCard == null || bribeWinnerCard.getSuit() == card.getSuit() && (bribeWinnerCard.getRank().getValue() < card.getRank().getValue())) {
            game.setBribeWinnerCard(card);
            game.setBribeWinnerId(playerId);
        }
    }

    public boolean handleRoundEnd(Set<User> players) {
        return players.stream().toList().getFirst().getCards().isEmpty();
    }

    public void deleteIfNoPlayers(UUID gameId) {
        repository.deleteByPlayersEmptyAndId(gameId);
    }

    public void movePurchaseToPlayer(Game game, UUID playerId) {
        for(User player : game.getPlayers()) {
            if(player.getId().equals(playerId)) {
                Set<Card> cards = player.getCards();
                cards.addAll(game.getPurchase());
                game.setPurchase(new HashSet<>());

            }
        }
    }
}
