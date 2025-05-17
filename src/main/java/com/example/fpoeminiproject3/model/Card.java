package com.example.fpoeminiproject3.model;

public class Card {
    private CardColor color;
    private CardType type;
    private String imageCardPath;

    public Card(CardColor color, CardType type) {
        this.color = color;
        this.type = type;
        this.imageCardPath = ImagePath();
    }

    public String ImagePath() {
        if (color == CardColor.WILD) {
            if (type == CardType.WILD_DRAW_FOUR){
                return "/com/example/fpoeminiproject3/images/4_wild_draw.png";
            }
            return "/com/example/fpoeminiproject3/images/wild.png";
        }
        switch (type) {
            case SKIP:
                return String.format("/com/example/fpoeminiproject3/images/skip_%s.png", color.name().toLowerCase());
            case REVERSE:
                return String.format("/com/example/fpoeminiproject3/images/reverse_%s.png", color.name().toLowerCase());
            case DRAW_TWO:
                return String.format("/com/example/fpoeminiproject3/images/2_wild_draw_%s.png", color.name().toLowerCase());
            default:
                break;
        }
        return String.format("/com/example/fpoeminiproject3/images/%d_%s.png", type.ordinal(), color.name().toLowerCase());
    }

    public CardColor getColor() {
        return color;
    }

    public CardType getType() {
        return type;
    }

    public String getImageCardPath() {
        return imageCardPath;
    }

    public boolean similarCard(Card other) {
        return color == other.color || type == other.type ||
                color == CardColor.WILD || other.color == CardColor.WILD;
    }

    public boolean specialCard() {
        return type == CardType.SKIP || type == CardType.REVERSE ||
                type == CardType.WILD || type == CardType.DRAW_TWO ||
                type == CardType.WILD_DRAW_FOUR;
    }

    @Override
    public String toString() {
        return color + " " + type;
    }
}
