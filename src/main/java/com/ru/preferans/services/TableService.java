package com.ru.preferans.services;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.table.Table;
import com.ru.preferans.entities.table.TableState;
import com.ru.preferans.repositories.TableRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository repository;

    public Table create(List<Card> cards) {
        var table = Table.builder()
                .cards(cards)
                .state(TableState.NOT_STARTED)
                .rounds(new ArrayList<>())
                .currentPlayersQuantity(0)
                .maxPlayersQuantity(3)
                .game(null)
                .build();
        return repository.save(table);
    }

    public Table setGame(String tableId, Game game) {
        Table table = getTable(tableId);
        table.setGame(game);
        return repository.save(table);
    }

    private Table getTable(String tableId) {
        return repository.findById(tableId)
                .orElseThrow(() -> getNotFound(tableId));
    }

    private EntityNotFoundException getNotFound(String tableId) {
        return new EntityNotFoundException("Table with id " + tableId + " was not found");
    }
}
