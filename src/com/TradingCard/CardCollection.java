package com.TradingCard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Manages the main pool of trading cards, tracking unique card attributes and copy counts.
 * <p>
 * Supports adding, removing, searching, selling, and adjusting counts of cards.
 */
public class CardCollection {
    /**
     * The list of all cards managed by this component.
     */
    private final ArrayList<Card> CARDS;


    /**
     * Constructs an empty CardCollection.
     */
    public CardCollection() {
        this.CARDS = new ArrayList<>();
    }

    /**
     * Adds a card to the collection.
     * <p>
     * If an identical card (same name, rarity, variation) already exists,
     * increments its count.  Otherwise, adds the new card instance.
     *
     * @param c the Card to add or increment
     * @throws IllegalArgumentException if a card with the same name but different attributes exists
     */
    public void addCard(Card c) {
        Card existing = findByCardName(c.getName());
        if (existing == null) {
            CARDS.add(c);
        } else if (existing.equals(c)) {
            existing.incrementCount();
        } else {
            throw new IllegalArgumentException(
                    "Card with name '" + c.getName() + "' has different attributes and cannot be merged.");
        }
    }

    /**
     * Removes one copy of a named card from the collection and returns a copy of it.
     *
     * @param name the name of the card to remove (case-insensitive, trimmed)
     * @return a new Card instance representing the removed copy (count = 1)
     * @throws NoSuchElementException  if no card with the given name exists
     * @throws IllegalStateException   if the collection is empty or no copies remain
     */
    public Card removeCardByName(String name) {
        if (CARDS.isEmpty()) {
            throw new IllegalStateException("Collection is empty.");
        }
        Card target = findByCardName(name);
        if (target == null) {
            throw new NoSuchElementException("Card '" + name + "' not found in collection.");
        }
        if (target.getCount() == 0) {
            throw new IllegalStateException("No copies left of card '" + name + "'.");
        }
        Card copy = Card.copyCard(target);
        target.decrementCount();
        return copy;
    }

    /**
     * Sells one copy of a named card, reducing its count by one and returning its value.
     *
     * @param name the name of the card to sell
     * @return the sale price based on the card's current value
     * @throws NoSuchElementException if no card with the given name exists
     */
    public BigDecimal sellCardByName(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new NoSuchElementException("Card '" + name + "' not found in collection.");
        }
        return card.sell();
    }

    /**
     * Searches for a card by name.
     *
     * @param name the name of the card to search (case-insensitive, trimmed)
     * @return the matching Card instance, or null if not found
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
     * Increments the count of a named card in the collection.
     *
     * @param name the name of the card to increment
     * @throws NoSuchElementException if no card with the given name exists
     */
    public void incrementCard(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new NoSuchElementException("Card '" + name + "' not found in collection.");
        }
        card.incrementCount();
    }

    /**
     * Decrements the count of a named card, not allowing it to go below zero.
     *
     * @param name the name of the card to decrement
     * @throws NoSuchElementException if no card with the given name exists
     * @throws IllegalStateException  if the card's count is already zero
     */
    public void decrementCard(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new NoSuchElementException("Card '" + name + "' not found in collection.");
        }
        if (card.getCount() == 0) {
            throw new IllegalStateException("Card '" + name + "' count is already zero.");
        }
        card.decrementCount();
    }

    /**
     * Returns a shallow copy of the internal card list, sorted by card name.
     *
     * @return a new ArrayList containing all cards, sorted alphabetically by name
     */
    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sortedCopy = new ArrayList<>(CARDS);
        sortedCopy.sort(Comparator.comparing(Card::getName));
        return sortedCopy;
    }

    /**
     * Gets the value of a specific card
     *
     * @param name the name of the card
     * @return the card's value
     * @throws NoSuchElementException if no card with that name exists
     */
    public BigDecimal getCardValue(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new NoSuchElementException("Card '" + name + "' not found in collection");
        }
        return card.getValue();
    }
}
