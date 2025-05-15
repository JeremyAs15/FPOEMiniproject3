package com.example.fpoeminiproject3.model;

public interface Game {
    void onTurnStarted(Player player);
    void onCardPlayed(Player player, Card card);
    void onCardDrawn(Player player, Card card);
    void onUnoCalled(Player player);
    void onColorSelected(Player player, CardColor color);
    void onGameOver(Player player);
}
