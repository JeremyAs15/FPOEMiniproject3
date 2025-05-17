package com.example.fpoeminiproject3.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Valida el funcionamiento del mazo, incluyendo cartas tomadas y tamaño inicial.
 */
public class CardDeckTest {

    /**
     * Prueba tomar una carta del mazo.
     * Verifica que la carta tomada no sea nula y que el mazo reduzca su tamaño en 1.
     */
    @Test
    public void testDrawCard() {
        CardDeck deck = new CardDeck();
        Card card = deck.drawCard();
        assertNotNull(card);
        assertEquals(63, deck.getDrawPileSize(), "El mazo debería tener 63 cartas después de sacar 1");
    }

    /**
     * Prueba el tamaño inicial del mazo.
     * Verifica que el mazo comience con exactamente 64 cartas (suma de drawPile y discardPile).
     */
    @Test
    public void testInitialDeckSize() {
        CardDeck deck = new CardDeck();
        assertEquals(64, deck.getDrawPileSize() + deck.getDiscardPileSize(), "El mazo inicial debe tener 64 cartas");
    }
}