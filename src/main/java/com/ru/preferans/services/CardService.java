package com.ru.preferans.services;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.card.Rank;
import com.ru.preferans.entities.card.Suit;
import com.ru.preferans.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository repository;

    public List<Card> createShuffledDeck() {
        List<Card> cards = new ArrayList<>(32);
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                var card = Card.builder()
                        .suit(suit)
                        .rank(rank)
                        .moves(new ArrayList<>())
                        .build();
                cards.add(card);
            }
        }
        List<Card> savedCards = repository.saveAll(cards);
        Collections.shuffle(savedCards);
        return savedCards;
    }
}
