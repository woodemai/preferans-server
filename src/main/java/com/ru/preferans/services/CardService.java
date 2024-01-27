package com.ru.preferans.services;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.card.Rank;
import com.ru.preferans.entities.card.Suit;
import com.ru.preferans.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository repository;


    public List<Card> getDeck() {
        List<Card> cards = new ArrayList<>();

        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                cards.add(Card.builder().suit(suit).rank(rank).build());
            }
        }
        return cards;
    }

    public void shuffleDeck(List<Card> cards) {
        Collections.shuffle(cards);
    }

    public List<Card> getShuffleDeck() {
        List<Card> cards = getAll();
        shuffleDeck(cards);
        return cards;
    }

    public List<Card> getAll() {
        return repository.findAll();
    }

    public void saveAll(List<Card> deck) {
        repository.saveAll(deck);
    }
}
