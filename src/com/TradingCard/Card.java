package com.TradingCard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Card {
    private final String name;
    private final Rarity rarity;
    private final Variation variation;
    private final BigDecimal baseValue;
    private int count;

    public Card(String n, Rarity r, Variation v, BigDecimal val) {
        this.name = n.trim();
        this. rarity = r;
        this.variation = v;
        this.baseValue = val;

        count = 1;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Variation getVariation() {
        return variation;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBaseValue() {
        return this.baseValue;
    }

    public int getCount() {
        return this.count;
    }

    public BigDecimal getValue() {
        BigDecimal multiplier;
        BigDecimal value;
        switch(variation){
            case EXTENDED_ART -> multiplier = BigDecimal.valueOf(1.5);
            case FULL_ART -> multiplier = BigDecimal.valueOf(2.0);
            case ALT_ART -> multiplier = BigDecimal.valueOf(3.0);
            default -> multiplier = BigDecimal.valueOf(1.0);
        }
        value = multiplier.multiply(getBaseValue());

        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public void incrementCount() {
        this.count++;
    }

    public void decrementCount() {
        if(this.count > 0) this.count--;
    }

    public static Card copyCard(Card c) {
        if (c != null) {
            return new Card(c.getName(), c.getRarity(), c.getVariation(), c.getBaseValue());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                " | Rarity: " + rarity +
                " | Variation: " + variation +
                " | Count: " + count +
                " | Value: $" + getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card other)) return false;
        return this.name.equalsIgnoreCase(other.name) &&
                this.rarity == other.rarity &&
                this.variation == other.variation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), rarity, variation);
    }

}
