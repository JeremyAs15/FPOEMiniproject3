package com.example.fpoeminiproject3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a player in the game.
 */
public abstract class Player {
    protected String name;
    protected List<Card> cards;
    protected Game game;
    protected boolean calledUno = false;

    /**
     * Constructs a new Player with the specified name.
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }

    /**
     * Sets the game instance this player belongs to.
     * @param game the game instance
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Adds a single card to the player's hand.
     * @param card the card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Adds multiple cards to the player's hand.
     * @param cards the list of cards to add
     */
    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }

    /**
     * Removes a card from the player's hand.
     * @param card the card to remove
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * Gets the player's current hand of cards.
     * @return an unmodifiable list of cards in the player's hand
     */
    public List<Card> getCards() {
        return List.copyOf(cards);
    }

    /**
     * Gets the number of cards in the player's hand.
     * @return the size of the player's hand
     */
    public int getDeckSize() {
        return cards.size();
    }

    /**
     * Checks if the player has UNO.
     * @return true if the player has one card, false otherwise
     */
    public boolean hasUno() {
        return cards.size() == 1;
    }

    /**
     * Abstract method to handle player's turn.
     */
    public abstract void playTurn();

    /**
     * Abstract method to handle UNO calling.
     */
    public abstract void callUno();

    /**
     * Gets the player's name.
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the player has called UNO.
     * @return true if UNO was called, false otherwise
     */
    public boolean calledUno() {
        return calledUno;
    }
}