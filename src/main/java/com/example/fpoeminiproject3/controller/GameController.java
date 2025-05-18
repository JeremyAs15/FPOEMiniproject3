package com.example.fpoeminiproject3.controller;

import com.example.fpoeminiproject3.model.*;
import com.example.fpoeminiproject3.view.GameView;
import javafx.application.Platform;

/**
 * It implements the GameObserver interface to respond to game state changes.
 */
public class GameController implements GameObserver {
    private final Game game;
    private final GameView view;

    /**
     * Constructs a GameController with the specified game model and view.
     * @param game the game model to control
     * @param view the view to update based on game state changes
     */
    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;
        this.game.addObserver(this);
    }

    /**
     * Initializes the controller and updates the view with the current player.
     */
    public void initialize() {
        view.updateCurrentPlayer(game.getCurrentPlayer());
    }

    /**
     * Handles card selection by the human player.
     * If the card is a WILD card, shows color selection dialog.
     * Otherwise, attempts to play the selected card.
     * @param card the card that was selected by the player
     */
    public void handleCardSelection(Card card) {
        try {
            game.getHumanPlayer().selectCard(card);
            if (game.getCurrentPlayer() == game.getHumanPlayer()) {
                if (card.getColor() == CardColor.WILD) {
                    view.showColorSelectionDialog();
                } else {
                    game.playCard(game.getHumanPlayer(), card);
                }
            }
        } catch (GameException e) {
            view.showTemporaryMessage(e.getMessage(), 2);
        }
    }

    /**
     * Handles the draw card action for the human player.
     */
    public void handleDrawCard() {
        if (game.getCurrentPlayer() == game.getHumanPlayer()) {
            try {
                game.drawCard(game.getHumanPlayer());
            } catch (GameException e) {
                view.showTemporaryMessage(e.getMessage(), 2);
            }
        }
    }

    /**
     * Handles the UNO call action for the human player and hides the UNO button if called successfully.
     */
    public void handleUnoCall() {
        if (game.getHumanPlayer().getDeckSize() == 1) {
            game.callUno(game.getHumanPlayer());
            view.hideUnoButton();
        }
    }

    /**
     * Handles the UNO timeout when the player fails to call UNO in time.
     */
    public void handleUnoTimeout() {
        game.checkUnoPenalty(game.getHumanPlayer());
    }

    /**
     * Handles color selection after a WILD card is played.
     * @param color the selected color for the WILD card
     */
    public void handleColorSelection(CardColor color) {
        try {
            game.playCard(game.getHumanPlayer(), game.getHumanPlayer().getSelectedCard());
        } catch (GameException e) {
            view.showTemporaryMessage(e.getMessage(), 2);
        }
    }

    /**
     * Updates the current player display and checks for UNO conditions.
     * @param player the player whose turn is starting
     */
    @Override
    public void onTurnStarted(Player player) {
        Platform.runLater(() -> {
            view.updateCurrentPlayer(player);
            view.updateDiscardPile(game.getTopDiscardCard());
            view.hideUnoButton();

            if (game.getHumanPlayer().getDeckSize() == 1) {
                view.showUnoButtonWithTimeout(game.getHumanPlayer(), 4);
            } else if (game.getMachinePlayer().getDeckSize() == 1) {
                view.showUnoButtonWithTimeout(game.getMachinePlayer(), 4);
            }
        });
    }

    /**
     * Called when a card is played and updates the player's hand and discard pile display.
     * @param player the player who played the card
     * @param card the card that was played
     */
    @Override
    public void onCardPlayed(Player player, Card card) {
        Platform.runLater(() -> {
            view.updatePlayerHand(player);
            view.updateDiscardPile(card);
        });
    }

    /**
     * Called when a card is drawn and updates the player's hand display.
     * @param player the player who drew the card
     * @param card the card that was drawn
     */
    @Override
    public void onCardDrawn(Player player, Card card) {
        Platform.runLater(() -> view.updatePlayerHand(player));
    }

    /**
     * Called when UNO is called by a player and shows a temporary message indicating who called UNO.
     * @param player the player who called UNO
     */
    @Override
    public void onUnoCalled(Player player) {
        view.showTemporaryMessage(player.getName() + " has pressed UNO!", 2);
    }

    /**
     * Called when a color is selected for a WILD card and shows a temporary message with the selected color.
     * @param player the player who selected the color
     * @param color the color that was selected
     */
    @Override
    public void onColorSelected(Player player, CardColor color) {
        view.showTemporaryMessage(player.getName() + " choose the color: " + color, 2);
    }

    /**
     * Called when the game ends and shows the game over screen with the winner.
     * @param winner the player who won the game
     */
    @Override
    public void onGameOver(Player winner) {
        view.showGameOver(winner);
    }
}