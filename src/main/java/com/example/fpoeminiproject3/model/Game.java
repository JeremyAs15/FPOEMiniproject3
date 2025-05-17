package com.example.fpoeminiproject3.model;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final CardDeck deck;
    private final HumanPlayer humanPlayer;
    private final MachinePlayer aiPlayer;
    private Player currentPlayer;
    private boolean reversed = false;
    private CardColor currentColor;
    private final List<GameObserver> observers = new ArrayList<>();

    public Game(String playerName) {
        this.deck = new CardDeck();
        this.humanPlayer = new HumanPlayer(playerName);
        this.aiPlayer = new MachinePlayer("Computadora");
        humanPlayer.setGame(this);
        aiPlayer.setGame(this);
        dealInitialCards();
        startGame();
    }

    private void dealInitialCards() {
        humanPlayer.addCards(deck.drawCards(5));
        aiPlayer.addCards(deck.drawCards(5));

        Card firstCard;
        do {
            firstCard = deck.drawCard();
        } while (firstCard.specialCard());

        deck.discardCard(firstCard);
        currentColor = firstCard.getColor();
    }

    private void startGame() {
        currentPlayer = humanPlayer;
        notifyTurnStarted();
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    private void notifyTurnStarted() {
        for (GameObserver observer : observers) {
            observer.onTurnStarted(currentPlayer);
        }
    }

    private void notifyCardPlayed(Player player, Card card) {
        for (GameObserver observer : observers) {
            observer.onCardPlayed(player, card);
        }
    }

    private void notifyCardDrawn(Player player, Card card) {
        for (GameObserver observer : observers) {
            observer.onCardDrawn(player, card);
        }
    }

    void notifyUnoCalled(Player player) {
        for (GameObserver observer : observers) {
            observer.onUnoCalled(player);
        }
    }

    private void notifyGameOver(Player winner) {
        for (GameObserver observer : observers) {
            observer.onGameOver(winner);
        }
    }

    private void notifyColorSelected(Player player, CardColor color) {
        for (GameObserver observer : observers) {
            observer.onColorSelected(player, color);
        }
    }

    public void playCard(Player player, Card card) throws GameException {
        if (player != currentPlayer) {
            throw new GameException("No es tu turno");
        }

        Card topCard = deck.getTopDiscardCard();

        if (card.getColor() != CardColor.WILD) {
            if (topCard.getColor() == CardColor.WILD) {
                if (card.getColor() != currentColor) {
                    throw new GameException("Debes jugar una carta de color " + currentColor);
                }
            } else if (!card.similarCard(topCard)) {
                throw new GameException("No puedes jugar esa carta");
            }
        }

        player.removeCard(card);
        deck.discardCard(card);

        if (card.getColor() == CardColor.WILD) {
            CardColor selectedColor;
            if (player instanceof MachinePlayer) {
                selectedColor = ((MachinePlayer)player).chooseColor();
                showTemporaryMessage(player.getName() + " eligió color: " + selectedColor, 3);
            } else {
                selectedColor = ((HumanPlayer)player).getSelectedCardColor();
            }
            currentColor = selectedColor;
            notifyColorSelected(player, selectedColor);
        } else {
            currentColor = card.getColor();
        }

        handleSpecialCard(card);
        notifyCardPlayed(player, card);

        if (player.getDeckSize() == 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            notifyGameOver(player);
            return;
        }

        switchTurn();
    }

    private void showTemporaryMessage(String s, int i) {

    }

    private void handleSpecialCard(Card card) {
        switch (card.getType()) {
            case SKIP:
                switchTurn();
                break;
            case REVERSE:
                switchTurn();
                break;
            case DRAW_TWO:
                Player nextPlayer = getNextPlayer();
                nextPlayer.addCards(deck.drawCards(2));
                notifyCardDrawn(nextPlayer, null);
                switchTurn();
                break;
            case WILD_DRAW_FOUR:
                Player next = getNextPlayer();
                next.addCards(deck.drawCards(4));
                notifyCardDrawn(next, null);
                switchTurn();
                break;
        }
    }

    public void drawCard(Player player) throws GameException {
        if (player != currentPlayer) {
            throw new GameException("No es tu turno");
        }

        Card drawnCard = deck.drawCard();
        player.addCard(drawnCard);

        if (player instanceof MachinePlayer && drawnCard.similarCard(deck.getTopDiscardCard())) {
            playCard(player, drawnCard);
            return;
        }

        switchTurn();
        notifyCardDrawn(player, drawnCard);
    }

    private void switchTurn() {
        currentPlayer = getNextPlayer();
        notifyTurnStarted();

        if (currentPlayer instanceof MachinePlayer) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (currentPlayer instanceof MachinePlayer) {
                    currentPlayer.playTurn();
                }
            }).start();
        }
    }

    private Player getNextPlayer() {
        return (currentPlayer == humanPlayer) ? aiPlayer : humanPlayer;
    }

    public void callUno(Player player) {
        player.callUno();
        notifyUnoCalled(player);
    }

    public void checkUnoPenalty(Player player) {
        Platform.runLater(() -> {
            if (player.getDeckSize() == 1 && !player.calledUno()) {
                try {
                    player.addCard(deck.drawCard());
                    notifyCardDrawn(player, null);
                    showTemporaryMessage(
                            player.getName() + " no dijo UNO! +1 carta",
                            2
                    );
                } catch (Exception e) {
                    System.err.println("Error al aplicar penalización UNO: " + e.getMessage());
                }
            }

            if (player instanceof HumanPlayer) {
                ((HumanPlayer)player).resetUnoState();
            }
        });
    }

    public Card getTopDiscardCard() {
        return deck.getTopDiscardCard();
    }

    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    public MachinePlayer getMachinePlayer() {
        return aiPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public CardColor getCurrentColor() {
        return currentColor;
    }
}