package com.TradingCard;

import java.util.ArrayList;
import java.util.Comparator;

public class Binder {
    private static final int MAX_CAPACITY = 20;

    private final String name;
    private final ArrayList<Card> cards;

    public Binder(String name) {
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

    public boolean addCard(Card card) {
        if(this.cards.size() >= MAX_CAPACITY){
        return false;
        }

        return this.cards.add(card);
    }

    public ArrayList<Card> removeAllCards() {
        ArrayList<Card> cards = new ArrayList<>(this.cards);
        this.cards.clear();
        return cards;
    }

    public Card removeCardByName(String name) {
        if (this.cards.isEmpty()) { // Binder is empty
            throw new IllegalStateException("binder is empty");
        }

        Card target = findByCardName(name);
        if (target == null) { // Card not found
            throw new IllegalArgumentException("card \"" + name + "\" not found in binder.");
        }

        this.cards.remove(target); // Card successfully removed from binder
        return target;
    }

    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sortedCopy = new ArrayList<>(this.cards);
        sortedCopy.sort(Comparator.comparing(Card::getName));
        return sortedCopy;
    }
}
