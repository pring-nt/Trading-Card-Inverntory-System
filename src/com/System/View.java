package com.System;

import com.TradingCard.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * View component for the Trading Card Inventory System (TCIS).
 * <p>
 * Responsible for all CLI input/output operations. Displays menus, lists, details, and
 * prompts the user for input. Contains no business logic.
 */
public class View {
    /**
     * Scanner used for reading CLI input from the user.
     */
    private final Scanner SC;

    /**
     * Constructs a new View with its own Scanner for user input.
     */
    public View() {
        SC = new Scanner(System.in);
    }

    /**
     * Displays the list of deck names.
     *
     * @param names the list of deck names to show
     */
    public void showDeckNames(ArrayList<String> names) {
        System.out.println("\n=== decks ===");
        for (String name : names) {
            System.out.println("  - " + name);
        }
    }

    /**
     * Displays the list of binder names.
     *
     * @param names the list of binder names to show
     */
    public void showBinderNames(ArrayList<String> names) {
        System.out.println("\n=== binders ===");
        for (String name : names) {
            System.out.println("  - " + name);
        }
    }

    /**
     * Displays available rarities for card creation.
     *
     * @param names the list of rarity names to show
     */
    public void showRarityOptions(ArrayList<String> names) {
        System.out.println("\n=== rarities ===");
        for (String name : names) {
            System.out.println("  - " + name);
        }
    }

    /**
     * Displays available variations for card creation.
     *
     * @param names the list of variation names to show
     */
    public void showVariationOptions(ArrayList<String> names) {
        System.out.println("\n=== variations ===");
        for (String name : names) {
            System.out.println("  - " + name);
        }
    }

    /**
     * Displays available binder types for binder creation.
     *
     * @param names the list of binder type names to show
     */
    public void showBinderTypeOptions(ArrayList<String> names) {
        System.out.println("\n=== binder types ===");
        for (String name : names) {
            System.out.println("  - " + name);
        }
    }

    /**
     * Displays full details of a single card.
     *
     * @param details the card details string (usually from Card.toString())
     */
    public void showCardDetails(String details) {
        System.out.println(details);
    }

    /**
     * Displays the entire card collection with counts.
     *
     * @param collection the CardCollection to display
     */
    public void showCollection(CardCollection collection) {
        ArrayList<Card> cards = collection.getSortedCopy();
        System.out.println("\n=== collection ===");
        for (Card card : cards) {
            System.out.println("  - card: " + card.getName() + ", count: " + card.getCount());
        }
    }

    /**
     * Displays the contents of a deck.
     *
     * @param deck the Deck to display
     */
    public void showDeck(Deck deck) {
        ArrayList<Card> cards = deck.getCopyOfCards();
        System.out.printf("%n=== deck: %s ===%n", deck.getName());
        int index = 1;
        for (Card card : cards) {
            System.out.printf("  %d) %s%n", index++, card.getName());
        }
    }

    /**
     * Displays the contents of a binder.
     *
     * @param binder the Binder to display
     */
    public void showBinder(Binder binder) {
        ArrayList<Card> cards = binder.getSortedCopy();
        System.out.printf("%n=== binder: %s ===%n", binder.getName());
        for (Card card : cards) {
            System.out.println(card.getName());
        }
    }

    /**
     * Displays the dynamic main menu based on current state.
     *
     * @param hasCards          true if collection has at least one card
     * @param hasBinders        true if at least one binder exists
     * @param hasDecks          true if at least one deck exists
     * @param collectorEarnings the current collector earnings value
     */
    public void showMainMenu(boolean hasCards, boolean hasBinders, boolean hasDecks, BigDecimal collectorEarnings) {
        System.out.println("\n=== main menu ===");
        System.out.printf("collector earnings: %.2f%n", collectorEarnings.doubleValue());
        int option = 1;
        System.out.printf("%d. add a card%n", option++);
        if (!hasBinders) System.out.printf("%d. create a new binder%n", option++);
        else System.out.printf("%d. manage binders%n", option++);
        if (!hasDecks) System.out.printf("%d. create a new deck%n", option++);
        else System.out.printf("%d. manage decks%n", option++);
        if (hasCards) {
            System.out.printf("%d. view collection%n", option++);
            System.out.printf("%d. increase/decrease card count%n", option++);
        }
        System.out.printf("%d. exit%n", option);
    }

