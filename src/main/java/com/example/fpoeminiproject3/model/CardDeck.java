package com.example.fpoeminiproject3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a deck of UNO cards with draw and discard piles.
 * Handles card distribution, discarding, and pile management.
 */
public class CardDeck {
    private final Stack<Card> drawPile;
    private final Stack<Card> discardPile;

    /**
     * Constructs a new CardDeck with initialized and shuffled cards and creates a standard UNO deck with 108 cards including:
     */
    public CardDeck() {
        drawPile = new Stack<>();
        discardPile = new Stack<>();
        initializeCardDeck();
        shuffle();
    }

    /**
     * Initializes the deck with standard UNO cards.
     * Excludes WILD color for number and action cards.
     * Adds special WILD cards separately.
     */
    private void initializeCardDeck() {
        drawPile.clear();
        for (CardColor color : CardColor.values()) {
            if (color == CardColor.WILD) continue;

            drawPile.add(new Card(color, CardType.ZERO));
            for (int i = 1; i <= 9; i++) {
                CardType type = CardType.values()[i];
                drawPile.add(new Card(color, type));
                drawPile.add(new Card(color, type)); // Second copy
            }

            drawPile.add(new Card(color, CardType.DRAW_TWO));
            drawPile.add(new Card(color, CardType.DRAW_TWO));
            drawPile.add(new Card(color, CardType.SKIP));
            drawPile.add(new Card(color, CardType.SKIP));
            drawPile.add(new Card(color, CardType.REVERSE));
            drawPile.add(new Card(color, CardType.REVERSE));
        }

        for (int i = 0; i < 4; i++) {
            drawPile.add(new Card(CardColor.WILD, CardType.WILD));
            drawPile.add(new Card(CardColor.WILD, CardType.WILD_DRAW_FOUR));
        }
    }

    /**
     * Shuffles the draw pile to randomize card order.
     */
    public void shuffle() {
        Collections.shuffle(drawPile);
    }

    /**
     * Draws a single card from the draw pile. If draw pile is empty, reshuffles discard pile (except top card) into draw pile.
     * @return the drawn Card
     * @throws IllegalStateException if no cards are available to draw
     */
    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffleDiscardPile();
        }
        return drawPile.pop();
    }

    /**
     * Draws multiple cards from the draw pile.
     * @param count number of cards to draw
     * @return List of drawn Cards
     */
    public List<Card> drawCards(int count) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cards.add(drawCard());
        }
        return cards;
    }

    /**
     * Discards a card to the discard pile.
     * @param card the Card to discard
     */
    public void discardCard(Card card) {
        discardPile.push(card);
    }

    /**
     * Gets the top card of the discard pile without removing it.
     * @return the top Card of discard pile
     */
    public Card getTopDiscardCard() {
        return discardPile.peek();
    }

    /**
     * Reshuffles the discard pile (except top card) into the draw pile and preserves the top card of discard pile to maintain game state.
     * @throws IllegalStateException if there are not enough cards to reshuffle
     */
    private void reshuffleDiscardPile() {
        if (discardPile.size() <= 1) {
            throw new IllegalStateException("There aren't enough cards to shuffle");
        }
        Card topCard = discardPile.pop();
        while (!discardPile.isEmpty()) {
            drawPile.push(discardPile.pop());
        }
        shuffle();
        discardPile.push(topCard);
    }

    /**
     * Gets the current size of the draw pile.
     * @return number of cards remaining in draw pile
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }

    /**
     * Gets the current size of the discard pile.
     * @return number of cards in discard pile
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }
}