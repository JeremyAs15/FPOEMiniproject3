package com.example.fpoeminiproject3.model;

public interface GameObserver {
    void onTurnStarted(Player player);
    void onCardPlayed(Player player, Card card);
    void onCardDrawn(Player player, Card card);
    void onUnoCalled(Player player);
    void onGameOver(Player winner);
    void onColorSelected(Player player, CardColor color);
}