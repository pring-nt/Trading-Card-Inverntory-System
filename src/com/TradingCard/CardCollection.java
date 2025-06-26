package com.TradingCard;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class CardCollection {
    private final ArrayList<Card> CARDS;

    public CardCollection(){
        this.CARDS = new ArrayList<>();
    }

    public void addCard(Card c){
        Card existing = findByCardName(c.getName());
        if (existing == null) {
            CARDS.add(c);
        } else if (existing.equals(c)) {
            existing.incrementCount();
        } else {
            throw new IllegalArgumentException("card with same name but different attributes exists.");
        }
    }

    public Card removeCardByName(String name) {
        if (CARDS.isEmpty()) {
            throw new IllegalStateException("collection is empty!");
        }

        Card target = findByCardName(name);
        if (target == null) {
            throw new NoSuchElementException("card \"" + name + "\" not found in collection!");
        }

        if (target.getCount() == 0) {
            throw new IllegalStateException("no copies left of the requested card.");
        }

        Card copy = Card.copyCard(target);
        target.decrementCount();
        return copy;
    }

    public Card findByCardName(String name) {
        String query = name.trim().toLowerCase();
        for (Card card : CARDS) {
            if (card.getName().trim().toLowerCase().equals(query)) {
                return card;
            }
        }
        return null;
    }


    public void incrementCard(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new NoSuchElementException("card \"" + name + "\" not found in collection!");
        }
        card.incrementCount();
    }

    public void decrementCard(String name) {
        Card card = findByCardName(name);
        if (card == null) {
            throw new NoSuchElementException("card \"" + name + "\" not found in collection!");
        }
        if (card.getCount() > 0) {
            card.decrementCount();
        } else throw new IllegalStateException("card count is already at 0!");
    }

    public ArrayList<Card> getSortedCopy() {
        ArrayList<Card> sortedCopy = new ArrayList<>(this.CARDS);
        sortedCopy.sort(Comparator.comparing(Card::getName));
        return sortedCopy;
    }
}
