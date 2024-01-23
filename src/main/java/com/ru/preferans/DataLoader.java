package com.ru.preferans;

import com.ru.preferans.repositories.CardRepository;
import com.ru.preferans.services.CardService;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final CardRepository cardRepository;
    private final CardService cardService;

    public DataLoader(CardRepository cardRepository, CardService cardService) {
        this.cardRepository = cardRepository;
        this.cardService = cardService;
        loadData();
    }

    private void loadData() {
        cardRepository.saveAll(cardService.getDeck());
    }
}
