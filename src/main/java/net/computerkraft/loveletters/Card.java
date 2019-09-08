package net.computerkraft.loveletters;

import java.util.Optional;
import java.util.stream.Stream;

public enum Card {

    NONE(0),

    UNKNOWN(0),

    KNOWN(0),

    GUARD(1),

    PRIEST(2),

    BARON(3),

    HANDMAID(4),

    PRINCE(5),

    KING(6),

    COUNTESS(7),

    PRINCESS(8);

    int value;

    private Card(int newValue) {
        value = newValue;
    }

    boolean beats(Card card) {
        return this.getValue() > card.getValue();
    }

    private int getValue() {
        return value;
    }

    public boolean requiresPlayerNomination() {
        return this == Card.GUARD || this == Card.PRIEST || this == Card.BARON || this == Card.PRINCE
                || this == Card.KING;
    }

    public static Optional<Card> fromValue(int value) {
        return Stream.of(Card.values())
                .filter(item -> item.getValue() == value)
                .findFirst();
    }

    public boolean isNominatableByGuard() {
        return this.getValue() > Card.GUARD.getValue();
    }
}
