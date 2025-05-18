package com.example.fpoeminiproject3.model;

/**
 * Represents a human player in the  game.
 */
public class HumanPlayer extends Player {
    private Card selectedCard;
    private CardColor selectedCardColor;

    /**
     * Constructs a new HumanPlayer with the specified name.
     * @param name the name of the human player
     */
    public HumanPlayer(String name) {
        super(name);
    }

    /**
     * Placeholder for human player's turn logic.
     */
    @Override
    public void playTurn() {
    }

    /**
     * Selects a card to play in the current turn.
     * @param card the card to select for potential play
     */
    public void selectCard(Card card) {
        this.selectedCard = card;
    }

    /**
     * Gets the currently selected card.
     * @return the selected Card, or null if no card is selected
     */
    public Card getSelectedCard() {
        return selectedCard;
    }

    /**
     * Selects a color when playing a wild card.
     * @param color the color to select (cannot be WILD)
     */
    public void selectColor(CardColor color) {
        this.selectedCardColor = color;
    }

    /**
     * Gets the selected color for a wild card.
     * @return the selected CardColor, or null if no color is selected
     */
    public CardColor getSelectedCardColor() {
        return selectedCardColor;
    }

    /**
     * Marks that the player has called UNO.
     */
    @Override
    public void callUno() {
        this.calledUno = true;
    }

    /**
     * Resets the UNO call state.
     */
    public void resetUnoState() {
        this.calledUno = false;
    }
}