package com.example.fpoeminiproject3.model;

public enum CardColor {
    BLUE, GREEN, RED, YELLOW, WILD;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
