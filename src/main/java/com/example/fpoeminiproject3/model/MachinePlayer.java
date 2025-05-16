package com.example.fpoeminiproject3.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MachinePlayer extends Player {
    private final Random random = new Random();

    public MachinePlayer(String name) {
        super(name);
    }

    @Override
    public void playTurn() {
        try {
            if (game.getCurrentPlayer() != this) return;
            Thread.sleep(3000 + random.nextInt(1000));
            if (hasUno()) {
                callUno();
            }

            Card topCard = game.getTopDiscardCard();
            CardColor currentColor = game.getCurrentColor();

            List<Card> playableCards = cards.stream()
                    .filter(card -> {
                        if (topCard.getColor() == CardColor.WILD) {
                            return card.getColor() == currentColor || card.getColor() == CardColor.WILD;
                        }
                        return card.similarCard(topCard);
                    })
                    .toList();

            if (!playableCards.isEmpty()) {
                if (topCard.getColor() == CardColor.WILD) {
                    List<Card> sameColorCards = playableCards.stream()
                            .filter(card -> card.getColor() == currentColor)
                            .toList();
                    if (!sameColorCards.isEmpty()) {
                        playableCards = sameColorCards;
                    }
                }

                Card cardToPlay = playableCards.get(random.nextInt(playableCards.size()));
                game.playCard(this, cardToPlay);
            } else {
                game.drawCard(this);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (GameException e) {
        }
    }

    public CardColor chooseColor() {
        Map<CardColor, Integer> colorCount = new HashMap<>();

        for (Card card : cards) {
            if (card.getColor() != CardColor.WILD) {
                colorCount.put(card.getColor(), colorCount.getOrDefault(card.getColor(), 0) + 1);
            }
        }

        if (colorCount.isEmpty()) {
            CardColor[] colors = CardColor.values();
            return colors[random.nextInt(colors.length - 1)];
        }

        return colorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }
    @Override
    public void callUno() {
        this.calledUno = true;
        game.notifyUnoCalled(this);
    }
}