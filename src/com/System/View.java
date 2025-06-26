package com.System;
import com.TradingCard.*;
import com.TradingCard.Enums.*;

import java.util.*;

public class View {
    private final Scanner sc;

    public View() {
        sc = new Scanner(System.in);
    }
    // Display Names
    public void showDeckNames(ArrayList<String> names) {
        System.out.println("\n=== decks ===");
        for (String name : names) {
            System.out.println("  - " + name);
        }
    }

    public void showBinderNames(ArrayList<String> names) {
        System.out.println("\n=== binders ===");
        for (String name : names) {
            System.out.println("  - " + name);
        }
    }

    //Display rarity and variation
    public void showRarityOptions() {
        System.out.println("\n=== rarities ===");
        for (Rarity r : Rarity.values()) {
            System.out.println("  - " + r.name().toLowerCase());
        }
    }

    public void showVariationOptions() {
        System.out.println("\n=== variations ===");
        for (Variation v : Variation.values()) {
            System.out.println("  - " + v.name().toLowerCase());
        }
    }

    // display single card details
    public void showCardDetails(Card c) {
        System.out.println(c);
    }

    //display collections
    public void showCollection(CardCollection c) {
        ArrayList<Card> cards = c.getSortedCopy();
        System.out.println("\n=== collection ===");
        for (Card card : cards) {
            System.out.println("  - " + card.getName() + " count: " + card.getCount());
        }
    }

    public void showDeck(Deck d) {
        ArrayList<Card> cards = d.getCopyOfCards();
        System.out.printf("%n=== deck: %s ===%n", d.getName());
        int i = 1;
        for (Card card : cards) {
            System.out.printf("  %d) %s%n", i++, card.getName());
        }
    }

    public void showBinder(Binder b) {
        ArrayList<Card> cards = b.getSortedCopy();
        System.out.printf("%n=== binder: %s ===%n", b.getName());
        for (Card card : cards) {
            System.out.println(card.getName());
        }
    }

    /**
     * display the main menu.
     *
     * @param hasCards   true if collection has at least one card
     * @param hasBinders true if at least one binder exists
     * @param hasDecks   true if at least one deck exists
     */
    public void showMainMenu(boolean hasCards, boolean hasBinders, boolean hasDecks) {
        System.out.println("\n=== main menu ===");
        int opt = 1;
        System.out.printf("%d) add a card%n", opt++);
        // before any binders exist
        if (!hasBinders) {
            System.out.printf("%d) create a new binder%n", opt++);
        } else {
            System.out.printf("%d) manage binders%n", opt++);
        }
        // before any decks exist
        if (!hasDecks) {
            System.out.printf("%d) create a new deck%n", opt++);
        } else {
            System.out.printf("%d) manage decks%n", opt++);
        }
        // only once at least one card in collection
        if (hasCards) {
            System.out.printf("%d) view collection/specific card%n", opt++);
            System.out.printf("%d) increase/decrease card count%n", opt++);
        }
        System.out.printf("%d) exit%n", opt);
    }

    public void showManageBinderMenu() {
        System.out.printf("%n=== manage binder ===%n");

        System.out.printf("%d. create new binder%n", 1);
        System.out.printf("%d. view existing binder%n", 2);
        System.out.printf("%d. back to main menu%n", 3);
    }

    public void showManageDeckMenu() {
        System.out.printf("%n=== manage deck ===%n");

        System.out.printf("%d. create new deck%n", 1);
        System.out.printf("%d. view existing deck%n", 2);
        System.out.printf("%d. back to main menu%n", 3);
    }

    /**
     * menu for managing a single binder.
     *
     * @param binderName  name of the binder
     * @param hasCards    true if binder has at least one card
     */
    public void showBinderMenu(String binderName, boolean hasCards) {
        System.out.printf("%n=== binder: %s ===%n", binderName);
        int option = 1;
        System.out.printf("%d. add card to binder%n", option++);
        if (hasCards) {
            System.out.printf("%d. remove card from binder%n", option++);
            System.out.printf("%d. trade card%n", option++);
            System.out.printf("%d. view binder contents%n", option++);
        }
        System.out.printf("%d. delete binder%n", option++);
        System.out.printf("%d. back%n", option);
    }

    /**
     * menu for managing a single deck.
     *
     * @param deckName   name of the deck
     * @param hasCards   true if deck has at least one card
     */
    public void showDeckMenu(String deckName, boolean hasCards) {
        System.out.printf("%n=== deck: %s ===%n", deckName);
        int option = 1;
        System.out.printf("%d. add card to deck%n", option++);
        if (hasCards) {
            System.out.printf("%d. remove card from deck%n", option++);
            System.out.printf("%d. view deck contents%n", option++);
        }
        System.out.printf("%d. delete deck%n", option++);
        System.out.printf("%d. back%n", option);
    }

    public void showCollectionOptions() {
        int option = 1;
        System.out.printf("%d. view a specific card%n", option++);
        System.out.printf("%d. back%n", option);
    }

    public String prompt(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    public boolean confirm(String message) {
        System.out.print(message);
        String input = sc.nextLine();

        switch (input.toLowerCase().trim()) {
            case "yes" -> {return true;}
            case "no" -> {return false;}
            default -> throw new IllegalArgumentException("invalid response: " + input);
        }
    }


    // Generic messages
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String error) {
        System.out.println("error: " + error);
    }

    //Close scanner
    public void closeScanner(){
        this.sc.close();
    }
}
