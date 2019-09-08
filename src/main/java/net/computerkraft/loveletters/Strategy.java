package net.computerkraft.loveletters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Strategy {

    /**
     * <pre>
     *     PCKPHBPG
     *    PX
     *    CXX
     *    KX X
     *    PX  X
     *    HX   X
     *    BX    X
     *    PX     X
     *    GX      X
     *     PCKPHBPG
     * </pre>
     */

    private AIGameView view;

    private List<Integer> points;

    private int playerNumber;

    private Random random;

    public Strategy(AIGameView gameView, List<Integer> newPoints, Random newRandom) {
        view = gameView;
        points = newPoints;
        random = newRandom;

    }

    public Play pickup(Card drawnCard) {
        if (view.getCard()
                .beats(drawnCard)) {
            return play(view.getCard(), drawnCard);
        } else {
            return play(drawnCard, view.getCard());
        }
    }

    private Play play(Card highCard, Card lowCard) {
        if (highCard == Card.PRINCESS) {
            return playCard(lowCard, highCard);
        } else if (highCard == lowCard) {
            return playCard(lowCard, highCard);
        } else if (highCard == Card.COUNTESS) {
            if (lowCard == Card.KING || lowCard == Card.PRINCE) {
                return playCard(highCard, lowCard);
            } else {
                return playCard(lowCard, highCard);
            }
        } else {
            Card play = chooseCardToPlay(highCard, lowCard);
            if (play == highCard) {
                return playCard(highCard, lowCard);
            } else {
                return playCard(lowCard, highCard);
            }
        }
    }

    private Card chooseCardToPlay(Card highCard, Card lowCard) {
        return lowCard;
    }

    private Play playCard(Card playedCard, Card otherCard) {
        if (playedCard == Card.PRINCESS) {
            return new Play(playedCard, view.getPlayer(), 0, Card.NONE);
        } else if (playedCard == Card.COUNTESS) {
            return new Play(playedCard, view.getPlayer(), 0, Card.NONE);
        } else if (playedCard == Card.KING) {
            // Pick player
            return new Play(playedCard, view.getPlayer(), 0, otherCard);
        } else if (playedCard == Card.PRINCE) {
            // Pick player (including me!)
            return new Play(playedCard, view.getPlayer(), 0, Card.NONE);
        } else if (playedCard == Card.BARON) {
            // Pick player
            return new Play(playedCard, view.getPlayer(), 0, otherCard);
        } else if (playedCard == Card.PRIEST) {
            // Pick player
            return new Play(playedCard, view.getPlayer(), 0, Card.NONE);
        } else {
            return playGuard(playedCard, otherCard);
        }
    }

    private Play playGuard(Card playedCard, Card otherCard) {
        // Go through players to see if we know any card
        // Should we eliminate them if we do know????
        // Check we are not selecting a player that we already know about

        Card guess;
        int player;

        // If all else fails, randomly select
        List<Card> remainingCards = new ArrayList<>(view.getUndiscardedCardsWithoutGuards());
        remainingCards.remove(playedCard);
        remainingCards.remove(otherCard);
        if (remainingCards.isEmpty()) {
            guess = Card.fromValue(new Random().nextInt(7) + 2)
                    .orElse(Card.PRINCESS);
        } else {
            guess = remainingCards.get(random.nextInt(remainingCards.size()));
        }
        player = view.getInPlayPlayers()
                .get(random.nextInt(view.getInPlayPlayers()
                        .size()));

        return new Play(playedCard, view.getPlayer(), player, guess);
    }

}
