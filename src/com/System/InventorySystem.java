package com.System;

import com.TradingCard.*;
import com.TradingCard.Enums.BinderType;
import com.TradingCard.Enums.Rarity;
import com.TradingCard.Enums.Variation;

import java.math.BigDecimal;
import java.util.*;

/**
 * InventorySystem serves as the core model for the Trading Card Inventory System (TCIS).
 * <p>
 * It manages a collection of cards, multiple decks, and multiple binders.
 * All business logic for adding, removing, trading, and selling cards flows through this class.
 */
public class InventorySystem {
    /**
     * The main collection of cards managed by the system.
     */
    protected final CardCollection CARD_COLLECTION;

    /**
     * Manager responsible for creating and managing decks.
     */
    protected final DeckManager DECK_MANAGER;

    /**
     * Manager responsible for creating and managing binders.
     */
    protected final BinderManager BINDER_MANAGER;

    /**
     * Constructs a new InventorySystem with empty collection, decks, and binders.
     */
    public InventorySystem() {
        this.CARD_COLLECTION = new CardCollection(); // primary card collection
        this.DECK_MANAGER = new DeckManager();       // deck manager containing a list of decks
        this.BINDER_MANAGER = new BinderManager();   // binder manager containing a list of binders
    }

    /**
     * Retrieves the underlying CardCollection.
     *
     * @return the CardCollection instance
     */
    public CardCollection getCardCollection() {
        return CARD_COLLECTION;
    }

    /**
     * Finds a card in the collection by name without modifying it.
     *
     * @param name name of the card to search (case-insensitive, trimmed)
     * @return the Card if found, or null if not present
     */
    public Card findCardByNameInCollection(String name) {
        return this.CARD_COLLECTION.findByCardName(name);
    }

    /**
     * Helper function that returns a list of cards back into the main collection.
     * Ignores any null entries in the provided list.
     *
     * @param cards list of Card instances to return
     */
    protected void returnCardsToCollection(ArrayList<Card> cards) {
        for (Card card : cards) {
            if (card != null) {
                addCardToCollection(card);
            }
        }
    }

    /**
     * Finds a Binder by its name.
     *
     * @param name name of the binder to find
     * @return the Binder instance
     * @throws NoSuchElementException if no binder with that name exists
     */
    public Binder findBinderByName(String name) {
        return BINDER_MANAGER.findBinderByName(name);
    }

    /**
     * Finds a Deck by its name.
     *
     * @param name name of the deck to find
     * @return the Deck instance
     * @throws NoSuchElementException if no deck with that name exists
     */
    public Deck findDeckByName(String name) {
        return DECK_MANAGER.findDeckByName(name);
    }

    /**
     * Creates a new Binder of the specified type and adds it to the system.
     *
     * @param name the name for the new binder
     * @param type the type of binder to create
     * @throws IllegalStateException if a binder with the same name already exists
     */
    public void createBinder(String name, BinderType type) {
        BINDER_MANAGER.createBinder(name, type);
    }

    /**
     * Deletes a Binder by name and returns its cards to the main collection.
     *
     * @param name name of the binder to delete
     * @throws NoSuchElementException if no binder with that name exists
     */
    public void deleteBinder(String name) {
        returnCardsToCollection(BINDER_MANAGER.deleteBinder(name));
    }

    /**
     * Creates a new Deck and adds it to the system.
     *
     * @param name the name for the new deck
     * @param sellable true to create a sellable deck, false otherwise
     * @throws IllegalStateException if a deck with the same name already exists
     */
    public void createDeck(String name, boolean sellable) {
        DECK_MANAGER.createDeck(name, sellable);
    }

    /**
     * Deletes a Deck by name and returns its cards to the main collection.
     *
     * @param name name of the deck to delete
     * @throws NoSuchElementException if no deck with that name exists
     */
    public void deleteDeck(String name) {
        returnCardsToCollection(DECK_MANAGER.deleteDeck(name));
    }

    /**
     * Removes a single card from a binder and returns it to the collection.
     *
     * @param binderName name of the binder to remove from
     * @param cardName name of the card to remove
     * @throws NoSuchElementException if the binder or card is not found
     */
    public void removeCardFromBinder(String binderName, String cardName) {
        Card tCard = BINDER_MANAGER.removeCardFromBinder(binderName, cardName);
        addCardToCollection(tCard);
    }

    /**
     * Moves a card from the collection into a binder slot.
     * rolls back if the binder is full or the card type is invalid.
     *
     * @param binderName name of the binder
     * @param cardName name of the card to move
     * @throws NoSuchElementException if the card or binder does not exist
     * @throws IllegalStateException if the binder cannot accept the card
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
     * Removes a card from a deck and returns it to the collection.
     *
     * @param deckName name of the deck to remove from
     * @param cardName name of the card to remove
     * @throws NoSuchElementException if the deck or card is not found
     */
    public void removeCardFromDeck(String deckName, String cardName) {
        Card tCard = DECK_MANAGER.removeCardFromDeck(deckName, cardName);
        addCardToCollection(tCard);
    }

    /**
     * Moves a card from the collection into a deck slot.
     * rolls back if the deck is full or contains a duplicate.
     *
     * @param deckName name of the deck
     * @param cardName name of the card to move
     * @throws NoSuchElementException if the card or deck does not exist
     * @throws IllegalStateException if the deck cannot accept the card
     */
    public void addCardToDeck(String deckName, String cardName) {
        Card tCard = removeSingleCardFromCollection(cardName);
        Card returnValue = DECK_MANAGER.addCardToDeck(deckName, tCard);
        if (returnValue != null) {
            addCardToCollection(tCard);
            throw new IllegalStateException("unable to add to deck (full or duplicate)");
        }
    }

