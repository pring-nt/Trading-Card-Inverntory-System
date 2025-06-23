package com.TradingCard;
import java.util.Comparator;
import java.util.ArrayList;

public class Collection {
    private final ArrayList<Card> cards;

    public Collection(){
        this.cards = new ArrayList<>();
    }

    public void addCard(Card c){
        Card existing = findByCardName(c.getName());
        if (existing == null) {
            cards.add(c);
        } else if (existing.equals(c)) {
            existing.incrementCount();
        } else {
            throw new IllegalArgumentException("Card with same name but different attributes exists.");
        }
    }

    public Card findByCardName(String name) {
        String query = name.trim().toLowerCase();
        for (Card card : cards) {
            if (card.getName().trim().toLowerCase().equals(query)) {
                return card;
            }
        }
        return null;
    }


    public void incrementCard(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new IllegalArgumentException("Card not found.");
        }
        card.incrementCount();
    }

    public void decrementCard(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new IllegalArgumentException("Card not found.");
        }
        card.decrementCount();
    }

    public void viewCollection(){
        System.out.println("\n\nCollection:");
        ArrayList<Card> copy = getSortedCopy();
        for(Card card: copy) {
            if (card.getCount() != 0) {
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
