package com.TradingCard;
import java.util.ArrayList;

public class Deck {
    private String name;
    private ArrayList<Card> cards;

    public Deck(String name, ArrayList<Card> cards) {
        this.name = name;
        this.cards = cards;
    }

    public void viewDeck(){
        System.out.println("\n\nDeck: " + this.name);
        for(Card card: this.cards) {
            if (card != null && card.getCount() != 0) {
                System.out.println(card);
            }
        }
    }
}
