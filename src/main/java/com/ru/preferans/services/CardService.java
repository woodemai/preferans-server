package com.ru.preferans.services;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.card.CardDto;
import com.ru.preferans.entities.card.Rank;
import com.ru.preferans.entities.card.Suit;
import com.ru.preferans.repositories.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CardService {

    private static final String NOT_FOUND_MESSAGE = "Card with ID '%s' not found";

    private final CardRepository cardRepository;


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
        return cardRepository.findAll();
    }

    public List<CardDto> convertListToDto(List<Card> cards) {
        List<CardDto> cardDTOs = new ArrayList<>();
        for (Card card : cards) {
            cardDTOs.add(
                    CardDto.builder()
                            .id(card.getId())
                            .suit(card.getSuit())
                            .rank(card.getRank())
                            .build()
            );
        }
        return cardDTOs;
    }

    public Card getById(UUID cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> getNotFoundExc(cardId));
    }

    private EntityNotFoundException getNotFoundExc(UUID id) {
        return new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, id));
    }
}
