package com.example.fpoeminiproject3.model;

/**
 * Represents the possible colors of UNO cards, including the special WILD color.
 */
public enum CardColor {
    RED, BLUE, GREEN, YELLOW, WILD;

    /**
     * Returns a properly formatted string representation of the color.
     * @return the formatted color name
     */
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toUpperCase();
    }
}
