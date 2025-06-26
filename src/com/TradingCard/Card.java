package com.TradingCard;

import com.TradingCard.Enums.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a trading card with a name, rarity, variation, base value, and count.
 * <p>
 * Provides methods to compute current market value based on variation and to
 * manage the count of copies in the collection.
 */
public class Card {
    private final String name;
    private final Rarity rarity;
    private final Variation variation;
    private final BigDecimal baseValue;
    private int count;

    /**
     * Constructs a Card with given attributes and an initial count of 1.
     * @param n    the name of the card (must be non-null, non-empty)
     * @param r    the rarity of the card
     * @param v    the variation of the card
     * @param val  the base monetary value of the card
     * @throws IllegalArgumentException if name is null or blank
     */
    public Card(String n, Rarity r, Variation v, BigDecimal val) {
        if (n == null || n.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.name = n.trim();
        this.rarity = r;
        this.variation = v;
        this.baseValue = val;
        this.count = 1; // initial copy count
    }

    /**
     * @return the rarity of this card
     */
    public Rarity getRarity() {
        return rarity;
    }

    /**
     * @return the variation of this card
     */
    public Variation getVariation() {
        return variation;
    }

    /**
     * @return the name of this card
     */
    public String getName() {
        return name;
    }

    /**
     * @return the base monetary value of this card
     */
    public BigDecimal getBaseValue() {
        return this.baseValue;
    }

    /**
     * @return the current count of copies in the collection
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Calculates the market value of the card based on its variation multiplier.
     * The result is rounded to two decimal places.
     * @return adjusted value according to variation
     */
    public BigDecimal getValue() {
        BigDecimal multiplier;
        switch (variation) {
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
     * @return a string representation including name, rarity, variation, count, and value
     */
    @Override
    public String toString() {
        return "Name: " + name +
                " | Rarity: " + rarity +
                " | Variation: " + variation +
                " | Count: " + count +
                " | Value: $" + getValue();
    }

    /**
     * Equals based on name (case-insensitive), rarity, and variation.
     * @param obj the object to compare
     * @return true if same type and attributes, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card other)) return false;
        return this.name.equalsIgnoreCase(other.name)
                && this.rarity == other.rarity
                && this.variation == other.variation;
    }

    /**
     * Hash code consistent with equals, using lowercase name, rarity, and variation.
     * @return computed hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), rarity, variation);
    }
}
