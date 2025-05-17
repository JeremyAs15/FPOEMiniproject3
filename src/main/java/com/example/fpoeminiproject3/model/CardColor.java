package com.example.fpoeminiproject3.model;

public enum CardColor {
    RED, BLUE, GREEN, YELLOW, WILD;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
