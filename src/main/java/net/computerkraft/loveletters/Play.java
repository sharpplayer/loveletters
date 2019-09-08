package net.computerkraft.loveletters;

public class Play {

    private Card card;

    private int byPlayer;

    private int toPlayer;

    private Card revealedCard;

    public Play(Card newCard, int newByPlayer, int newToPlayer, Card newRevealedCard) {
        card = newCard;
        byPlayer = newByPlayer;
        toPlayer = newToPlayer;
        revealedCard = newRevealedCard;
    }

    public Card getCard() {
        return card;
    }

    public int getToPlayer() {
        return toPlayer;
    }

    public Card getRevealedCard() {
        return revealedCard;
    }

    public int getByPlayer() {
        return byPlayer;
    }

}
