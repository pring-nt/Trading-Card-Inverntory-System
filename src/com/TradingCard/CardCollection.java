package com.TradingCard;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * CardCollection manages the main pool of trading cards.
 * <p>
 * Supports adding, removing, searching, and adjusting counts of cards,
 * while preserving unique card attributes and copy counts.
 */
public class CardCollection {
    private final ArrayList<Card> CARDS;

    /**
     * Constructs an empty CardCollection.
     */
    public CardCollection(){
        this.CARDS = new ArrayList<>(); // initialize list storage
    }

    /**
     * Add a card or increment count if identical card exists.
     * @param c Card to add or increment
     * @throws IllegalArgumentException if a card with same name but different attributes exists
     */
    public void addCard(Card c){
        Card existing = findByCardName(c.getName());
        if (existing == null) {
            CARDS.add(c); // new unique card
        } else if (existing.equals(c)) {
            existing.incrementCount(); // same card, increase count
        } else {
            throw new IllegalArgumentException("card with same name but different attributes exists.");
        }
    }

    /**
     * Remove one copy of a named card, returning a copy.
     * @param name name of card to remove (case-insensitive, trimmed)
     * @return a new Card instance representing the removed copy
     * @throws IllegalStateException if collection empty or no copies left
     * @throws NoSuchElementException if card not found
     */
    public Card removeCardByName(String name) {
        if (CARDS.isEmpty()) {
            throw new IllegalStateException("collection is empty!");
        }
        Card target = findByCardName(name);
        if (target == null) {
            throw new NoSuchElementException("card \"" + name + "\" not found in collection!");
        }
        if (target.getCount() == 0) {
            throw new IllegalStateException("no copies left of the requested card.");
        }
        Card copy = Card.copyCard(target); // shallow copy, count=1
        target.decrementCount();          // reduce stored count
        return copy;
    }

    /**
     * Search for a card by name.
     * @param name card name to search (case-insensitive, trimmed)
     * @return the matching Card or null if absent
     */
    public Card findByCardName(String name) {
        String query = name.trim().toLowerCase();
        for (Card card : CARDS) {
            if (card.getName().trim().toLowerCase().equals(query)) {
                return card;
            }
        }
        return null; // not found
    }

    /**
     * Increment the count of a named card in the collection.
     * @param name card name to increment
     * @throws NoSuchElementException if card not found
     */
    public void incrementCard(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new NoSuchElementException("card \"" + name + "\" not found in collection!");
        }
        card.incrementCount();
    }

    /**
     * Decrement the count of a named card, not below zero.
     * @param name card name to decrement
     * @throws NoSuchElementException if card not found
     * @throws IllegalStateException if count already at zero
     */
    public void decrementCard(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new NoSuchElementException("card \"" + name + "\" not found in collection!");
        }
        if (card.getCount() > 0) {
            card.decrementCount();
        } else throw new IllegalStateException("card count is already at 0!");
    }

    /**
     * Obtain a sorted shallow copy of all cards by name.
     * @return sorted ArrayList of Card references
     */
    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sortedCopy = new ArrayList<>(this.CARDS);
        sortedCopy.sort(Comparator.comparing(Card::getName));
        return sortedCopy;
    }
}
