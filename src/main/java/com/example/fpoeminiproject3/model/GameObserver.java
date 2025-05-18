package com.example.fpoeminiproject3.model;

/**
 * Observer interface for receiving game state notifications from the game.
 * Implement this interface to be notified of significant game events and state changes.
 */
public interface GameObserver {
    void onTurnStarted(Player player);
    void onCardPlayed(Player player, Card card);
    void onCardDrawn(Player player, Card card);
    void onUnoCalled(Player player);
    void onGameOver(Player winner);
    void onColorSelected(Player player, CardColor color);
}