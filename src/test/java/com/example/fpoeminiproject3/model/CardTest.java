package com.example.fpoeminiproject3.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Valida la lógica de comparación de cartas y las rutas de imágenes.
 */
public class CardTest {

    /**
     * Prueba la similitud entre cartas.
     * Verifica que dos cartas sean similares si comparten color, tipo o son comodines.
     */
    @Test
    public void testSimilarCard() {
        Card redFive = new Card(CardColor.RED, CardType.FIVE);
        Card blueFive = new Card(CardColor.BLUE, CardType.FIVE);
        Card redSkip = new Card(CardColor.RED, CardType.SKIP);
        Card wild = new Card(CardColor.WILD, CardType.WILD);

        assertTrue(redFive.similarCard(blueFive), "Cartas con mismo tipo deberían ser similares");
        assertTrue(redFive.similarCard(redSkip), "Cartas con mismo color deberían ser similares");
        assertTrue(redFive.similarCard(wild), "Toda carta debería ser similar a un comodín");
    }

    /**
     * Prueba las rutas de imágenes de las cartas.
     * Verifica que las rutas generadas para comodines y cartas numéricas sean correctas.
     */
    @Test
    public void testImagePath() {
        Card wildCard = new Card(CardColor.WILD, CardType.WILD);
        assertEquals("/com/example/fpoeminiproject3/images/wild.png", wildCard.getImageCardPath(), "Ruta de imagen incorrecta para comodín");

        Card blueTwo = new Card(CardColor.BLUE, CardType.TWO);
        assertEquals("/com/example/fpoeminiproject3/images/2_blue.png", blueTwo.getImageCardPath(), "Ruta de imagen incorrecta para carta numérica");
    }
}