package com.TradingCard;
import java.util.ArrayList;
import java.util.Comparator;

public class Deck {
    private static final int MAX_CAPACITY = 10;

    private final String name;
    private final ArrayList<Card> cards;

    public Deck(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Card getCardAtIndex(int index) {
        return this.cards.get(index);
    }

    public Card findByCardName(String name) {
        for (Card card : this.cards) {
            if (card.getName().equalsIgnoreCase(name)) return card;
        }
        return null;
    }

    public boolean containsCard(String name) {
        return findByCardName(name) != null;
    }

    public boolean addCard(Card c){
        if(this.cards.size() >= MAX_CAPACITY) {
            return false;
        }
        Card existing = findByCardName(c.getName());
        if (existing != null) {
            if (existing.equals(c)){
                return false;
            } else {
                throw new IllegalArgumentException("a different card with the same name already exists in the deck.");
            }
        }
        this.cards.add(c);
        return true;
    }

    public ArrayList<Card> removeAllCards() {
        ArrayList<Card> cards = new ArrayList<>(this.cards);
        this.cards.clear();
        return cards;
    }

    public Card removeCardByName(String name) {
        if (this.cards.isEmpty()) { // Deck is empty
            throw new IllegalStateException("deck is empty");
        }

        Card target = findByCardName(name);
        if (target == null) { // Card is not found
            throw new IllegalArgumentException("card \"" + name + "\" not found in deck.");
        }

        this.cards.remove(target); //Successfully removed card from deck
        return target;
    }

    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sortedCopy = new ArrayList<>(this.cards);
        sortedCopy.sort(Comparator.comparing(Card::getName));
        return sortedCopy;
    }
}
