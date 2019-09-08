package net.computerkraft.loveletters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

// 1566581330810L C+P

public class Game {

    private Integer maxPoints;

    private Pack pack;

    private List<Integer> points = new ArrayList<>();

    private List<GameView> gameViews = new ArrayList<>();

    private List<Boolean> players = new ArrayList<>();

    private Random random;

    public Game(int numberOfPlayers, int humanPlayers, int newMaxPoints) {
        long seed = 1566581330810L; //System.currentTimeMillis();
        System.out.println("Seed:" + seed);
        random = new Random(seed);
        for (int count = 0; count < numberOfPlayers; count++) {
            players.add(count < humanPlayers);
            points.add(0);
        }
        Collections.shuffle(players, random);
        maxPoints = newMaxPoints;
    }

    public void play() {
        int turn = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (noWinnerYet()) {
                initialiseGame();
                while (moreThanOneInPlay() && !pack.isEmpty()) {
                    GameView viewTurn = gameViews.get(turn);
                    Card card = pack.pickup();
                    Play play;
                    if (viewTurn instanceof HumanGameView) {
                        play = getPlay(viewTurn, card, scanner);
                    } else {
                        play = new Strategy((AIGameView) viewTurn, points, random).pickup(card);
                    }

                    List<Play> reversePlays = gameViews.stream()
                            .map(item -> item.updateForPlay(play, pack, card))
                            .flatMap(Optional::stream)
                            .collect(Collectors.toList());

                    // Suspect this will need to be passed to all game views, but the play will need to be modified so
                    // that
                    // only data known to the player is available, in particular if a king is played on known cards,
                    // they
                    // can be followed
                    reversePlays.forEach(item -> gameViews.get(item.getToPlayer() - 1)
                            .updateForPlay(item, null, item.getCard())); // null pack means a return play

                    if (viewTurn instanceof HumanGameView) {
                        for (int i = 0; i < 50; i++) {
                            System.out.println();
                        }
                    }
                    turn = nextInPlay(turn);
                }

                if (pack.isEmpty()) {
                    // Compare cards of those in play
                } else {
                    // The only one in play is the winner
                    points.set(turn, points.get(turn) + 1);
                    System.out.println("Player " + (turn + 1) + " won!");
                }
            }
        }

    }

    private void initialiseGame() {
        pack = new Pack(random);
        List<Card> initialDiscards = new ArrayList<>();
        if (players.size() == 2) {
            initialDiscards.add(pack.pickup());
            initialDiscards.add(pack.pickup());
            initialDiscards.add(pack.pickup());
        }
        gameViews.clear();
        for (int count = 0; count < players.size(); count++) {
            if (players.get(count)) {
                gameViews.add(new HumanGameView(players.size(), count + 1, pack.pickup(), initialDiscards));
            } else {
                gameViews.add(new AIGameView(players.size(), count + 1, pack.pickup(), initialDiscards));
            }
        }
    }

    private Play getPlay(GameView viewTurn, Card card, Scanner scanner) {
        System.out.println(viewTurn.toString());
        System.out.println("Draw card:" + card);
        int toPlayer = 0;
        Card playedCard;
        Card revealedCard;
        do {
            playedCard = viewTurn.getCard();
            revealedCard = card;
            String input = "";
            while (!"dh".contains(input.toLowerCase()) || input.length() != 1) {
                System.out.print("Play (D)raw or (H)eld:");
                input = scanner.nextLine();
            }
            boolean playDrawn = input.equalsIgnoreCase("d");
            if (playDrawn) {
                playedCard = card;
                revealedCard = viewTurn.getCard();
            }
        } while (!validCombination(playedCard, revealedCard));

        if (playedCard.requiresPlayerNomination()) {
            while (toPlayer == viewTurn.getPlayer() || toPlayer < 1 || toPlayer > viewTurn.getNumberOfPlayers()) {
                System.out.print("Nominate player (to " + viewTurn.getNumberOfPlayers() + "):");
                toPlayer = scanner.nextInt();
            }
        }
        if (playedCard == Card.GUARD) {
            revealedCard = Card.NONE;
            while (!revealedCard.isNominatableByGuard()) {
                System.out.print("Nominate card (2-8):");
                revealedCard = Card.fromValue(scanner.nextInt())
                        .orElse(Card.NONE);
            }
        }
        return new Play(playedCard, viewTurn.getPlayer(), toPlayer, revealedCard);
    }

    private boolean validCombination(Card playedCard, Card revealedCard) {
        return revealedCard != Card.COUNTESS || (playedCard != Card.KING && playedCard != Card.PRINCE);
    }

    private boolean noWinnerYet() {
        return getWinner() == 0;
    }

    private int getWinner() {
        return points.stream()
                .filter(maxPoints::equals)
                .findFirst()
                .orElse(0);
    }

    private int nextInPlay(int turn) {
        for (int count = turn + 1; count < gameViews.size(); count++) {
            if (gameViews.get(count)
                    .isInPlay()) {
                return count;
            }
        }
        for (int count = 0; count < turn - 1; count++) {
            if (gameViews.get(count)
                    .isInPlay()) {
                return count;
            }
        }
        return 0;
    }

    private boolean moreThanOneInPlay() {
        return gameViews.stream()
                .filter(GameView::isInPlay)
                .count() > 1;
    }

    public static void main(String[] args) {
        int bestOf = 13;
        int numberOfPlayers = 2;
        int numberOfHumans = 1;
        new Game(numberOfPlayers, numberOfHumans, (int) Math.ceil(bestOf / (double) numberOfPlayers)).play();
    }
}
