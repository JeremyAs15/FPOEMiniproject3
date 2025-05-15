package com.example.fpoeminiproject3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CardDeck {
    private final Stack<Card> drawPile;
    private final Stack<Card> discardPile;

    public CardDeck() {
        drawPile = new Stack<>();
        discardPile = new Stack<>();
        initializeCardDeck();
        shuffle();
    }

    private void initializeCardDeck() {
        for (CardColor color : CardColor.values()) {
            if (color == CardColor.WILD) continue;

            drawPile.add(new Card(color, CardType.ZERO));
            for(int i = 1; i <= 9; i++) {
                CardType type = CardType.values()[i];
                drawPile.add(new Card(color, type));
            }
            drawPile.add(new Card(color, CardType.DRAW_TWO));
            drawPile.add(new Card(color, CardType.DRAW_TWO));
            drawPile.add(new Card(color, CardType.SKIP));
            drawPile.add(new Card(color, CardType.REVERSE));
        }
        for (int i = 0; i < 4; i++) {
            drawPile.add(new Card(CardColor.WILD, CardType.WILD));
            drawPile.add(new Card(CardColor.WILD, CardType.WILD_DRAW_FOUR));
        }
    }

    public void shuffle() {
        Collections.shuffle(drawPile);
    }

    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffleDiscardPile();
        }
        return drawPile.pop();
    }

    public List<Card> drawCards(int count){
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cards.add(drawCard());
        }
        return cards;
    }

    public void discardCard(Card card) {
        discardPile.push(card);
    }

    public Card getTopDiscardCard() {
        return discardPile.peek();
    }

    private void reshuffleDiscardPile() {
        if (discardPile.size() <= 1) {
            throw new IllegalStateException("No hay suficientes cartas para mezclar");
        }
        Card topCard = discardPile.pop();
        while (!discardPile.isEmpty()) {
            drawPile.push(discardPile.pop());
        }
        shuffle();
        discardPile.push(topCard);
    }
}
