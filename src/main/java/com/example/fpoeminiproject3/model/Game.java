package com.example.fpoeminiproject3.model;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the game logic and notifies observers of state changes through the GameObserver interface.
 */
public class Game {
    private final CardDeck deck;
    private final HumanPlayer humanPlayer;
    private final MachinePlayer aiPlayer;
    private Player currentPlayer;
    private boolean reversed = false;
    private CardColor currentColor;
    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Initializes a new UNO game with a human player and machine opponent.
     * @param playerName The name of the human player
     */
    public Game(String playerName) {
        this.deck = new CardDeck();
        this.humanPlayer = new HumanPlayer(playerName);
        this.aiPlayer = new MachinePlayer("Computer");
        humanPlayer.setGame(this);
        aiPlayer.setGame(this);
        dealInitialCards();
        startGame();
    }

    /**
     * Deals initial cards to players and sets up the first discard card.
     */

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

    /**
     * Starts the game by setting the human player as the first player and notifying observers.
     */
    private void startGame() {
        currentPlayer = humanPlayer;
        notifyTurnStarted();
    }

    /**
     * Registers an observer to receive game state notifications.
     * @param observer The observer to add
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers that a new turn has started.
     */
    private void notifyTurnStarted() {
        for (GameObserver observer : observers) {
            observer.onTurnStarted(currentPlayer);
        }
    }

    /**
     * Notifies all observers that a card was played.
     * @param player The player who played the card
     * @param card The card that was played
     */
    private void notifyCardPlayed(Player player, Card card) {
        for (GameObserver observer : observers) {
            observer.onCardPlayed(player, card);
        }
    }

    /**
     * Notifies all observers that a card was drawn.
     * @param player The player who drew the card
     * @param card The card that was drawn
     */
    private void notifyCardDrawn(Player player, Card card) {
        for (GameObserver observer : observers) {
            observer.onCardDrawn(player, card);
        }
    }

    /**
     * Notifies all observers that UNO was called.
     * @param player The player who called UNO
     */
    void notifyUnoCalled(Player player) {
        for (GameObserver observer : observers) {
            observer.onUnoCalled(player);
        }
    }

    /**
     * Notifies all observers that the game is over.
     * @param winner The winning player
     */
    private void notifyGameOver(Player winner) {
        for (GameObserver observer : observers) {
            observer.onGameOver(winner);
        }
    }

    /**
     * Notifies all observers of a color selection after playing a wild card.
     * @param player The player who selected the color
     * @param color The selected color
     */
    private void notifyColorSelected(Player player, CardColor color) {
        for (GameObserver observer : observers) {
            observer.onColorSelected(player, color);
        }
    }

    /**
     * Attempts to play a card for the current player.
     * @param player The player attempting to play
     * @param card The card to play
     * @throws GameException If it's not the player's turn or the move is invalid
     */
    public void playCard(Player player, Card card) throws GameException {
        if (player != currentPlayer) {
            throw new GameException("It's not your turn.");
        }

        Card topCard = deck.getTopDiscardCard();

        if (card.getColor() != CardColor.WILD) {
            if (topCard.getColor() == CardColor.WILD) {
                if (card.getColor() != currentColor) {
                    throw new GameException("You should put a card of the color " + currentColor);
                }
            } else if (!card.similarCard(topCard)) {
                throw new GameException("You can't put that card");
            }
        }

        player.removeCard(card);
        deck.discardCard(card);

        if (card.getColor() == CardColor.WILD) {
            CardColor selectedColor;
            if (player instanceof MachinePlayer) {
                selectedColor = ((MachinePlayer)player).chooseColor();
                showTemporaryMessage(player.getName() + " chooses the color: " + selectedColor, 3);
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

    /**
     * Displays a temporary message to observers.
     *
     * @param message The message to display
     * @param time The duration in seconds to display the message
     */
    private void showTemporaryMessage(String message, int time) {

    }

    /**
     * Handles special card effects
     * @param card The special card that was played
     */
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

    /**
     * Draws a card for the current player.
     * @param player The player drawing the card
     * @throws GameException If it's not the player's turn
     */
    public void drawCard(Player player) throws GameException {
        if (player != currentPlayer) {
            throw new GameException("It's not your turn.");
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

    /**
     * Advances the game to the next player's turn.
     * Automatically triggers machine turn if next player is computer.
     */
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

    /**
     * Gets the next player in turn order.
     * @return The next player
     */
    private Player getNextPlayer() {
        return (currentPlayer == humanPlayer) ? aiPlayer : humanPlayer;
    }

    /**
     * Registers that a player has called UNO.
     * @param player The player who called UNO
     */
    public void callUno(Player player) {
        player.callUno();
        notifyUnoCalled(player);
    }

    /**
     * Checks and applies UNO penalty if player didn't call UNO with one card.
     * @param player The player to check
     */
    public void checkUnoPenalty(Player player) {
        Platform.runLater(() -> {
            if (player.getDeckSize() == 1 && !player.calledUno()) {
                try {
                    player.addCard(deck.drawCard());
                    notifyCardDrawn(player, null);
                    showTemporaryMessage(
                            player.getName() + " doesn't said UNO! +1 card",
                            2
                    );
                } catch (Exception e) {
                    System.err.println("Error when applying penalty UNO: " + e.getMessage());
                }
            }

            if (player instanceof HumanPlayer) {
                ((HumanPlayer)player).resetUnoState();
            }
        });
    }

    /**
     * Gets the top card of the discard pile.
     * @return The top discard card
     */
    public Card getTopDiscardCard() {
        return deck.getTopDiscardCard();
    }

    /**
     * Gets the human player instance.
     * @return The human player
     */
    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    /**
     * Gets the AI player instance.
     * @return The machine player
     */
    public MachinePlayer getMachinePlayer() {
        return aiPlayer;
    }

    /**
     * Gets the current player whose turn it is.
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the current active color
     * @return The current color
     */
    public CardColor getCurrentColor() {
        return currentColor;
    }
}