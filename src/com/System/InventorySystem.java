package com.System;
import com.TradingCard.*;
import java.math.BigDecimal;
import java.util.*;

public class InventorySystem {
    private final CardCollection cardCollection;
    private final ArrayList<Deck> decks;
    private final ArrayList<Binder> binders;

    public InventorySystem() {
        this.cardCollection = new CardCollection();
        this.decks = new ArrayList<>();
        this.binders = new ArrayList<>();
    }

    public CardCollection getCardCollection() {
        return cardCollection;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public ArrayList<Binder> getBinders() {
        return binders;
    }

    private void returnCardsToCollection(ArrayList<Card> cards) {
        for (Card c : cards) {
            if (c != null) {
                this.cardCollection.addCard(c);
            }
        }
    }

    public Binder findBinderByName(String name) {
        for (Binder binder: this.binders) {
            if (binder.getName().equalsIgnoreCase(name))
                return binder;
        }
        throw new NoSuchElementException("binder \"" + name + "\" not found");
    }

    public Deck findDeckByName(String name) {
        for (Deck deck: this.decks) {
            if (deck.getName().equalsIgnoreCase(name))
                return deck;
        }
        throw new NoSuchElementException("deck \"" + name + "\" not found");
    }

    public Binder createBinder(String name) {
        Binder existing = findBinderByName(name);
        if (existing != null) {
            throw new IllegalStateException("binder with the name: " + name + " already exists!");
        }
        Binder binder = new Binder(name);
        this.binders.add(binder);
        return binder;
    }

    public boolean deleteBinder(String name) {
        Binder target = findBinderByName(name);
        returnCardsToCollection(target.removeAllCards());
        this.binders.remove(target);
        return true;
    }

    public Deck createDeck(String name) {
        for (Deck d : decks) {
            if (d.getName().equalsIgnoreCase(name))
                throw new IllegalStateException("deck \"" + name + "\" already exists");
        }
        Deck deck = new Deck(name);
        this.decks.add(deck);
        return deck;
    }

    public boolean deleteDeck(String name) {
        Deck target = findDeckByName(name);
        returnCardsToCollection(target.removeAllCards());
        this.decks.remove(target);
        return true;
    }

    public boolean removeCardFromBinder(String binderName, String cardName) {
        Binder tBinder = findBinderByName(binderName);
        Card tCard = tBinder.removeCardByName(cardName);
        addCardToCollection(tCard);
        return true;
    }

    public boolean addCardToBinder(String binderName, String cardName) {
        Binder tBinder = findBinderByName(binderName);
        Card tCard = this.cardCollection.removeCardByName(cardName);
        if(!tBinder.addCard(tCard)) {
            // rollback extraction
            // rethrow a clearer exception for higher layers
            this.cardCollection.addCard(tCard);
            throw new IllegalStateException("unable to add to binder because it is full");
        }
        return true;
    }

    public boolean deleteCardFromDeck(String deckName, String cardName) {
        Deck tDeck = findDeckByName(deckName);
        Card tCard = tDeck.removeCardByName(cardName);
        addCardToCollection(tCard);
        return true;
    }

    public void addCardToDeck(String deckName, String cardName) {
        Deck tDeck = findDeckByName(deckName);
        Card tCard = cardCollection.removeCardByName(cardName);  // throws on error
        if (!tDeck.addCard(tCard)) {
            // rollback
            cardCollection.addCard(tCard);
            throw new IllegalStateException("unable to add to deck (full or duplicate)");
        }
    }

    public boolean tradeCard(String binderName, String outgoingName, Card incomingCard, boolean force) {
        Binder tBinder = findBinderByName(binderName);
        Card outgoingCard = tBinder.removeCardByName(outgoingName);
        addCardToCollection(incomingCard); // Store the ingoing card in collection

        BigDecimal diff = incomingCard.getValue().subtract(outgoingCard.getValue()).abs();
        if (diff.compareTo(BigDecimal.ONE) >= 0 && !force) { // Compare the difference to 1 and if trade is not forced
            // Cancel
            this.cardCollection.removeCardByName(incomingCard.getName()); //Roll back
            tBinder.addCard(outgoingCard);
            return false; // return false to prompt user if they want to continue trade
        }

        Card tradeCard = this.cardCollection.removeCardByName(incomingCard.getName());
        tBinder.addCard(tradeCard);
        return true; // Trade successful
    }

    public ArrayList<String> getBinderNames() {
        ArrayList<String> binderNames = new ArrayList<>();
        for (Binder binder : this.binders)  {
            binderNames.add(binder.getName());
        }
        return binderNames;
    }

    public ArrayList<String> getDeckNames() {
        ArrayList<String> deckNames = new ArrayList<>();
        for (Deck deck : this.decks)  {
            deckNames.add(deck.getName());
        }
        return deckNames;
    }

    public ArrayList<String> getCardNamesInCollection() {
        ArrayList<String> collectionNames = new ArrayList<>();
        for (Card card : this.cardCollection.getSortedCopy())  {
            collectionNames.add(card.getName());
        }
        return collectionNames;
    }

    public void addCardToCollection(Card c) {
        this.cardCollection.addCard(c);
    }
}
