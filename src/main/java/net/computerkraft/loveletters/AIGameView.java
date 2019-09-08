package net.computerkraft.loveletters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AIGameView implements GameView {

    private int playerNumber;

    private List<PlayerView> playerViews = new ArrayList<>();

    private List<Card> initialDiscards;

    private boolean inPlay = true;

    public AIGameView(int numberOfPlayers, int newPlayerNumber, Card myCard, List<Card> initDiscards) {
        initialDiscards = initDiscards;
        playerNumber = newPlayerNumber;
        for (int count = 0; count < numberOfPlayers; count++) {
            Card card = myCard;
            if (count + 1 != playerNumber) {
                card = Card.UNKNOWN;
            }
            playerViews.add(new PlayerView(numberOfPlayers, playerNumber, card));
        }
    }

    @Override
    public boolean isInPlay() {
        return inPlay;
    }

    @Override
    public void setInPlay(boolean newInPlay) {
        inPlay = newInPlay;
        if (!inPlay) {
            PlayerView view = playerViews.get(playerNumber - 1);
            view.setDiscardAndNewCard(view.getCard(), Card.NONE);
        }
    }

    @Override
    public Card getCard() {
        return playerViews.get(playerNumber - 1)
                .getCard();
    }

    @Override
    public int getPlayer() {
        return playerNumber;
    }

    @Override
    public boolean isSafe() {
        return playerViews.get(playerNumber - 1)
                .isSafe();
    }

    @Override
    public void setCard(Card pickup, boolean discardCurrent) {
        PlayerView view = playerViews.get(playerNumber - 1);
        if (discardCurrent) {
            view.setDiscardAndNewCard(view.getCard(), pickup);
        } else {
            view.setDiscardAndNewCard(pickup, view.getCard());
        }

    }

    @Override
    public void updateView(Play play) {
        if (play.getByPlayer() != playerNumber) {
            PlayerView view = playerViews.get(play.getByPlayer() - 1);
            view.setDiscardAndNewCard(play.getCard(), Card.UNKNOWN);
        }
    }

    @Override
    public int getNumberOfPlayers() {
        return playerViews.size();
    }

    public List<Card> getUndiscardedCards() {
        List<Card> fullPack = new ArrayList<>(Pack.FULL_PACK);
        for (PlayerView view : playerViews) {
            view.getStack()
                    .stream()
                    .forEach(fullPack::remove);
        }
        initialDiscards.stream()
                .forEach(fullPack::remove);
        return fullPack;
    }

    public List<Card> getUndiscardedCardsWithoutGuards() {
        List<Card> cards = getUndiscardedCards();
        while (cards.contains(Card.GUARD)) {
            cards.remove(Card.GUARD);
        }
        return cards;
    }

    public List<Integer> getInPlayPlayers() {
        return playerViews.stream()
                .filter(PlayerView::isInPlay)
                .filter(player -> player.getPlayerNumber() != playerNumber)
                .map(PlayerView::getPlayerNumber)
                .collect(Collectors.toList());
    }
}
