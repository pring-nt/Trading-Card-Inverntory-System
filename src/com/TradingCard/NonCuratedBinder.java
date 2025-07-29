package com.TradingCard;

/**
 * A basic binder that holds any set of cards up to a fixed capacity.
 * <p>
 * Non-curated binders have no restrictions on card rarity or variant
 * and cannot be sold directly.
 */
public class NonCuratedBinder extends Binder {

    /**
     * Constructs a {@code NonCuratedBinder} with the given name.
     *
     * @param name non-null, non-blank name for this binder
     * @throws IllegalArgumentException if name is null or blank
     */
    public NonCuratedBinder(String name) {
        super(name);
    }

    /**
     * Adds a card to this binder if capacity allows.
     * <p>
     * There are no restrictions on the card's rarity or variant.
     *
     * @param card the {@link Card} to add
     * @return {@code true} if the card was added; {@code false} if the binder is full
     */
    @Override
    public boolean addCard(Card card) {
        if (CARDS.size() >= MAX_CAPACITY) {
            return false; // binder is full
        }
        return CARDS.add(card);
    }
}