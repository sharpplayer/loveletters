package net.computerkraft.loveletters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Pack {

    public static final List<Card> FULL_PACK = Arrays.asList(Card.GUARD, Card.GUARD, Card.GUARD, Card.GUARD, Card.GUARD,
            Card.PRIEST, Card.PRIEST, Card.BARON, Card.BARON, Card.HANDMAID, Card.HANDMAID, Card.PRINCE, Card.PRINCE,
            Card.KING, Card.COUNTESS, Card.PRINCESS);

    private List<Card> cards;

    public Pack(Random random) {
        cards = new ArrayList<>(FULL_PACK);
        Collections.shuffle(cards, random);
        pickup();
    }

    public Card pickup() {
        if (cards.isEmpty()) {
            // Possible if someone discards a prince when the pack is empty
            return Card.NONE;
        }
        return cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

}
