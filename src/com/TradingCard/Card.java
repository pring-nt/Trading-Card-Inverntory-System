package com.TradingCard;

import com.TradingCard.Enums.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a trading card with a NAME, RARITY, VARIATION, base value, and count.
 * <p>
 * Provides methods to compute current market value based on VARIATION and to
 * manage the count of copies in the collection.
 */
public class Card {
    private final String NAME;
    private final Rarity RARITY;
    private final Variation VARIATION;
    private final BigDecimal BASE_VALUE;
    private int count;

    /**
     * Constructs a Card with given attributes and an initial count of 1.
     * @param n    the NAME of the card (must be non-null, non-empty)
     * @param r    the RARITY of the card
     * @param v    the VARIATION of the card
     * @param val  the base monetary value of the card
     * @throws IllegalArgumentException if NAME is null or blank
     */
    public Card(String n, Rarity r, Variation v, BigDecimal val) {
        if (n == null || n.trim().isEmpty()) {
            throw new IllegalArgumentException("NAME cannot be empty");
        }
        this.NAME = n.trim();
        this.RARITY = r;
        this.VARIATION = v;
        this.BASE_VALUE = val;
        this.count = 1; // initial copy count
    }

    /**
     * @return the RARITY of this card
     */
    public Rarity getRarity() {
        return RARITY;
    }

    /**
     * @return the VARIATION of this card
     */
    public Variation getVariation() {
        return VARIATION;
    }

    /**
     * @return the NAME of this card
     */
    public String getName() {
        return NAME;
    }

    /**
     * @return the base monetary value of this card
     */
    public BigDecimal getBaseValue() {
        return this.BASE_VALUE;
    }

    /**
     * @return the current count of copies in the collection
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Calculates the market value of the card based on its VARIATION multiplier.
     * The result is rounded to two decimal places.
     * @return adjusted value according to VARIATION
     */
    public BigDecimal getValue() {
        BigDecimal multiplier;
        switch (VARIATION) {
            case EXTENDED_ART -> multiplier = BigDecimal.valueOf(1.5);
            case FULL_ART     -> multiplier = BigDecimal.valueOf(2.0);
            case ALT_ART      -> multiplier = BigDecimal.valueOf(3.0);
            default           -> multiplier = BigDecimal.valueOf(1.0);
        }
        BigDecimal value = multiplier.multiply(getBaseValue());
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Increments the count of this card by one.
     */
    public void incrementCount() {
        this.count++;
    }

    /**
     * Decrements the count of this card by one, not falling below zero.
     */
    public void decrementCount() {
        if (this.count > 0) this.count--;
    }

    /**
     * Creates a shallow copy of the given card with count reset to 1.
     * @param c the card to copy
     * @return a new Card instance with identical attributes (count=1), or null if c is null
     */
    public static Card copyCard(Card c) {
        if (c != null) {
            return new Card(c.getName(), c.getRarity(), c.getVariation(), c.getBaseValue());
        }
        return null;
    }

    /**
     * @return a string representation including NAME, RARITY, VARIATION, count, and value
     */
    @Override
    public String toString() {
        return "Name: " + NAME +
                " | Rarity: " + RARITY +
                " | Variation: " + VARIATION +
                " | Count: " + count +
                " | Value: $" + getValue();
    }

    /**
     * Equals based on NAME (case-insensitive), RARITY, and VARIATION.
     * @param obj the object to compare
     * @return true if same type and attributes, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card other)) return false;
        return this.NAME.equalsIgnoreCase(other.NAME)
                && this.RARITY == other.RARITY
                && this.VARIATION == other.VARIATION;
    }

    /**
     * Hash code consistent with equals, using lowercase NAME, RARITY, and VARIATION.
     * @return computed hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(NAME.toLowerCase(), RARITY, VARIATION);
    }
}
