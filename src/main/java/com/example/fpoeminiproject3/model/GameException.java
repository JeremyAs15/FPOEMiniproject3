package com.example.fpoeminiproject3.model;

/**
 * Custom exception class for game-related errors in the UNO game.
 * Used to indicate violations of game rules or invalid player actions.
 */
public class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }
}
