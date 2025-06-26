package com.System;
import com.TradingCard.*;
import java.math.BigDecimal;
import java.util.*;

public class InventorySystem {
    private final CardCollection CARD_COLLECTION;
    private final ArrayList<Deck> DECKS;
    private final ArrayList<Binder> BINDERS;

    public InventorySystem() {
        this.CARD_COLLECTION = new CardCollection();
        this.DECKS = new ArrayList<>();
        this.BINDERS = new ArrayList<>();
    }

    public CardCollection getCardCollection() {
        return CARD_COLLECTION;
    }

    public Card findCardByNameInCollection(String name) {
        return this.CARD_COLLECTION.findByCardName(name);
    }

    private void returnCardsToCollection(ArrayList<Card> cards) {
        for (Card card : cards) {
            if (card != null) {
                addCardToCollection(card);
            }
        }
    }

    public Binder findBinderByName(String name) {
        for (Binder binder: this.BINDERS) {
            if (binder.getName().equalsIgnoreCase(name))
                return binder;
        }
        throw new NoSuchElementException("binder \"" + name + "\" not found");
    }

    public Deck findDeckByName(String name) {
        for (Deck deck: this.DECKS) {
            if (deck.getName().equalsIgnoreCase(name))
                return deck;
        }
        throw new NoSuchElementException("deck \"" + name + "\" not found");
    }

    public void createBinder(String name) {
        for (Binder b : this.BINDERS) {
            if (b.getName().equalsIgnoreCase(name))
                throw new IllegalStateException("binder \"" + name + "\" already exists");
        }
        Binder binder = new Binder(name);
        this.BINDERS.add(binder);
    }

    public void deleteBinder(String name) {
        Binder target = findBinderByName(name);
        returnCardsToCollection(target.removeAllCards());
        this.BINDERS.remove(target);
    }

    public void createDeck(String name) {
        for (Deck d : this.DECKS) {
            if (d.getName().equalsIgnoreCase(name))
                throw new IllegalStateException("deck \"" + name + "\" already exists");
        }
        Deck deck = new Deck(name);
        this.DECKS.add(deck);
    }

    public void deleteDeck(String name) {
        Deck target = findDeckByName(name);
        returnCardsToCollection(target.removeAllCards());
        this.DECKS.remove(target);
    }

    public void removeCardFromBinder(String binderName, String cardName) {
        Binder tBinder = findBinderByName(binderName); // throws on error
        Card tCard = tBinder.removeCardByName(cardName); // throws on error
        addCardToCollection(tCard);
    }

    public void addCardToBinder(String binderName, String cardName) {
        Binder tBinder = findBinderByName(binderName); // throws on error
        Card tCard = removeCardFromCollection(cardName); // throws on error
        if(!tBinder.addCard(tCard)) {
            // rollback extraction
            addCardToCollection(tCard);
            throw new IllegalStateException("unable to add to binder because it is full");
        }
    }

    public void deleteCardFromDeck(String deckName, String cardName) {
        Deck tDeck = findDeckByName(deckName); // throws on error
        Card tCard = tDeck.removeCardByName(cardName); // throws on error
        addCardToCollection(tCard);
    }

    public void addCardToDeck(String deckName, String cardName) {
        Deck tDeck = findDeckByName(deckName);
        Card tCard = removeCardFromCollection(cardName);  // throws on error
        if (!tDeck.addCard(tCard)) {
            // rollback
            addCardToCollection(tCard);
            throw new IllegalStateException("unable to add to deck (full or duplicate)");
        }
    }

    public boolean tradeCard(String binderName, String outgoingName, Card incomingCard, boolean force) {
        Binder tBinder = findBinderByName(binderName); // find the binder that would contain the outgoing card
        Card outgoingCard = tBinder.removeCardByName(outgoingName); // find the outgoing card and remove it from binder
        addCardToCollection(incomingCard); // Store the ingoing card in collection

        BigDecimal diff = incomingCard.getValue().subtract(outgoingCard.getValue()).abs();
        if (diff.compareTo(BigDecimal.ONE) >= 0 && !force) { // Compare the difference to 1 and if trade is not forced
            // Cancel
            removeCardFromCollection(incomingCard.getName()); //Roll back
            tBinder.addCard(outgoingCard);
            return false; // return false to prompt user if they want to continue trade
        }

        Card tradeCard = removeCardFromCollection(incomingCard.getName());
        tBinder.addCard(tradeCard);
        return true; // Trade successful
    }

    public ArrayList<String> getBinderNames() {
        ArrayList<String> binderNames = new ArrayList<>();
        for (Binder binder : this.BINDERS)  {
            binderNames.add(binder.getName());
        }
        return binderNames;
    }

    public ArrayList<String> getDeckNames() {
        ArrayList<String> deckNames = new ArrayList<>();
        for (Deck deck : this.DECKS)  {
            deckNames.add(deck.getName());
        }
        return deckNames;
    }

    public void addCardToCollection(Card c) {
        this.CARD_COLLECTION.addCard(c);
    }

    public Card removeCardFromCollection(String name) {
        return this.CARD_COLLECTION.removeCardByName(name);
    }

    public void incrementCardInCollection(String name) {
        this.CARD_COLLECTION.incrementCard(name);
    }

    public void decrementCardInCollection(String name) {
        this.CARD_COLLECTION.decrementCard(name);
    }
}
