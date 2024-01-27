package com.ru.preferans;

import com.ru.preferans.services.CardService;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final CardService cardService;

    public DataLoader(CardService cardService) {
        this.cardService = cardService;
        loadData();
    }

    private void loadData() {
        cardService.saveAll(cardService.getDeck());
    }
}
