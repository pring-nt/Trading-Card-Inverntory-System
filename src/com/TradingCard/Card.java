package com.TradingCard;

public class Card {
    private final String name;
    private final Rarity rarity;
    private final Variation variation;
    private double value;
    private int count;

    public Card(String n, Rarity r, Variation v, double val) {
        this.name = n;
        this. rarity = r;
        this.variation = v;
        this.value = val;
        count = 1;
    }
}
