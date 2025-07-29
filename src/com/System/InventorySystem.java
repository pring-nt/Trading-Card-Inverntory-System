package com.System;

import com.TradingCard.*;
import com.TradingCard.Enums.BinderType;

import java.math.BigDecimal;
import java.util.*;

/**
 * InventorySystem serves as the core model for the Trading Card Inventory System (TCIS).
 * <p>
 * It manages a collection of cards, multiple decks, and multiple binders. All business logic
 * for adding, removing, and trading cards flows through this class.
 */
public class InventorySystem {
    private final CardCollection CARD_COLLECTION;
    private final ArrayList<Deck> DECKS;
    private final BinderManager BINDER_MANAGER;
    private BigDecimal collectorEarnings;

    /**
     * Constructs a new InventorySystem with empty collection, decks, and binders.
     */
    public InventorySystem() {
        this.CARD_COLLECTION = new CardCollection(); // primary card collection
        this.DECKS = new ArrayList<>();             // list of decks
        this.BINDER_MANAGER = new BinderManager();  // binder manager
    }

    /**
     * Retrieves the underlying CardCollection model.
     * @return the CardCollection instance
     */
    public CardCollection getCardCollection() {
        return CARD_COLLECTION;
    }

    /**
     * Find a card by name in the collection without modifying it.
     * @param name name of card to search (case-insensitive, trimmed)
     * @return the Card if found, or null
     */
    public Card findCardByNameInCollection(String name) {
        return this.CARD_COLLECTION.findByCardName(name);
    }

    /**
     * Helper to return a list of cards back into the collection.
     * @param cards list of Card instances to return
     */
    private void returnCardsToCollection(ArrayList<Card> cards) {
        for (Card card : cards) {
            if (card != null) {
                addCardToCollection(card);
            }
        }
    }

    /**
     * Find a Binder by its name.
     * @param name name of the binder to find
     * @return the Binder instance
     * @throws NoSuchElementException if no binder with that name exists
     */
    public Binder findBinderByName(String name) {
        return BINDER_MANAGER.findBinderByName(name);
    }

    /**
     * Find a Deck by its name.
     * @param name name of the deck to find
     * @return the Deck instance
     * @throws NoSuchElementException if no deck with that name exists
     */
    public Deck findDeckByName(String name) {
        for (Deck deck : this.DECKS) {
            if (deck.getName().equalsIgnoreCase(name))
                return deck;
        }
        throw new NoSuchElementException("deck \"" + name + "\" not found");
    }

    /**
     * Create a new Binder and add it to the system.
     * @param name name for the new binder
     * @param type type of binder being created
     * @throws IllegalStateException if a binder with that name already exists
     */
    public void createBinder(String name, BinderType type) {
        BINDER_MANAGER.createBinder(name, type);
    }

    /**
     * Delete a Binder, returning all its cards to the main collection.
     * @param name name of the binder to delete
     * @throws NoSuchElementException if binder not found
     */
    public void deleteBinder(String name) {
        returnCardsToCollection(BINDER_MANAGER.deleteBinder(name));
    }

    /**
     * Create a new Deck and add it to the system.
     * @param name name for the new deck
     * @throws IllegalStateException if a deck with that name already exists
     */
    public void createDeck(String name) {
        for (Deck d : this.DECKS) {
            if (d.getName().equalsIgnoreCase(name))
                throw new IllegalStateException("deck \"" + name + "\" already exists");
        }
        Deck deck = new Deck(name);
        this.DECKS.add(deck);
    }

    /**
     * Delete a Deck, returning all its cards to the main collection.
     * @param name name of the deck to delete
     * @throws NoSuchElementException if deck not found
     */
    public void deleteDeck(String name) {
        Deck target = findDeckByName(name);
        returnCardsToCollection(target.removeAllCards());
        this.DECKS.remove(target);
    }

    /**
     * Remove a single card from a binder and return it to the collection.
     * @param binderName name of binder to remove from
     * @param cardName name of card to remove
     */
    public void removeCardFromBinder(String binderName, String cardName) {
        Card tCard = BINDER_MANAGER.removeCardFromBinder(binderName, cardName);
        addCardToCollection(tCard);
    }

