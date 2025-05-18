package com.example.fpoeminiproject3.model;

/**
 * Represents all possible types of UNO cards
 */
public enum CardType {
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
    SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR;

    /**
     * Returns a properly formatted string representation of the card type and converts enum names to title case and replaces underscores with spaces.
     * @return the formatted card type name in title case with spaces
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toUpperCase().replace("_", " ");
    }
}
