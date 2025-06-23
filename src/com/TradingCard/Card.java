package com.TradingCard;

import java.math.BigDecimal;

public class Card {
    private final String name;
    private final Rarity rarity;
    private final Variation variation;
    private final BigDecimal value;
    private int count;

    public Card(String n, Rarity r, Variation v, BigDecimal val) {
        this.name = n;
        this. rarity = r;
        this.variation = v;
        BigDecimal multiplier;
        switch(variation){
            case ALT_ART -> {
                multiplier = BigDecimal.valueOf(1.5);
                this.value = multiplier.multiply(val);
            }
            case FULL_ART -> {
                multiplier = BigDecimal.valueOf(2.0);
                this.value = multiplier.multiply(val);
            }
            case EXTENDED_ART -> {
                multiplier = BigDecimal.valueOf(3.0);
                this.value = multiplier.multiply(val);
            }
            default -> {
                this.value = val;
            }
        }
        count = 1;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public int getCount() {
        return this.count;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public Variation getVariation() {
        return this.variation;
    }
}
