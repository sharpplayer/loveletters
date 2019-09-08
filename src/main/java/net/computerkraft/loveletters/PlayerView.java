package net.computerkraft.loveletters;

import java.util.ArrayList;
import java.util.List;

public class PlayerView {

    private int playerNumber;

    private List<Card> stack = new ArrayList<>();

    private List<Card> playersCards;

    private boolean inPlay = true;

    public PlayerView(int numberOfPlayers, int newPlayerNumber, Card playerCard) {
        playersCards = new ArrayList<>();
        playerNumber = newPlayerNumber;
        for (int count = 0; count < numberOfPlayers; count++) {
            if (count == newPlayerNumber - 1) {
                playersCards.add(playerCard);
            } else {
                playersCards.add(Card.UNKNOWN);
            }
        }
    }

    public void setDiscardAndNewCard(Card discard, Card newCard) {
        stack.add(discard);
        if (getCard() != discard) {
            setCard(newCard);
        }
    }

    public boolean isInPlay() {
        return inPlay;
    }

    public boolean isSafe() {
        return !stack.isEmpty() && stack.get(stack.size() - 1) == Card.HANDMAID;
    }

    public void setCard(Card newCard) {
        playersCards.set(playerNumber - 1, newCard);
    }

    public Card getCard() {
        return playersCards.get(playerNumber - 1);
    }

    public List<Card> getStack() {
        return stack;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
