package com.TradingCard;

import java.util.ArrayList;
import java.util.Comparator;

public class Binder {
    private static final int MAX_CAPACITY = 20;

    private final String NAME;
    private final ArrayList<Card> CARDS;

    public Binder(String name) {
        this.NAME = name.trim();
        this.CARDS = new ArrayList<>();
    }

    public String getName() {
        return NAME;
    }

    public Card findByCardName(String name) {
        for (Card card : this.CARDS) {
            if (card.getName().equalsIgnoreCase(name)) return card;
        }
        return null;
    }

    public boolean addCard(Card card) {
        if(this.CARDS.size() >= MAX_CAPACITY){
        return false;
        }

        return this.CARDS.add(card);
    }

    public ArrayList<Card> removeAllCards() {
        ArrayList<Card> cards = new ArrayList<>(this.CARDS);
        this.CARDS.clear();
        return cards;
    }

    public Card removeCardByName(String name) {
        if (this.CARDS.isEmpty()) { // Binder is empty
            throw new IllegalStateException("binder is empty");
        }

        Card target = findByCardName(name);
        if (target == null) { // Card not found
            throw new IllegalArgumentException("card \"" + name + "\" not found in binder.");
        }

        this.CARDS.remove(target); // Card successfully removed from binder
        return target;
    }

    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sortedCopy = new ArrayList<>(this.CARDS);
        sortedCopy.sort(Comparator.comparing(Card::getName));
        return sortedCopy;
    }
}
