package com.TradingCard;

public enum Variation {
    STANDARD(1.0), // For cards that are not rare or legendary
    NORMAL(1.0),
    EXTENDED_ART(1.5),
    FULL_ART(2.0),
    ALT_ART(3.0);

    private final double multiplier;

    private Variation(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
