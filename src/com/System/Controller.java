package com.System;

import com.TradingCard.*;
import com.TradingCard.Enums.Rarity;
import com.TradingCard.Enums.Variation;

import java.math.BigDecimal;

/**
 * Controller for the Trading Card Inventory System (TCIS).
 * <p>
 * Implements the CLI-driven control flow between the View and the InventorySystem model.
 * Handles user input, invokes model operations, and delegates display to the View.
 */
public class Controller {
    private final View VIEW;
    private final InventorySystem INVENTORY_SYSTEM;

    /**
     * Constructs a Controller with given View and InventorySystem.
     * @param view the View component for I/O
     * @param system the InventorySystem model
     */
    public Controller(View view, InventorySystem system) {
        this.VIEW = view;             // assign the view for printing and input
        this.INVENTORY_SYSTEM = system; // assign the model for business logic
    }

    /**
     * Main application loop. Displays menus, processes user choices, and handles exit confirmation.
     */
    public void run() {
        boolean exitFlag = false;
        // loop until user confirms exit
        while (!exitFlag) {
            // check model state to adapt menu
            boolean hasCards   = !INVENTORY_SYSTEM.getCardCollection().getSortedCopy().isEmpty();
            boolean hasBinders = !INVENTORY_SYSTEM.getBinderNames().isEmpty();
            boolean hasDecks   = !INVENTORY_SYSTEM.getDeckNames().isEmpty();

            VIEW.showMainMenu(hasCards, hasBinders, hasDecks); // display options
            String choice = prompt();                            // get user input
            try {
                // route based on input
                switch (choice) {
                    case "1" -> handleAddCard();                     // add new card
                    case "2" -> { if (!hasBinders) handleCreateBinder(); else handleManageBinderMenu(); }
                    case "3" -> { if (!hasDecks) handleCreateDeck(); else handleManageDeckMenu(); }
                    case "4" -> { if (hasCards) handleViewCollection(); else exitFlag = true; }
                    case "5" -> { if (hasCards) handleAdjustCount(); else invalid(); }
                    case "6" -> { if (hasCards) exitFlag = true; else invalid(); }
                    default  -> invalid();                              // invalid choice
                }
            } catch (Exception e) {
                // display any model or parsing errors
                VIEW.showError(e.getMessage());
            }

            // confirm exit if flagged
            if (exitFlag) {
                boolean confirm = VIEW.confirm("are you sure you want to exit? (yes/no): ");
                if (!confirm) exitFlag = false; // cancel exit if user says no
            }
        }
        VIEW.closeScanner();                         // cleanup scanner resource
        VIEW.showMessage("closing program... goodbye!"); // final message
    }

    /**
     * Handle adding a card: either new or increment existing count.
     * @return the added/incremented card's name, or null if user cancels
     */
    private String handleAddCard() {
        String name = promptInput("card name: ");
        Card existing = INVENTORY_SYSTEM.findCardByNameInCollection(name);
        if (existing != null) {
            // existing card: offer increment
            VIEW.showMessage("card already exists in collection");
            if (VIEW.confirm("do you want to increment the count of this card instead? (yes/no): ")) {
                INVENTORY_SYSTEM.incrementCardInCollection(name);
                return name.trim().toLowerCase(); // return after increment
            }
            return null; // user cancelled
        }
        // new card: prompt attributes
        Rarity rarity = promptRarity();
        Variation var = promptVariation(rarity);
        BigDecimal val = promptBaseValue();
        Card c = new Card(name, rarity, var, val);
        INVENTORY_SYSTEM.addCardToCollection(c);
        VIEW.showMessage("card added: " + c.getName());
        return c.getName();
    }

    /**
     * Prompt and create a new Binder in the model.
     */
    private void handleCreateBinder() {
        String name = promptInput("binder name: ");
        INVENTORY_SYSTEM.createBinder(name);
        VIEW.showMessage("binder created: " + name);
    }

    /**
     * Prompt and create a new Deck in the model.
     */
    private void handleCreateDeck() {
        String name = promptInput("deck name: ");
        INVENTORY_SYSTEM.createDeck(name);
        VIEW.showMessage("deck created: " + name);
    }

