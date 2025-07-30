package com.TradingCard;

import com.TradingCard.Enums.Rarity;

import java.math.BigDecimal;

/**
 * A binder that holds only rare and legendary cards and allows selling of its contents
 * with a 10% handling fee on top of the total real value.
 * <p>
 * Implements {@link Sellable} so that it can be sold through the inventory system.
 */
public class RaresBinder extends Binder implements Sellable {

    private static final BigDecimal HANDLING_RATE = new BigDecimal("0.10");

    /**
     * Constructs a {@code RaresBinder} with the given name.
     *
     * @param name non-null, non-blank name for this binder
     * @throws IllegalArgumentException if name is null or blank
     */
    public RaresBinder(String name) {
        super(name);
    }

    /**
     * Indicates that this binder can be sold.
     *
     * @return {@code true} always, as rares binders are sellable
     */
    @Override
    public boolean isSellable() {
        return true;
    }

    /**
     * Adds a card to this binder if capacity allows and the card's rarity is rare or legendary.
     *
     * @param card the {@link Card} to add
     * @return {@code true} if the card was added; {@code false} if the binder is full
     * @throws IllegalArgumentException if the card's rarity is neither rare nor legendary
     */
    @Override
    public boolean addCard(Card card) {
        if (CARDS.size() >= MAX_CAPACITY) {
            return false; // binder is full
        }
        if (card.getRarity() != Rarity.RARE
                && card.getRarity() != Rarity.LEGENDARY) {
            throw new IllegalArgumentException("Card with rarity " + card.getRarity() + " is not allowed in a rares binder");
        }
        return CARDS.add(card);
    }

    /**
     * Computes the total sale price for all cards in this binder,
     * including a 10% handling fee, clears the binder, and returns the amount earned.
     *
     * @return total sale price as {@link BigDecimal}
     */
    @Override
    public BigDecimal sell() {
        BigDecimal base = getValue();
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
            if(card != null) {
                total = total.add(card.getValue());
            }
        }
        return total;
    }
}
