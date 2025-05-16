package com.example.fpoeminiproject3.model;

public class HumanPlayer extends Player {
    private Card selectedCard;
    private CardColor selectedCardColor;
    private boolean pressedUnoForOpponent = false;

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public void playTurn() {
    }

    public void selectCard(Card card) {
        this.selectedCard = card;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void selectColor(CardColor color) {
        this.selectedCardColor = color;
    }

    public CardColor getSelectedCardColor() {
        return selectedCardColor;
    }

    @Override
    public void callUno() {
        this.calledUno = true;
    }

    public void resetUnoState() {
        this.calledUno = false;
        this.pressedUnoForOpponent = false;
    }
}