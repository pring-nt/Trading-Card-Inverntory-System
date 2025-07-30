package com.System;

import com.TradingCard.Sellable;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

/**
 * EnhancedTCIS extends the core InventorySystem to provide functionality
 * for tracking and accumulating total earnings from selling cards, decks, and binders.
 * <p>
 * This subclass introduces a collectorEarnings field which reflects the total value
 * gained through all sales operations made through this instance.
 */
public class EnhancedTCIS extends InventorySystem {
    private BigDecimal collectorEarnings;

    /**
     * Constructs a new EnhancedTCIS with zero collector earnings.
     */
    public EnhancedTCIS() {
        super();
        this.collectorEarnings = BigDecimal.ZERO;
    }

    /**
     * Sells a binder and adds the resulting earnings to the collector’s total.
     *
     * @param binderName name of the binder to sell
     * @throws NoSuchElementException if the binder does not exist
     * @throws IllegalStateException if the binder is not sellable
     */
    public void sellBinder(String binderName) {
        BigDecimal earnings = BINDER_MANAGER.sellBinder(binderName);
        this.collectorEarnings = this.collectorEarnings.add(earnings);
    }

    /**
     * Sells a deck and adds the resulting earnings to the collector’s total.
     *
     * @param deckName name of the deck to sell
     * @throws NoSuchElementException if the deck does not exist
     * @throws IllegalStateException if the deck is not sellable
     */
    public void sellDeck(String deckName) {
        BigDecimal earnings = DECK_MANAGER.sellDeck(deckName);
        this.collectorEarnings = this.collectorEarnings.add(earnings);
    }

    /**
     * Sets a custom price for a binder if it is a {@code LuxuryBinder}.
     *
     * @param name  the name of the binder
     * @param value the custom price to assign
     *
     * @throws NoSuchElementException if no binder with the given name exists
     * @throws ClassCastException     if the binder is not a {@code LuxuryBinder}
     */
    public void setBinderPrice(String name, BigDecimal value) {
        BINDER_MANAGER.setCustomPrice(name, value);
    }

    /**
     * Checks if a binder with the specified name is a LuxuryBinder.
     *
     * @param name the name of the binder to check
     * @return {@code true} if the binder is a LuxuryBinder, {@code false} otherwise
     * @throws NoSuchElementException if no binder with the given name exists
     */
    public boolean isLuxuryBinder(String name) {
        return BINDER_MANAGER.isLuxuryBinder(name);
    }

    /**
     * Gets the custom price set for a LuxuryBinder.
     *
     * @param binderName the name of the binder
     * @return the custom price of the LuxuryBinder
     * @throws NoSuchElementException if the binder does not exist
     * @throws IllegalStateException if the binder is not a LuxuryBinder
     */
    public BigDecimal getLuxuryBinderCustomPrice(String binderName) {
        return BINDER_MANAGER.getLuxuryBinderCustomPrice(binderName);
    }


    /**
     * Sells a card by name from the collection and adds the resulting earnings
     * to the collector’s total.
     *
     * @param cardName name of the card to sell
     * @throws NoSuchElementException if no card with the given name exists
     */
    public void sellCard(String cardName) {
        BigDecimal earnings = CARD_COLLECTION.sellCardByName(cardName);
        this.collectorEarnings = this.collectorEarnings.add(earnings);
    }

    /**
     * Gets the total accumulated earnings from all sales.
     *
     * @return the current collector earnings
     */
    public BigDecimal getCollectorEarnings() {
        return this.collectorEarnings;
    }

    /**
     * Checks whether the specified binder is sellable.
     *
     * <p>This method delegates the check to the {@code BINDER_MANAGER}, which determines
     * if the binder with the given name implements the {@code Sellable} interface.</p>
     *
     * @param name the name of the binder to check
     * @return {@code true} if the binder is sellable; {@code false} otherwise
     * @throws NoSuchElementException if no binder with the given name exists
     */
    public boolean isBinderSellable(String name) {
        return BINDER_MANAGER.isBinderSellable(name);
    }

    /**
     * Checks whether the specified deck is sellable.
     *
     * <p>This method delegates the check to the {@code DECK_MANAGER}, which determines
     * if the deck with the given name implements the {@code Sellable} interface.</p>
     *
     * @param name the name of the deck to check
     * @return {@code true} if the deck is sellable; {@code false} otherwise
     * @throws NoSuchElementException if no deck with the given name exists
     */
    public boolean isDeckSellable(String name) {
        return DECK_MANAGER.isDeckSellable(name);
    }

    /**
     * Gets the current value of a binder without selling it.
     * <p>
     * This method delegates to {@code BINDER_MANAGER} to retrieve the estimated value
     * of the specified binder. The value is computed based on the contents of the binder
     * and does not include any handling fees. Only sellable binders can be evaluated.
     *
     * @param binderName the name of the binder to evaluate
     * @return the estimated value of the binder as a {@link BigDecimal}
     * @throws NoSuchElementException if no binder with that name exists
     * @throws IllegalStateException if the binder is not sellable
     */
    public BigDecimal getBinderValue(String binderName) {
        return BINDER_MANAGER.getBinderValue(binderName);
    }

    /**
     * Gets the current value of a deck without selling it.
     * <p>
     * This method delegates to {@code DECK_MANAGER} to retrieve the estimated value
     * of the specified deck. Only decks that implement {@link Sellable} can be evaluated.
     *
     * @param deckName the name of the deck to evaluate
     * @return the estimated value of the deck as a {@link BigDecimal}
     * @throws NoSuchElementException if no deck with that name exists
     * @throws IllegalStateException if the deck is not sellable
     */
    public BigDecimal getDeckValue(String deckName) {
        return DECK_MANAGER.getDeckValue(deckName);
    }

}