    /**
     * Remove a card from the collection and add it into a binder slot.
     * @param binderName name of binder
     * @param cardName name of card to move
     * @throws IllegalStateException if binder is full (and rolls back)
     */
    public void addCardToBinder(String binderName, String cardName) {
        Card tCard = removeSingleCardFromCollection(cardName);
        Card returnValue = BINDER_MANAGER.addCardToBinder(binderName, tCard);
        if(returnValue != null) {
            addCardToCollection(returnValue);
            throw new IllegalStateException("unable to add to binder because it is full");
        }
    }

    /**
     * Remove a card from a deck and return it to the collection.
     * @param deckName name of deck
     * @param cardName name of card to remove
     */
    public void deleteCardFromDeck(String deckName, String cardName) {
        Deck tDeck = findDeckByName(deckName);
        Card tCard = tDeck.removeCardByName(cardName);
        addCardToCollection(tCard);
    }

    /**
     * Remove a card from collection and add it into a deck slot.
     * @param deckName name of deck
     * @param cardName name of card to move
     * @throws IllegalStateException if deck is full or duplicate (and rolls back)
     */
    public void addCardToDeck(String deckName, String cardName) {
        Deck tDeck = findDeckByName(deckName);
        Card tCard = removeSingleCardFromCollection(cardName);
        if (!tDeck.addCard(tCard)) {
            addCardToCollection(tCard);
            throw new IllegalStateException("unable to add to deck (full or duplicate)");
        }
    }

    /**
     * Trade an outgoing card from a binder for an incoming one.
     * @param binderName name of binder to trade in
     * @param outgoingName name of card to remove
     * @param incomingCard Card to add from external source
     * @param force if true, skip the $1 value difference check
     * @return true if trade completed, false if cancelled due to value diff
     */
    public boolean tradeCard(String binderName, String outgoingName, Card incomingCard, boolean force) {
        Binder tBinder = findBinderByName(binderName);
        if (tBinder instanceof Sellable) {
            throw new IllegalStateException("Binder \"" + binderName + "\" cannot be used for trading");
        }

        Card outgoingCard = tBinder.removeCardByName(outgoingName);
        addCardToCollection(incomingCard);

        BigDecimal diff = incomingCard.getValue().subtract(outgoingCard.getValue()).abs();

        if (diff.compareTo(BigDecimal.ONE) >= 0 && !force) {
            removeSingleCardFromCollection(incomingCard.getName());
            tBinder.addCard(outgoingCard);
            return false;
        }

        Card tradeCard = removeSingleCardFromCollection(incomingCard.getName());
        tBinder.addCard(tradeCard);
        return true;
    }

    /**
     * Retrieve names of all binders in the system.
     * @return list of binder names
     */
    public ArrayList<String> getBinderNames() {
        return BINDER_MANAGER.getBinderNames();
    }

    /**
     * Retrieve names of all decks in the system.
     * @return list of deck names
     */
    public ArrayList<String> getDeckNames() {
        ArrayList<String> deckNames = new ArrayList<>();
        for (Deck deck : this.DECKS) {
            deckNames.add(deck.getName());
        }
        return deckNames;
    }

    /**
     * Add a card directly into the collection.
     * @param c the Card to add
     */
    public void addCardToCollection(Card c) {
        this.CARD_COLLECTION.addCard(c);
    }

    /**
     * Remove and return a single card from the collection.
     * @param name name of card to remove
     * @return the removed Card instance
     */
    public Card removeSingleCardFromCollection(String name) {
        return this.CARD_COLLECTION.removeCardByName(name);
    }

    /**
     * Increment the count of a card in the collection by name.
     * @param name name of card to increment
     */
    public void incrementCardInCollection(String name) {
        this.CARD_COLLECTION.incrementCard(name);
    }

    /**
     * Decrement the count of a card in the collection by name.
     * @param name name of card to decrement
     */
    public void decrementCardInCollection(String name) {
        this.CARD_COLLECTION.decrementCard(name);
    }
}
