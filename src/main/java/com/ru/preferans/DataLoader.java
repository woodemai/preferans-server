package com.ru.preferans;

import com.ru.preferans.repositories.CardRepository;
import com.ru.preferans.services.BetService;
import com.ru.preferans.services.CardService;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final CardRepository cardRepository;
    private final CardService cardService;
    private final BetService betService;

    public DataLoader(CardRepository cardRepository, CardService cardService, BetService betService) {
        this.cardRepository = cardRepository;
        this.cardService = cardService;
        this.betService = betService;
        loadData();
    }

    private void loadData() {
        cardRepository.saveAll(cardService.getDeck());
        betService.loadAll();
    }
}