    /**
     * Show collection, then allow user to choose a card to adjust count.
     */
    private void handleAdjustCount() {
        VIEW.showCollection(INVENTORY_SYSTEM.getCardCollection());
        String name = promptInput("card name: ").trim().toLowerCase();
        if (INVENTORY_SYSTEM.findCardByNameInCollection(name) == null) {
            throw new IllegalArgumentException("card \"" + name + "\" not found in collection!");
        }
        String op = promptInput("increase or decrease? : ").trim().toLowerCase();
        switch(op) {
            case "increase" -> INVENTORY_SYSTEM.incrementCardInCollection(name);
            case "decrease" -> INVENTORY_SYSTEM.decrementCardInCollection(name);
            default -> throw new IllegalArgumentException("must enter either 'increase' or 'decrease'");
        }
        VIEW.showMessage("count updated");
    }

    /**
     * Display collection list and sub-menu for specific card details.
     */
    private void handleViewCollection() {
        String choice;
        String cardName;
        Card c;
        boolean back = false;
        while(!back) {
            VIEW.showCollection(INVENTORY_SYSTEM.getCardCollection());
            VIEW.showCollectionOptions();
            choice = prompt();
            switch (choice) {
                case "1" -> {
                    cardName = promptInput("card name: ");
                    c = INVENTORY_SYSTEM.findCardByNameInCollection(cardName);
                    if (c == null) {
                        invalid();
                    }
                    else VIEW.showCardDetails(c);
                }
                case "2" -> back = true; // return to main menu
                default -> invalid();
            }
        }
    }

    /**
     * Sub-menu for binder creation or viewing existing binders.
     */
    private void handleManageBinderMenu() {
        String choice;
        boolean back = false;
        while(!back) {
            VIEW.showManageBinderMenu();
            choice = prompt();
            switch (choice) {
                case "1" -> handleCreateBinder();
                case "2" ->  { if(!INVENTORY_SYSTEM.getBinderNames().isEmpty()) handleViewBinder();
                               else VIEW.showError("there are no binders found"); } // In case there are no binders left
                case "3" -> back = true;
                default -> invalid();
            }
        }
    }

    /**
     * Sub-menu for deck creation or viewing existing decks.
     */
    private void handleManageDeckMenu() {
        String choice;
        boolean back = false;
        while(!back) {
            VIEW.showManageDeckMenu();
            choice = prompt();
            switch (choice) {
                case "1" -> handleCreateDeck();
                case "2" -> { if(!INVENTORY_SYSTEM.getDeckNames().isEmpty()) handleViewDeck();
                              else VIEW.showError("there are no decks found"); } // In case there are no decks left
                case "3" -> back = true;
                default -> invalid();
            }
        }
    }

    /**
     * Display and operate on a specific Binder's contents (add/remove/trade).
     */
    private void handleViewBinder() {
        VIEW.showBinderNames(INVENTORY_SYSTEM.getBinderNames());
        String binderName = promptInput("select binder: ");
        Binder curBinder = INVENTORY_SYSTEM.findBinderByName(binderName);
        boolean back = false;
        while (!back) {
            boolean hasCard = !curBinder.getSortedCopy().isEmpty();
            VIEW.showBinderMenu(binderName, hasCard);
            String opt = prompt();
            if(hasCard) {
                switch (opt) {
                    case "1" -> addCardToBinder(binderName);
                    case "2" -> removeCardFromBinder(binderName);
                    case "3" -> tradeInBinder(binderName);
                    case "4" -> viewBinder(binderName);
                    case "5" -> {
                        INVENTORY_SYSTEM.deleteBinder(binderName);
                        back = true;
                    }
                    case "6" -> back = true;
                    default -> invalid();
                }
            } else { // if binder has no cards
                switch (opt) {
                    case "1" -> addCardToBinder(binderName);
                    case "2" -> {
                        INVENTORY_SYSTEM.deleteBinder(binderName);
                        back = true;
                    }
                    case "3" -> back = true;
                    default -> invalid();
                }
            }
        }
    }

    /**
     * Display and operate on a specific Deck's contents (add/remove/view).
     */
    private void handleViewDeck() {
        VIEW.showDeckNames(INVENTORY_SYSTEM.getDeckNames());
        String deckName = promptInput("select deck: ");
        boolean back = false;
        Deck curDeck = INVENTORY_SYSTEM.findDeckByName(deckName);
        while (!back) {
            boolean hasCard = !curDeck.getCopyOfCards().isEmpty();
            VIEW.showDeckMenu(deckName, hasCard);
            String opt = prompt();
            if (hasCard) {
                switch (opt) {
                    case "1" -> addCardToDeck(deckName);
                    case "2" -> deleteCardFromDeck(deckName);
                    case "3" -> viewDeck(deckName);
                    case "4" -> {
                        INVENTORY_SYSTEM.deleteDeck(deckName);
                        back = true;
                    }
                    case "5" -> back = true;
                    default -> invalid();
                }
            } else { // if deck has no cards
                switch (opt) {
                    case "1" -> addCardToDeck(deckName);
                    case "2" -> {
                        INVENTORY_SYSTEM.deleteDeck(deckName);
                        back = true;
                    }
                    case "3" -> back = true;
                    default -> invalid();
                }
            }
        }
    }

