package com.TradingCard;

import java.util.ArrayList;
import java.util.Comparator;

public class Binder {
    private final String name;
    private final ArrayList<Card> cards;

    public Binder(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addCard(Card card) {
        if(this.cards.size() < 20){
            this.cards.add(card);
        }
        else {
            System.out.println("Binder is full!");
        }
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    private Card findCardByName(String name) {
        for (Card card : cards) {
            if (card.getName().equalsIgnoreCase(name)) return card;
        }
        return null;
    }

    public Card removeCard(String name) {
        if (cards.isEmpty()) {
            System.out.println("Binder is empty!");
            return null;
        }

        Card target = findCardByName(name);
        if (target == null) {
            System.out.println("Card not found in binder!");
            return null;
        }

        cards.remove(target);
        System.out.println(target + "\nHas been removed from the binder.");
        return target;
    }

    public void viewBinder() {
        System.out.println("Binder Name: " + this.name);
        ArrayList<Card> sorted = getSortedCopy();
        for(Card card: sorted) {
            if (card.getCount() == 1) {
                System.out.println(card);
            }
        }
    }

    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sortedCopy = new ArrayList<>(this.cards);
        sortedCopy.sort(Comparator.comparing(Card::getName));
        return sortedCopy;
    }
}