    /**
     * Displays the manage binder menu.
     */
    public void showManageBinderMenu() {
        System.out.printf("%n=== manage binder ===%n");
        System.out.printf("%d. create new binder%n", 1);
        System.out.printf("%d. view existing binder%n", 2);
        System.out.printf("%d. back to main menu%n", 3);
    }

    /**
     * Displays the manage deck menu.
     */
    public void showManageDeckMenu() {
        System.out.printf("%n=== manage deck ===%n");
        System.out.printf("%d. create new deck%n", 1);
        System.out.printf("%d. view existing deck%n", 2);
        System.out.printf("%d. back to main menu%n", 3);
    }

    /**
     * Displays options for a specific binder's operations.
     *
     * @param binderName the name of the binder
     * @param hasCards   true if binder contains cards
     * @param sellable   true if binder can be sold
     * @param isLuxury   true if binder is luxury type
     */
    public void showBinderMenu(String binderName, boolean hasCards, boolean sellable, boolean isLuxury) {
        System.out.printf("%n=== binder: %s ===%n", binderName);
        int option = 1;
        System.out.printf("%d. add card to binder%n", option++);
        if (hasCards) {
            System.out.printf("%d. remove card from binder%n", option++);
            System.out.printf("%d. trade card%n", option++);
            System.out.printf("%d. view binder contents%n", option++);
        }
        System.out.printf("%d. delete binder%n", option++);
        if (hasCards && sellable) {
            if (isLuxury) System.out.printf("%d. set binder value%n", option++);
            System.out.printf("%d. sell binder%n", option++);
        }
        System.out.printf("%d. back%n", option);
    }

    /**
     * Displays options for a specific deck's operations.
     *
     * @param deckName the name of the deck
     * @param hasCards true if deck contains cards
     * @param sellable true if deck can be sold
     */
    public void showDeckMenu(String deckName, boolean hasCards, boolean sellable) {
        System.out.printf("%n=== deck: %s ===%n", deckName);
        int option = 1;
        System.out.printf("%d. add card to deck%n", option++);
        if (hasCards) {
            System.out.printf("%d. remove card from deck%n", option++);
            System.out.printf("%d. view deck contents%n", option++);
        }
        System.out.printf("%d. delete deck%n", option++);
        if (hasCards && sellable) System.out.printf("%d. sell deck%n", option++);
        System.out.printf("%d. back%n", option);
    }

    /**
     * Displays collection submenu for specific card actions.
     */
    public void showCollectionOptions() {
        System.out.printf("%d. view a specific card%n", 1);
        System.out.printf("%d. sell a specific card%n", 2);
        System.out.printf("%d. back%n", 3);
    }

    /**
     * Prompts with a message and reads a line from the user.
     *
     * @param message the prompt to display before reading
     * @return the line of input entered by the user
     */
    public String readLine(String message) {
        System.out.print(message);
        return SC.nextLine();
    }

    /**
     * Prompts for yes/no confirmation.
     *
     * @param message the prompt message
     * @return true if the user enters "yes", false if "no"
     * @throws IllegalArgumentException if input is not "yes" or "no"
     */
    public boolean confirm(String message) {
        System.out.print(message);
        String input = SC.nextLine().toLowerCase().trim();
        switch (input) {
            case "yes" -> {
                return true;
            }
            case "no" -> {
                return false;
            }
            default -> throw new IllegalArgumentException("invalid response: " + input);
        }
    }

    /**
     * Displays a general information message.
     *
     * @param message the message to show
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message.
     *
     * @param error the error text to show
     */
    public void showError(String error) {
        System.out.println("error: " + error);
    }

    /**
     * Closes the Scanner resource when the application exits.
     */
    public void closeScanner() {
        SC.close();
    }
}