    /**
     * Trades an outgoing card from a binder for an incoming one.
     *
     * @param binderName    name of the binder to trade in
     * @param outgoingName  name of the card to remove
     * @param incomingCard  the Card to add from external source
     * @param force         if true, skip the $1 value difference check
     * @return true if trade completed, false if cancelled due to value difference
     * @throws NoSuchElementException if the binder or card is not found
     * @throws IllegalStateException    if the binder can not be used in trading or ingoing card is rejected by binder
     */
    public boolean tradeCard(String binderName, String outgoingName, Card incomingCard, boolean force) {
        // Locate and validate binder
        Binder tBinder = findBinderByName(binderName);
        if (tBinder instanceof Sellable) {
            throw new IllegalStateException("Binder \"" + binderName + "\" cannot be used for trading");
        }

        // Remove outgoing card
        Card outgoingCard = tBinder.removeCardByName(outgoingName);
        // Add incoming card temporarily to collection
        addCardToCollection(incomingCard);

        // Value difference check
        BigDecimal diff = incomingCard.getValue().subtract(outgoingCard.getValue()).abs();
        if (diff.compareTo(BigDecimal.ONE) >= 0 && !force) {
            // Rollback
            removeSingleCardFromCollection(incomingCard.getName());
            tBinder.addCard(outgoingCard);
            return false;
        }

        // Perform trade: remove from collection and attempt to add
        Card toTrade = removeSingleCardFromCollection(incomingCard.getName());
        if (!tBinder.addCard(toTrade)) {
            // Rollback on add failure
            tBinder.addCard(outgoingCard);
            throw new IllegalStateException("Incoming card \"" + toTrade.getName() + "\" is not allowed in binder \"" + binderName + "\"");
        }
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
        return DECK_MANAGER.getDeckNames();
    }

    /**
     * Add a card directly into the collection.
     * @param c the Card to add
     */
    public void addCardToCollection(Card c) {
        this.CARD_COLLECTION.addCard(c);
    }

    /**
     * Returns the value of a specific card in the collection.
     *
     * @param name the name of the card
     * @return the monetary value of the card
     * @throws NoSuchElementException if the card does not exist
     */
    public BigDecimal getCardValue(String name) {
        return CARD_COLLECTION.getCardValue(name);
    }


    /**
     * Remove and return a single card from the collection.
     * @param name name of card to remove
     * @return the removed Card instance
     * @throws NoSuchElementException  if no card with the given name exists
     * @throws IllegalStateException   if the collection is empty or no copies remain
     */
    public Card removeSingleCardFromCollection(String name) {
        return this.CARD_COLLECTION.removeCardByName(name);
    }

    /**
     * Increment the count of a card in the collection by name.
     * @param name name of card to increment
     * @throws NoSuchElementException if no card with the given name exists
     */
    public void incrementCardInCollection(String name) {
        this.CARD_COLLECTION.incrementCard(name);
    }

    /**
     * Decrement the count of a card in the collection by name.
     * @throws NoSuchElementException if no card with the given name exists
     * @throws IllegalStateException  if the card's count is already zero
     */
    public void decrementCardInCollection(String name) {
        this.CARD_COLLECTION.decrementCard(name);
    }
    /**
     * Returns a list of all rarity enum names as strings.
     *
     * @return list of rarity names
     */
    public ArrayList<String> getRarityNames() {
        ArrayList<String> rarities = new ArrayList<>();
        for (Rarity rarity : Rarity.values()) {
            rarities.add(rarity.name());
        }
        return rarities;
    }

    /**
     * Returns a list of all variation enum names as strings.
     *
     * @return list of variation names
     */
    public ArrayList<String> getVariationNames() {
        ArrayList<String> variations = new ArrayList<>();
        for (Variation variation : Variation.values()) {
            variations.add(variation.name());
        }
        return variations;
    }

    /**
     * Returns a list of all binder type enum names as strings.
     *
     * @return list of binder type names
     */
    public ArrayList<String> getBinderTypes() {
        ArrayList<String> binderTypes = new ArrayList<>();
        for (BinderType binderType : BinderType.values()) {
            binderTypes.add(binderType.name());
        }
        return binderTypes;
    }

    /**
     * Checks whether the binder with the given name is empty.
     *
     * <p>This delegates the check to the {@code BINDER_MANAGER}, which retrieves the binder
     * and checks if it contains any cards.</p>
     *
     * @param name the name of the binder to check
     * @return {@code true} if the binder exists and has no cards; {@code false} otherwise
     * @throws NoSuchElementException if no binder with the given name exists
     */
    public boolean isBinderEmpty(String name) {
        return BINDER_MANAGER.isBinderEmpty(name);
    }

    /**
     * Checks whether the deck with the given name is empty.
     *
     * <p>This delegates the check to the {@code DECK_MANAGER}, which retrieves the deck
     * and checks if it contains any cards.</p>
     *
     * @param name the name of the deck to check
     * @return {@code true} if the deck exists and has no cards; {@code false} otherwise
     * @throws NoSuchElementException if no deck with the given name exists
     */
    public boolean isDeckEmpty(String name) {
        return DECK_MANAGER.isDeckEmpty(name);
    }
}
