package com.ru.preferans.entities.bet;

import com.ru.preferans.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Bet implements Comparable<Bet>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private BetType type;

    private int value;

    @Enumerated(EnumType.STRING)
    private BetSuit suit;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> user = new ArrayList<>();

    @Override
    public int compareTo(Bet o) {
        if (this.getType() == o.getType()) {
            if (this.getType() == BetType.VALUE) {
                int valueComparison = Integer.compare(this.getValue(), o.getValue());
                if (valueComparison != 0) {
                    return valueComparison;
                }

                return Integer.compare(this.getSuit().getValue(), o.getSuit().getValue());
            } else {
                return 0;
            }
        } else if (this.getType() == BetType.PASS) {
            return -1;
        } else if (this.getType() == BetType.MIZER && o.getType() == BetType.VALUE) {
            return (o.getValue() < 9) ? 1 : -1;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return value == bet.value && Objects.equals(id, bet.id) && type == bet.type && suit == bet.suit && Objects.equals(user, bet.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, value, suit, user);
    }
}
