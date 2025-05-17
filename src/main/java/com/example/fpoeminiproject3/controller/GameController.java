package com.example.fpoeminiproject3.controller;

import com.example.fpoeminiproject3.model.*;
import com.example.fpoeminiproject3.view.GameView;
import javafx.application.Platform;

public class GameController implements GameObserver {
    private final Game game;
    private final GameView view;

    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;
        this.game.addObserver(this);
    }

    public void initialize() {
        view.updateCurrentPlayer(game.getCurrentPlayer());
    }

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

    public void handleDrawCard() {
        if (game.getCurrentPlayer() == game.getHumanPlayer()) {
            try {
                game.drawCard(game.getHumanPlayer());
            } catch (GameException e) {
                view.showTemporaryMessage(e.getMessage(), 2);
            }
        }
    }

    public void handleUnoCall() {
        if (game.getHumanPlayer().getDeckSize() == 1) {
            game.callUno(game.getHumanPlayer());
            view.hideUnoButton();
        }
    }

    public void handleUnoTimeout() {
        game.checkUnoPenalty(game.getHumanPlayer());
    }

    public void handleColorSelection(CardColor color) {
        try {
            game.playCard(game.getHumanPlayer(), game.getHumanPlayer().getSelectedCard());
        } catch (GameException e) {
            view.showTemporaryMessage(e.getMessage(), 2);
        }
    }

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

    @Override
    public void onCardPlayed(Player player, Card card) {
        Platform.runLater(() -> {
            view.updatePlayerHand(player);
            view.updateDiscardPile(card);
        });
    }

    @Override
    public void onCardDrawn(Player player, Card card) {
        Platform.runLater(() -> view.updatePlayerHand(player));
    }

    @Override
    public void onUnoCalled(Player player) {
        view.showTemporaryMessage(player.getName() + " ha llamado UNO!", 2);
    }

    @Override
    public void onColorSelected(Player player, CardColor color) {
        view.showTemporaryMessage(player.getName() + " eligió color: " + color, 2);
    }

    @Override
    public void onGameOver(Player winner) {
        view.showGameOver(winner);
    }
}
