package com.TradingCard;
import java.util.ArrayList;

public class Deck {
    private static final int MAX_CAPACITY = 10;

    private final String NAME;
    private final ArrayList<Card> CARDS;

    public Deck(String name) {
        this.NAME = name;
        this.CARDS = new ArrayList<>();
    }

    public String getName() {
        return NAME;
    }

    public Card getCardAtIndex(int index) {
        return this.CARDS.get(index);
    }

    public Card findByCardName(String name) {
        for (Card card : this.CARDS) {
            if (card.getName().equalsIgnoreCase(name)) return card;
        }
        return null;
    }

    public boolean addCard(Card c){
        if(this.CARDS.size() >= MAX_CAPACITY) {
            return false;
        }
        Card existing = findByCardName(c.getName());
        if (existing != null) {
            if (existing.equals(c)){
                return false;
            } else {
                throw new IllegalArgumentException("a different card with the same NAME already exists in the deck.");
            }
        }
        this.CARDS.add(c);
        return true;
    }

    public ArrayList<Card> removeAllCards() {
        ArrayList<Card> cards = new ArrayList<>(this.CARDS);
        this.CARDS.clear();
        return cards;
    }

    public Card removeCardByName(String name) {
        if (this.CARDS.isEmpty()) { // Deck is empty
            throw new IllegalStateException("deck is empty");
        }

        Card target = findByCardName(name);
        if (target == null) { // Card is not found
            throw new IllegalArgumentException("card \"" + name + "\" not found in deck.");
        }

        this.CARDS.remove(target); //Successfully removed card from deck
        return target;
    }

    public ArrayList<Card> getCopyOfCards() {
        return new ArrayList<>(this.CARDS);
    }
}
