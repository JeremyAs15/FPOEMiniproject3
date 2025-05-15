package com.example.fpoeminiproject3.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected String name;
    protected List<Card> cards;
    protected Game game;
    protected boolean calledUno = false;

    public Player(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getDeckSize() {
        return cards.size();
    }

    public boolean hasUno() {
        return cards.size() == 1;
    }

    public abstract void playTurn();

    public abstract void callUno();

    public String getName() {
        return name;
    }

    public boolean calledUno() {
        return calledUno;
    }
}
