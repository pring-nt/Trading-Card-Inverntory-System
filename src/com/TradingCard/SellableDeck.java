package com.TradingCard;

import java.math.BigDecimal;

/**
 * A deck that can be sold through the inventory system.
 * <p>
 * Implements {@link Sellable}, allowing its total value to be calculated and its contents to be liquidated.
 */
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

    /**
     * Sells all cards in the deck, returning their total value and clearing the deck.
     *
     * @return total value of all cards in the deck as {@link BigDecimal}
     */
    @Override
    public BigDecimal sell() {
        BigDecimal total = getValue();
        CARDS.clear();
        return total;
    }

    /**
     * Calculates the total value of all cards in the deck without removing them.
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

    /**
     * Indicates that this deck can be sold.
     *
     * @return {@code true} always, as sellable decks are sellable
     */
    @Override
    public boolean isSellable() {
        return true;
    }
}
