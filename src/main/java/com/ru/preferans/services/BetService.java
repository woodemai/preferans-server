package com.ru.preferans.services;

import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.repositories.BetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BetService {
    private final BetRepository repository;

    public Bet save(Bet bet) {
        Optional<Bet> optBet = repository.findByTypeAndValueAndSuit(bet.getType(), bet.getValue(), bet.getSuit());
        return optBet.orElseGet(() -> repository.save(bet));
    }
}
