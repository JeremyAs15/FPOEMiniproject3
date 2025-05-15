package com.example.fpoeminiproject3.model;

public enum CardType {
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, WILD, DRAW_TWO, WILD_DRAW_FOUR, REVERSE, SKIP;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }
}
