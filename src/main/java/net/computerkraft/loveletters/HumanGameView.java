package net.computerkraft.loveletters;

import java.util.ArrayList;
import java.util.List;

public class HumanGameView implements GameView {

    private int playerNumber;

    private Card card;

    private List<Card> initialDiscards;

    private List<List<Card>> playerStacks;

    private boolean inPlay = true;

    public HumanGameView(int numberOfPlayers, int newPlayerNumber, Card pickup, List<Card> initDiscards) {
        initialDiscards = initDiscards;
        playerStacks = new ArrayList<>();
        for (int count = 0; count < numberOfPlayers; count++) {
            playerStacks.add(new ArrayList<>());
        }
        card = pickup;
        playerNumber = newPlayerNumber;
    }

    @Override
    public String toString() {
        StringBuilder stacks = new StringBuilder();
        for (int count = 0; count < playerStacks.size(); count++) {
            stacks.append(" " + (count + 1) + ":" + playerStacks.get(count)
                    .toString());
        }
        return "Player:" + playerNumber + ", Initials:" + initialDiscards.toString() + ", Stacks:" + stacks + ", Card:"
                + card;
    }

    @Override
    public boolean isInPlay() {
        return inPlay;
    }

    @Override
    public void setInPlay(boolean newInPlay) {
        inPlay = newInPlay;
        if (!inPlay) {
            playerStacks.get(playerNumber - 1)
                    .add(card);
            card = Card.NONE;
        }
    }

    @Override
    public Card getCard() {
        return card;
    }

    @Override
    public int getPlayer() {
        return playerNumber;
    }

    @Override
    public void setCard(Card pickup, boolean discardCurrent) {
        if (discardCurrent) {
            playerStacks.get(playerNumber - 1)
                    .add(card);
        }
        card = pickup;
    }

    @Override
    public boolean isSafe() {
        List<Card> stack = playerStacks.get(playerNumber - 1);
        return (!stack.isEmpty() && stack.get(stack.size() - 1) == Card.HANDMAID);
    }

    @Override
    public void updateView(Play play) {
        playerStacks.get(play.getByPlayer() - 1)
                .add(play.getCard());
    }

    @Override
    public int getNumberOfPlayers() {
        return playerStacks.size();
    }
}
