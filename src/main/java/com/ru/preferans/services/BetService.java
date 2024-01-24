package com.ru.preferans.services;

import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.entities.bet.BetSuit;
import com.ru.preferans.entities.bet.BetType;
import com.ru.preferans.repositories.BetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BetService {

    private static final String NOT_FOUND_MESSAGE = "Bet with type '%s', suit '%s', value '%s' not found";

    private final BetRepository repository;

    public Bet save(Bet bet) {
        return repository.save(bet);
    }

    public Bet get(BetType type, BetSuit suit, int value) {
        List<Bet> bets = repository.findAll();
        for (Bet bet : bets) {
            if (bet.getType() == type
                    && bet.getValue() == value
                    && bet.getSuit() == suit) {
                return bet;
            }
        }
        return null;
    }

    public void loadAll() {
        save(Bet.builder().type(BetType.PASS).build());
        save(Bet.builder().type(BetType.MIZER).build());
        for (int i = 7; i <= 10; i++) {
            for (BetSuit suit : BetSuit.values()) {
                save(Bet.builder().type(BetType.VALUE).suit(suit).value((short) i).build());
            }
        }

    }

    private EntityNotFoundException getNotFoundExc(BetType type, BetSuit suit, short value) {
        return new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, type, suit, value));
    }

}
