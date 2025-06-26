package com.TradingCard;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Binder holds up to a fixed number of cards for trading purposes.
 * <p>
 * Supports adding cards, removing by name or clearing all cards,
 * and retrieving a sorted view of contained cards.
 */
public class Binder {
    private static final int MAX_CAPACITY = 20;  // maximum slots in a binder

    private final String NAME;                  // binder's unique name
    private final ArrayList<Card> CARDS;       // internal list of cards

    /**
     * Constructs a Binder with the given name.
     * @param name non-null, non-blank name for this binder
     * @throws IllegalArgumentException if name is null or blank
     */
    public Binder(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.NAME = name.trim();
        this.CARDS = new ArrayList<>(); // initialize empty card list
    }

    /**
     * @return the name of this binder
     */
    public String getName() {
        return NAME;
    }

    /**
     * Find a card in this binder by name.
     * @param name case-insensitive name to search
     * @return the matching Card, or null if not found
     */
    public Card findByCardName(String name) {
        for (Card card : this.CARDS) {
            if (card.getName().equalsIgnoreCase(name))
                return card;
        }
        return null;
    }

    /**
     * Add a card to this binder if capacity allows.
     * @param card the Card to add
     * @return true if added, false if binder is full
     */
    public boolean addCard(Card card) {
        if (this.CARDS.size() >= MAX_CAPACITY) {
            return false; // full, cannot add
        }
        return this.CARDS.add(card);
    }

    /**
     * Remove and return all cards from this binder.
     * Clears the binder's contents.
     * @return a new list containing all removed cards
     */
    public ArrayList<Card> removeAllCards() {
        ArrayList<Card> cards = new ArrayList<>(this.CARDS);
        this.CARDS.clear(); // empty binder
        return cards;
    }

    /**
     * Remove a specific card by name.
     * @param name case-insensitive name of card to remove
     * @return the removed Card instance
     * @throws IllegalStateException if binder is empty
     * @throws IllegalArgumentException if card not found
     */
    public Card removeCardByName(String name) {
        if (this.CARDS.isEmpty()) {
            throw new IllegalStateException("binder is empty");
        }
        Card target = findByCardName(name);
        if (target == null) {
            throw new IllegalArgumentException("card \"" + name + "\" not found in binder.");
        }
        this.CARDS.remove(target); // remove first match
        return target;
    }

    /**
     * Get a sorted copy of the cards in this binder by card name.
     * @return new list sorted alphabetically
     */
    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sortedCopy = new ArrayList<>(this.CARDS);
        sortedCopy.sort(Comparator.comparing(Card::getName));
        return sortedCopy;
    }
}
