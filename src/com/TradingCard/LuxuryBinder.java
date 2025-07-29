package com.TradingCard;

import com.TradingCard.Enums.Variation;

import java.math.BigDecimal;

/**
 * A binder that holds only non-normal variant cards (e.g., extended-art, full-art, alt-art)
 * and allows selling at a collector-defined price (not below real value) plus a 10% handling fee.
 * <p>
 * Implements {@link Sellable} so that it can be sold through the inventory system.
 */
public class LuxuryBinder extends Binder implements Sellable {

    private static final BigDecimal HANDLING_RATE = new BigDecimal("0.10");
    private BigDecimal customPrice = BigDecimal.ZERO;

    /**
     * Constructs a {@code LuxuryBinder} with the given name.
     *
     * @param name non-null, non-blank name for this binder
     * @throws IllegalArgumentException if name is null or blank
     */
    public LuxuryBinder(String name) {
        super(name);
    }

    /**
     * Adds a card to this binder if capacity allows and the card's variant is not normal.
     *
     * @param card the {@link Card} to add
     * @return {@code true} if the card was added; {@code false} if the binder is full
     * @throws IllegalArgumentException if the card's variant is normal
     */
    @Override
    public boolean addCard(Card card) {
        if (CARDS.size() >= MAX_CAPACITY) {
            return false; // binder is full
        }
        if (card.getVariation() == Variation.NORMAL) {
            throw new IllegalArgumentException("Card with variant NORMAL is not allowed in a luxury binder");
        }
        return CARDS.add(card);
    }

    /**
     * Sets a custom sale price for this binder.  The custom price must not be lower
     * than the total real value of the cards currently inside.
     *
     * @param price desired price, non-null
     * @throws IllegalArgumentException if price is below the total real value
     */
    public void setCustomPrice(BigDecimal price) {
        BigDecimal min = getValue();
        if (price.compareTo(min) < 0) {
            throw new IllegalArgumentException(
                    "Custom price cannot be below total real value (" + min + ")");
        }
        this.customPrice = price;
    }

    /**
     * Computes the total sale price for all cards in this binder,
     * using the custom price if set (otherwise the real value sum),
     * plus a 10% handling fee, clears the binder, and returns the amount earned.
     *
     * @return total sale price as {@link BigDecimal}
     */
    @Override
    public BigDecimal sell() {
        BigDecimal base;
        if(customPrice.compareTo(BigDecimal.ZERO) > 0) {
            base = customPrice;
        }
        else {
            base = getValue();
        }
        BigDecimal fee = base.multiply(HANDLING_RATE);
        BigDecimal total = base.add(fee);
        CARDS.clear();
        return total;
    }

    /**
     * Computes the total real value of all cards currently in this binder.
     * Does not modify the binder's contents.
     *
     * @return sum of card values as {@link BigDecimal}
     */
    @Override
    public BigDecimal getValue() {
        BigDecimal total = BigDecimal.ZERO;
        for (Card card : CARDS) {
            total = total.add(card.getValue());
        }
        return total;
    }
}
