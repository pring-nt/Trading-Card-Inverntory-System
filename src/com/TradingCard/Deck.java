package com.TradingCard;

import java.util.ArrayList;

/**
 * Deck holds up to a fixed number of unique cards for gameplay.
 * <p>
 * Supports adding, removing, and listing cards, with capacity and duplicate
 * prevention logic enforced.
 */
public class Deck {
    private static final int MAX_CAPACITY = 10; // max cards in a deck

    private final String NAME;                // deck name identifier
    private final ArrayList<Card> CARDS;     // internal list of cards

    /**
     * Constructs a Deck with the specified name.
     * @param name non-null, non-blank name
     * @throws IllegalArgumentException if name is null or blank
     */
    public Deck(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.NAME = name.trim();
        this.CARDS = new ArrayList<>();
    }

    /**
     * @return the deck's name
     */
    public String getName() {
        return NAME;
    }

    /**
     * Retrieves a card by its position in deck.
     * @param index zero-based index of card
     * @return the Card at given index
     * @throws IndexOutOfBoundsException if index invalid
     */
    public Card getCardAtIndex(int index) {
        if (index < 0 || index >= CARDS.size())
            throw new IndexOutOfBoundsException("invalid card index: " + index);
        return this.CARDS.get(index);
    }

    /**
     * Find a card in the deck by name.
     * @param name case-insensitive card name
     * @return the matching Card or null if absent
     */
    public Card findByCardName(String name) {
        for (Card card : this.CARDS) {
            if (card.getName().equalsIgnoreCase(name)) return card;
        }
        return null; // not found
    }

    /**
     * Attempts to add a card to the deck.
     * @param c Card to add
     * @return true if added, false if full or duplicate
     * @throws IllegalArgumentException if same name but different attributes exists
     */
    public boolean addCard(Card c) {
        if (this.CARDS.size() >= MAX_CAPACITY) {
            return false; // deck full
        }
        Card existing = findByCardName(c.getName());
        if (existing != null) {
            if (existing.equals(c)) {
                return false; // duplicate
            } else {
                throw new IllegalArgumentException("a different card with the same name already exists in the deck.");
            }
        }
        this.CARDS.add(c);
        return true;
    }

    /**
     * Remove and return all cards, clearing the deck.
     * @return list of removed cards
     */
    public ArrayList<Card> removeAllCards() {
        ArrayList<Card> cards = new ArrayList<>(this.CARDS);
        this.CARDS.clear();
        return cards;
    }

    /**
     * Remove a specific card by name.
     * @param name case-insensitive name of card
     * @return the removed Card
     * @throws IllegalStateException if deck empty
     * @throws IllegalArgumentException if card not found
     */
    public Card removeCardByName(String name) {
        if (this.CARDS.isEmpty()) { // Deck is empty
            throw new IllegalStateException("deck is empty");
        }
        Card target = findByCardName(name);
        if (target == null) { // not present
            throw new IllegalArgumentException("card \"" + name + "\" not found in deck.");
        }
        this.CARDS.remove(target);
        return target;
    }

    /**
     * @return a shallow copy of current cards (unsorted)
     */
    public ArrayList<Card> getCopyOfCards() {
        return new ArrayList<>(this.CARDS);
    }
}
