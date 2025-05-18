package com.example.fpoeminiproject3.model;

/**
 * Represents a UNO card with color, type, and associated image path and provides methods to determine card properties and relationships between cards.
 */
public class Card {
    private CardColor color;
    private CardType type;
    private String imageCardPath;

    /**
     * Constructs a new Card with specified color and type and automatically generates the appropriate image path for the card.
     * @param color the color of the card
     * @param type the type of the card
     */
    public Card(CardColor color, CardType type) {
        this.color = color;
        this.type = type;
        this.imageCardPath = ImagePath();
    }

    /**
     * Determines the image path for this card based on its color and type.
     * @return the path to the card's image resource
     */
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
                return String.format("/com/example/fpoeminiproject3/images/reserve_%s.png", color.name().toLowerCase());
            case DRAW_TWO:
                return String.format("/com/example/fpoeminiproject3/images/2_wild_draw_%s.png", color.name().toLowerCase());
            default:
                break;
        }
        return String.format("/com/example/fpoeminiproject3/images/%d_%s.png",
                type.ordinal(),
                color.name().toLowerCase());
    }

    /**
     * Gets the color of this card.
     * @return the card's color
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * Gets the type of this card.
     * @return the card's type
     */
    public CardType getType() {
        return type;
    }

    /**
     * Gets the image path for this card.
     * @return the path to the card's image resource
     */
    public String getImageCardPath() {
        return imageCardPath;
    }

    /**
     * Determines if this card is similar to another card for gameplay purposes.
     * @param other the card to compare with
     * @return true if the cards are similar, false otherwise
     */
    public boolean similarCard(Card other) {
        return color == other.color || type == other.type ||
                color == CardColor.WILD || other.color == CardColor.WILD;
    }

    /**
     * Checks if this card is a special card.
     * @return true if the card is a special action card, false otherwise
     */
    public boolean specialCard() {
        return type == CardType.SKIP || type == CardType.REVERSE ||
                type == CardType.WILD || type == CardType.DRAW_TWO ||
                type == CardType.WILD_DRAW_FOUR;
    }

    /**
     * Representation of the card in the format "COLOR TYPE".
     * @return string representation of the card
     */
    @Override
    public String toString() {
        return color + " " + type;
    }
}