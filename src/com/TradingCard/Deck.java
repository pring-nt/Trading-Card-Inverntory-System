package com.TradingCard;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Represents a deck of up to 10 unique cards for gameplay.
 * <p>
 * Supports adding cards (preventing duplicates and enforcing capacity),
 * removing cards by name or index, and listing current cards.
 */
public class Deck {
    private static final int MAX_CAPACITY = 10; // maximum cards in a deck

    private final String NAME;                // deck's identifier
    protected final ArrayList<Card> CARDS;    // internal storage of cards

    /**
     * Constructs a Deck with the specified name.
     *
     * @param name non-null, non-blank name for this deck
     * @throws IllegalArgumentException if name is null or blank
     */
    public Deck(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Deck name cannot be null or blank");
        }
        this.NAME = name.trim();
        this.CARDS = new ArrayList<>();
    }

    /**
     * Retrieves the deck's name.
     *
     * @return the name of this deck
     */
    public String getName() {
        return NAME;
    }

    /**
     * Retrieves a card by its position in the deck.
     *
     * @param index zero-based index of the card
     * @return the Card at the given index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Card getCardAtIndex(int index) {
        if (index < 0 || index >= CARDS.size()) {
            throw new IndexOutOfBoundsException("Invalid card index: " + index);
        }
        return CARDS.get(index);
    }

    /**
     * Searches for a card in the deck by name.
     *
     * @param name case-insensitive card name to search
     * @return the matching Card, or null if not present
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
     * Attempts to add a card to the deck.
     * <p>
     * Prevents duplicates and enforces maximum capacity. Throws if name collision with different attributes.
     *
     * @param c the Card to add
     * @return {@code true} if added; {@code false} if deck is full or card is an identical duplicate
     * @throws IllegalArgumentException if a different card with the same name already exists
     */
    public boolean addCard(Card c) {
        if (CARDS.size() >= MAX_CAPACITY) {
            return false; // deck is full
        }
        Card existing = findByCardName(c.getName());
        if (existing != null) {
            if (existing.equals(c)) {
                return false; // duplicate card
            } else {
                throw new IllegalArgumentException(
                        "A different card with name '" + c.getName() + "' already exists in the deck");
            }
        }
        CARDS.add(c);
        return true;
    }

    /**
     * Removes and returns all cards from the deck, clearing its contents.
     *
     * @return a new list containing the removed cards
     */
    public ArrayList<Card> removeAllCards() {
        ArrayList<Card> removed = new ArrayList<>(CARDS);
        CARDS.clear();
        return removed;
    }

    /**
     * Removes a specific card by name from the deck.
     *
     * @param name case-insensitive name of the card to remove
     * @return the removed Card instance
     * @throws IllegalStateException    if the deck is empty
     * @throws NoSuchElementException   if no matching card is found
     */
    public Card removeCardByName(String name) {
        if (CARDS.isEmpty()) {
            throw new IllegalStateException("Deck '" + NAME + "' is empty");
        }
        Card target = findByCardName(name);
        if (target == null) {
            throw new NoSuchElementException(
                    "Card '" + name + "' not found in deck '" + NAME + "'");
        }
        CARDS.remove(target);
        return target;
    }

    /**
     * Provides a shallow copy of current cards in insertion order.
     *
     * @return a new list of cards in this deck
     */
    public ArrayList<Card> getCopyOfCards() {
        return new ArrayList<>(CARDS);
    }

    /**
     * Indicates that this deck can not be sold.
     *
     * @return {@code false} always, as non-sellable decks cannot be sold
     */
    public boolean isSellable() {
        return true;
    }

    /**
     * Checks whether the deck contains any cards.
     *
     * @return {@code true} if the deck has no cards; {@code false} otherwise
     */
    public boolean isEmpty() {
        return CARDS.isEmpty();
    }
}
