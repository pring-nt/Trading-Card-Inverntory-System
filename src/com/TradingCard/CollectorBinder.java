package com.TradingCard;

import com.TradingCard.Enums.Rarity;
import com.TradingCard.Enums.Variation;

/**
 * A binder that holds only rare or legendary cards with non-normal variants
 * (e.g., extended-art, full-art, alt-art) and cannot be sold directly.
 */
public class CollectorBinder extends Binder {

    /**
     * Constructs a {@code CollectorBinder} with the given name.
     *
     * @param name non-null, non-blank name for this binder
     * @throws IllegalArgumentException if name is null or blank
     */
    public CollectorBinder(String name) {
        super(name);
    }

    /**
     * Adds a card to this binder if capacity allows and the card meets the rarity
     * and variant requirements: rarity must be rare or legendary, and variant must not be normal.
     *
     * @param card the {@link Card} to add
     * @return {@code true} if the card was added; {@code false} if the binder is full
     * @throws IllegalArgumentException if the card does not meet rarity or variant constraints
     */
    @Override
    public boolean addCard(Card card) {
        if (CARDS.size() >= MAX_CAPACITY) {
            return false; // binder is full
        }
        Rarity rarity = card.getRarity();
        Variation variant = card.getVariation();
        if ((rarity != Rarity.RARE && rarity != Rarity.LEGENDARY) || variant == Variation.NORMAL) {
            throw new IllegalArgumentException(
                    String.format("Card %s [%s, %s] is not allowed in a collector binder",
                            card.getName(), rarity, variant)
            );
        }
        return CARDS.add(card);
    }
}