    /**
     * Prompt the user for a menu choice.
     */
    private String prompt() {
        return VIEW.readLine("choose an option: ");
    }

    /**
     * Prompt the user with a given message and return input.
     */
    private String promptInput(String msg) {
        return VIEW.readLine(msg);
    }

    /**
     * Display an error message for invalid menu choices.
     */
    private void invalid() {
        VIEW.showError("invalid option");
    }

    /**
     * Loop until a valid Rarity is entered.
     */
    private Rarity promptRarity() {
        while (true) {
            VIEW.showRarityOptions();
            String input = prompt();
            try {
                return Rarity.valueOf(input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                VIEW.showError("invalid rarity: " + input);
            }
        }
    }

    /**
     * Loop until a valid Variation is entered, based on rarity.
     */
    private Variation promptVariation(Rarity rarity) {
        if (rarity != Rarity.RARE && rarity != Rarity.LEGENDARY) {
            return Variation.NORMAL;
        }
        while (true) {
            VIEW.showVariationOptions();
            String input = prompt();
            try {
                return Variation.valueOf(input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                VIEW.showError("invalid variation: " + input);
            }
        }
    }

    /**
     * Loop until a valid BigDecimal is entered for base value.
     */
    private BigDecimal promptBaseValue() {
        while (true) {
            try {
                return new BigDecimal(promptInput("base value: "));
            } catch (NumberFormatException e) {
                VIEW.showError("invalid number");
            }
        }
    }

    /**
     * Add a card from collection to binder, with rollback on failure.
     */
    private void addCardToBinder(String bName) {
        String cName = promptInput("card name: ");
        INVENTORY_SYSTEM.addCardToBinder(bName, cName);
        VIEW.showMessage("added to binder");
    }

    /**
     * Remove a card from a binder back into the collection.
     */
    private void removeCardFromBinder(String bName) {
        String cName = promptInput("card cName: ");
        INVENTORY_SYSTEM.removeCardFromBinder(bName, cName);
        VIEW.showMessage("removed from binder");
    }

    /**
     * View a binder given a binder name.
     */
    private void viewBinder(String bName) {
        VIEW.showBinder(INVENTORY_SYSTEM.findBinderByName(bName));
    }

    /**
     * Handle a trade: removes outgoing, adds incoming, with optional force.
     */
    private void tradeInBinder(String bName) {
        String out = promptInput("outgoing name: ");
        String incomingName = handleAddCard(); // reuse prompts for incoming card
        Card incoming = INVENTORY_SYSTEM.findCardByNameInCollection(incomingName);
        boolean proceed = INVENTORY_SYSTEM.tradeCard(bName, out, incoming, false);
        if (!proceed) {
            boolean yes = VIEW.confirm("value difference ≥ $1, proceed? (yes/no): ");
            INVENTORY_SYSTEM.tradeCard(bName, out, incoming, yes);
        }
        VIEW.showMessage("trade completed");
    }

    /**
     * Add a card from collection to a deck, with rollback on failure.
     */
    private void addCardToDeck(String deckName) {
        String name = promptInput("card name: ");
        INVENTORY_SYSTEM.addCardToDeck(deckName, name);
        VIEW.showMessage("added to deck");
    }

    /**
     * Remove a card from a deck back into the collection.
     */
    private void deleteCardFromDeck(String deckName) {
        String name = promptInput("card name: ");
        INVENTORY_SYSTEM.deleteCardFromDeck(deckName, name);
        VIEW.showMessage("removed from deck");
    }

    /**
     * Display deck details and optionally view a specific card.
     */
    private void viewDeck(String deckName) {
        Deck d = INVENTORY_SYSTEM.findDeckByName(deckName);
        VIEW.showDeck(d);
        boolean inspect = VIEW.confirm("do you want to view a specific card? (yes/no): ");
        if(!inspect) { return; }
        String choice = promptInput("view a card by number or name: ");
        Card c;
        if (choice.matches("\\d+")) {
            int idx = Integer.parseInt(choice) - 1;
            c = d.getCardAtIndex(idx);
        } else {
            c = d.findByCardName(choice);
        }
        VIEW.showCardDetails(c);
    }
}
