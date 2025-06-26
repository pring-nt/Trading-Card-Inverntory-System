package com.System;

import com.TradingCard.*;
import com.TradingCard.Enums.*;

import java.util.*;

/**
 * View component for the Trading Card Inventory System (TCIS).
 * <p>
 * Responsible for all CLI input/output operations. Displays menus, lists, details, and
 * prompts the user for input. Does not contain any business logic.
 */
public class View {
    private final Scanner sc;

    /**
     * Constructs a new View with its own Scanner for user input.
     */
    public View() {
        sc = new Scanner(System.in); // initialize scanner for CLI
    }

    /**
     * Display the list of deck names.
     * @param names list of deck names to show
     */
    public void showDeckNames(ArrayList<String> names) {
        System.out.println("\n=== decks ===");
        for (String name : names) {
            System.out.println("  - " + name); // prefix for readability
        }
    }

    /**
     * Display the list of binder names.
     * @param names list of binder names to show
     */
    public void showBinderNames(ArrayList<String> names) {
        System.out.println("\n=== binders ===");
        for (String name : names) {
            System.out.println("  - " + name);
        }
    }

    /**
     * Display available rarities for card creation.
     */
    public void showRarityOptions() {
        System.out.println("\n=== rarities ===");
        for (Rarity r : Rarity.values()) {
            System.out.println("  - " + r.name().toLowerCase());
        }
    }

    /**
     * Display available variations for card creation.
     */
    public void showVariationOptions() {
        System.out.println("\n=== variations ===");
        for (Variation v : Variation.values()) {
            System.out.println("  - " + v.name().toLowerCase());
        }
    }

    /**
     * Display full details of a single card.
     * @param c the Card whose details to show
     */
    public void showCardDetails(Card c) {
        System.out.println(c); // relies on Card.toString()
    }

    /**
     * Display the entire card collection with counts.
     * @param c the CardCollection to display
     */
    public void showCollection(CardCollection c) {
        ArrayList<Card> cards = c.getSortedCopy();
        System.out.println("\n=== collection ===");
        for (Card card : cards) {
            System.out.println("  - card: " + card.getName() + ", count: " + card.getCount());
        }
    }

    /**
     * Display the contents of a deck.
     * @param d the Deck to display
     */
    public void showDeck(Deck d) {
        ArrayList<Card> cards = d.getCopyOfCards();
        System.out.printf("%n=== deck: %s ===%n", d.getName());
        int i = 1;
        for (Card card : cards) {
            System.out.printf("  %d) %s%n", i++, card.getName());
        }
    }

    /**
     * Display the contents of a binder.
     * @param b the Binder to display
     */
    public void showBinder(Binder b) {
        ArrayList<Card> cards = b.getSortedCopy();
        System.out.printf("%n=== binder: %s ===%n", b.getName());
        for (Card card : cards) {
            System.out.println(card.getName());
        }
    }

    /**
     * Display the dynamic main menu based on current state.
     * @param hasCards   true if collection has at least one card
     * @param hasBinders true if at least one binder exists
     * @param hasDecks   true if at least one deck exists
     */
    public void showMainMenu(boolean hasCards, boolean hasBinders, boolean hasDecks) {
        System.out.println("\n=== main menu ===");
        int opt = 1;
        System.out.printf("%d. add a card%n", opt++);
        // binder option
        if (!hasBinders) System.out.printf("%d. create a new binder%n", opt++);
        else System.out.printf("%d. manage binders%n", opt++);
        // deck option
        if (!hasDecks) System.out.printf("%d. create a new deck%n", opt++);
        else System.out.printf("%d. manage decks%n", opt++);
        // collection options
        if (hasCards) {
            System.out.printf("%d. view collection%n", opt++);
            System.out.printf("%d. increase/decrease card count%n", opt++);
        }
        System.out.printf("%d. exit%n", opt);
    }

    /**
     * Display menu to create or view binders.
     */
    public void showManageBinderMenu() {
        System.out.printf("%n=== manage binder ===%n");
        System.out.printf("%d. create new binder%n", 1);
        System.out.printf("%d. view existing binder%n", 2);
        System.out.printf("%d. back to main menu%n", 3);
    }

    /**
     * Display menu to create or view decks.
     */
    public void showManageDeckMenu() {
        System.out.printf("%n=== manage deck ===%n");
        System.out.printf("%d. create new deck%n", 1);
        System.out.printf("%d. view existing deck%n", 2);
        System.out.printf("%d. back to main menu%n", 3);
    }

    /**
     * Display options for a specific binder's operations.
     * @param binderName name of the binder
     * @param hasCards   true if binder contains cards
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
     * Display options for a specific deck's operations.
     * @param deckName name of the deck
     * @param hasCards true if deck contains cards
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

    /**
     * Display collection sub-menu for specific card view.
     */
    public void showCollectionOptions() {
        int option = 1;
        System.out.printf("%d. view a specific card%n", option++);
        System.out.printf("%d. back%n", option);
    }

    /**
     * Prompt with message and read a line from the user.
     * @param message prompt to display before reading
     * @return user input line
     */
    public String readLine(String message) {
        System.out.print(message);
        return sc.nextLine(); // read input
    }

    /**
     * Prompt for yes/no confirmation.
     * @param message prompt message
     * @return true if user enters "yes", false if "no"
     * @throws IllegalArgumentException on other input
     */
    public boolean confirm(String message) {
        System.out.print(message);
        String input = sc.nextLine();
        switch (input.toLowerCase().trim()) {
            case "yes" -> { return true; }
            case "no"  -> { return false; }
            default    -> throw new IllegalArgumentException("invalid response: " + input);
        }
    }

    /**
     * Display a general information message.
     * @param message the message to show
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Display an error message.
     * @param error the error text to show
     */
    public void showError(String error) {
        System.out.println("error: " + error);
    }

    /**
     * Close the Scanner resource when application exits.
     */
    public void closeScanner() {
        this.sc.close();
    }
}
