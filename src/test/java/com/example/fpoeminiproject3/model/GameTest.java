package com.example.fpoeminiproject3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica el estado inicial del juego y las interacciones básicas con cartas.
 */
public class GameTest {
    private Game game;
    private HumanPlayer humanPlayer;
    private MachinePlayer machinePlayer;

    /**
     * Configura el entorno de prueba antes de cada test.
     * Inicializa un nuevo juego con un jugador humano y una máquina.
     */
    @BeforeEach
    public void setUp() {
        game = new Game("JugadorPrueba");
        humanPlayer = game.getHumanPlayer();
        machinePlayer = game.getMachinePlayer();
    }

    /**
     * Prueba el estado inicial del juego.
     * Verifica que ambos jugadores tengan 5 cartas y que haya una carta en el descarte.
     */
    @Test
    public void testInitialGameState() {
        assertEquals(5, humanPlayer.getDeckSize(), "El jugador humano debe tener 5 cartas iniciales");
        assertEquals(5, machinePlayer.getDeckSize(), "La máquina debe tener 5 cartas iniciales");
        assertNotNull(game.getTopDiscardCard(), "Debe haber una carta en el descarte inicial");
    }

    /**
     * Prueba jugar una carta válida.
     * Verifica que el jugador humano reduce su mazo en 1 carta al jugar.
     * @throws GameException Si la carta no es jugable.
     */
    @Test
    public void testPlayCard() throws GameException {
        Card playableCard = humanPlayer.getCards().stream()
                .filter(card -> card.similarCard(game.getTopDiscardCard()))
                .findFirst()
                .orElseThrow();
        game.playCard(humanPlayer, playableCard);
        assertEquals(4, humanPlayer.getDeckSize(), "El jugador debe tener una carta menos después de jugar");
    }
}