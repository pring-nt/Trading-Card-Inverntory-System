package com.TradingCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Abstract base class representing a binder that holds a fixed number of cards.
 * <p>
 * Supports adding cards (enforced by subclasses), removing individual cards or clearing all,
 * and retrieving a sorted view of contained cards.
 */
public abstract class Binder {
    /** maximum number of cards this binder can hold */
    protected static final int MAX_CAPACITY = 20;

    private final String NAME;
    protected final ArrayList<Card> CARDS;

    /**
     * Constructs a Binder with the given name.
     *
     * @param name non-null, non-blank name for this binder
     * @throws IllegalArgumentException if name is null or blank
     */
    public Binder(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Binder name cannot be null or blank");
        }
        this.NAME = name.trim();
        this.CARDS = new ArrayList<>();
    }

    /**
     * Retrieves the name of this binder.
     *
     * @return the binder's name
     */
    public String getName() {
        return NAME;
    }

    /**
     * Searches for a card in this binder by name.
     *
     * @param name case-insensitive card name to search
     * @return the matching Card, or null if not found
     */
    public Card findByCardName(String name) {
        String query = name.trim().toLowerCase();
        for (Card card : CARDS) {
            if (card.getName().toLowerCase().equals(query)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Attempts to add a card to this binder.
     * The specific acceptance criteria (capacity, rarity, etc.) are defined by subclasses.
     *
     * @param card the Card to add
     * @return {@code true} if the card was added; {@code false} if the binder is full or the card is disallowed
     */
    public abstract boolean addCard(Card card);

    /**
     * Determines whether this binder supports selling its contents.
     * return value is defined by the subclasses
     *
     * @return {@code true} if the binder is sellable; {@code false} otherwise
     */
    public abstract boolean isSellable();

    /**
     * Removes and returns all cards from this binder, clearing its contents.
     *
     * @return a new list containing all removed cards
     */
    public ArrayList<Card> removeAllCards() {
        ArrayList<Card> removed = new ArrayList<>(CARDS);
        CARDS.clear();
        return removed;
    }

    /**
     * Removes a specific card by name from this binder.
     *
     * @param name case-insensitive name of the card to remove
     * @return the removed Card instance
     * @throws IllegalStateException if the binder is empty
     * @throws NoSuchElementException if no matching card is found
     */
    public Card removeCardByName(String name) {
        if (CARDS.isEmpty()) {
            throw new IllegalStateException("Binder '" + NAME + "' is empty");
        }
        Card target = findByCardName(name);
        if (target == null) {
            throw new NoSuchElementException(
                    "Card '" + name + "' not found in binder '" + NAME + "'");
        }
        CARDS.remove(target);
        return target;
    }

    /**
     * Returns a shallow copy of the cards in this binder, sorted by card name.
     *
     * @return a new list of cards sorted alphabetically
     */
    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sorted = new ArrayList<>(CARDS);
        sorted.sort(Comparator.comparing(Card::getName));
        return sorted;
    }

    /**
     * Checks whether the binder contains any cards.
     *
     * @return {@code true} if the binder has no cards; {@code false} otherwise
     */
    public boolean isEmpty() {
        return CARDS.isEmpty();
    }

}