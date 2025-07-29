package com.TradingCard;

import java.math.BigDecimal;

public class SellableDeck extends Deck implements Sellable{

    /**
     * Constructs a {@code SellableDeck} with the specified name.
     *
     * @param name non-null, non-blank name
     * @throws IllegalArgumentException if name is null or blank
     */
    public SellableDeck(String name) {
        super(name);
    }

    @Override
    public BigDecimal sell() {
        return null;
    }

    @Override
    public BigDecimal getValue() {
        return null;
    }
}
