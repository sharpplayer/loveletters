package net.computerkraft.loveletters;

import java.util.Optional;

public interface GameView {

    boolean isInPlay();

    default Optional<Play> updateForPlay(Play play, Pack pack, Card pickup) {
        if (play.getToPlayer() == getPlayer()) {
            if (isSafe()) {
                // Do nothing
            } else if (play.getCard() == Card.GUARD) {
                if (play.getRevealedCard() == getCard()) {
                    setInPlay(false);
                }
            } else if (play.getCard() == Card.PRIEST && pack != null) {
                return Optional.of(new Play(Card.PRIEST, getPlayer(), play.getByPlayer(), getCard()));
            } else if (play.getCard() == Card.BARON) {
                return handleBaron(play);
            } else if (play.getCard() == Card.PRINCE) {
                handlePrince(pack);
            } else if (play.getCard() == Card.KING) {
                Card card = getCard();
                setCard(play.getRevealedCard(), false);
                if (pack != null) {
                    return Optional.of(new Play(Card.KING, getPlayer(), play.getByPlayer(), card));
                }
            }
            updateView(play);
        } else if (play.getByPlayer() == getPlayer()) {
            if (play.getCard() == Card.PRINCESS) {
                setInPlay(false);
            } else if (play.getCard() == getCard()) {
                setCard(pickup, true);
            } else {
                updateView(play);
            }
        }

        return Optional.empty();
    }

    void updateView(Play play);

    default void handlePrince(Pack pack) {
        if (getCard() == Card.PRINCESS) {
            setInPlay(false);
        } else {
            setCard(pack.pickup(), true);
        }
    }

    private Optional<Play> handleBaron(Play play) {
        if (play.getRevealedCard() == getCard()) {
            return Optional.empty();
        } else if (play.getRevealedCard()
                .beats(getCard())) {
            setInPlay(false);
            return Optional.empty();
        } else {
            return Optional.of(new Play(Card.BARON, getPlayer(), play.getByPlayer(), getCard()));
        }
    }

    boolean isSafe();

    void setCard(Card pickup, boolean discardCurrent);

    void setInPlay(boolean b);

    Card getCard();

    int getPlayer();

    int getNumberOfPlayers();

}