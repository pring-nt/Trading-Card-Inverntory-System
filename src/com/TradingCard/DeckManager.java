package com.TradingCard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class DeckManager {
    private final ArrayList<Deck> DECKS;

    public DeckManager() {
        this.DECKS = new ArrayList<>();
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
     * Creates a new Deck and adds it to the system.
     *
     * @param name the name of the new deck
     * @param sellable true if the deck should be sellable, false otherwise
     * @throws IllegalStateException if a deck with that name already exists
     */
    public void createDeck(String name, boolean sellable) {
        for (Deck d : this.DECKS) {
            if (d.getName().equalsIgnoreCase(name))
                throw new IllegalStateException("deck \"" + name + "\" already exists");
        }
        Deck deck;
        if(sellable) {
            deck = new SellableDeck(name);
        }
        else {
            deck = new Deck(name);
        }
        this.DECKS.add(deck);
    }

    /**
     * Deletes a Deck by name and returns all its cards to the main collection.
     *
     * @param name the name of the deck to delete
     * @return the list of cards removed from the deck
     * @throws NoSuchElementException if no deck with the given name exists
     */
    public ArrayList<Card> deleteDeck(String name) {
        Deck target = findDeckByName(name);
        ArrayList<Card> cards = target.removeAllCards();
        this.DECKS.remove(target);
        return cards;
    }

    /**
     * Removes a card from a deck and returns it to the collection.
     *
     * @param deckName the name of the deck
     * @param cardName the name of the card to remove
     * @return the removed card
     * @throws NoSuchElementException if the deck or card is not found
     */
    public Card removeCardFromDeck(String deckName, String cardName) {
        Deck tDeck = findDeckByName(deckName);
        return tDeck.removeCardByName(cardName);
    }

    /**
     * Moves a card from the collection into a deck.
     *
     * @param deckName the name of the deck
     * @param card the card to add
     * @return null if added successfully; the card itself if adding failed
     * @throws IllegalStateException if the deck is full or the card is a duplicate
     */
    public Card addCardToDeck(String deckName, Card card) {
        Deck tDeck = findDeckByName(deckName);
        if (!tDeck.addCard(card)) {
            return card;
        }
        return null;
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
     * Sells a deck by name.
     *
     * @param name the name of the deck to sell
     * @return the total sale price, including any handling fee
     * @throws NoSuchElementException if no deck with that name exists
     * @throws IllegalStateException if the deck is not sellable
     */
    public BigDecimal sellDeck(String name) {
        Deck deck = findDeckByName(name);

        if (!deck.isSellable()) {
            throw new IllegalStateException("deck \"" + name + "\" cannot be sold");
        }

        BigDecimal price = ((SellableDeck) deck).sell();
        DECKS.remove(deck);
        return price;
    }

    /**
     * Gets the current value of a deck without selling it.
     *
     * @param name the name of the deck
     * @return the deck's estimated value
     * @throws NoSuchElementException if no deck with that name exists
     * @throws IllegalStateException if the deck is not sellable
     */
    public BigDecimal getDeckValue(String name) {
        Deck deck = findDeckByName(name);

        if (!deck.isSellable()) {
            throw new IllegalStateException("deck \"" + name + "\" cannot be sold");
        }

        return ((Sellable) deck).getValue();
    }

    /**
     * Checks if the deck with the given name is sellable.
     *
     * <p>This delegates to the deck's {@code isSellable()} method, which indicates
     * whether the deck supports being sold through the inventory system.</p>
     *
     * @param name the name of the deck to check
     * @return {@code true} if the deck is sellable; {@code false} otherwise
     * @throws NoSuchElementException if no deck with the given name exists
     */
    public boolean isDeckSellable(String name) {
        Deck deck = findDeckByName(name);
        return deck.isSellable();
    }

    /**
     * Checks if the deck with the given name is empty
     *
     * <p>This delegates to the deck's {@code isEmpty()} method, which indicates
     * whether the deck supports being sold through the inventory system.</p>
     *
     * @param name the name of the deck to check
     * @return {@code true} if the deck is empty; {@code false} otherwise
     * @throws NoSuchElementException if no deck with the given name exists
     */
    public boolean isDeckEmpty(String name) {
        Deck deck = findDeckByName(name);
        return deck.isEmpty();
    }
}
