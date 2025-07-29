package com.TradingCard;

import com.TradingCard.Enums.Rarity;

import java.math.BigDecimal;

/**
 * A binder that holds only common and uncommon cards and allows selling of its contents
 * at their real value with no handling fee.
 * <p>
 * Implements {@link Sellable} so that it can be sold through the inventory system.
 */
public class PauperBinder extends Binder implements Sellable {

    /**
     * Constructs a {@code PauperBinder} with the given name.
     *
     * @param name non-null, non-blank name for this binder
     * @throws IllegalArgumentException if name is null or blank
     */
    public PauperBinder(String name) {
        super(name);
    }

    /**
     * Adds a card to this binder if capacity allows and the card's rarity is either common or uncommon.
     *
     * @param card the {@link Card} to add
     * @return {@code true} if the card was added; {@code false} if the binder is full
     * @throws IllegalArgumentException if the card's rarity is neither common nor uncommon
     */
    @Override
    public boolean addCard(Card card) {
        if (CARDS.size() >= MAX_CAPACITY) {
            return false; // full, cannot add
        }

        if (card.getRarity() != Rarity.COMMON
                && card.getRarity() != Rarity.UNCOMMON) {
            throw new IllegalArgumentException("Card with rarity " + card.getRarity() + " is not allowed in a pauper binder");
        }
        return CARDS.add(card);
    }

    /**
     * Sells all cards in this binder.  Computes the total real value of contained cards,
     * clears the binder, and returns the amount earned.  Pauper binders incur no handling fee.
     *
     * @return total sale price as {@link BigDecimal}
     */
    @Override
    public BigDecimal sell() {
        BigDecimal total = getValue();
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
