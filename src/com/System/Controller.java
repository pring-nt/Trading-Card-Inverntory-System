package com.System;

import com.TradingCard.*;
import com.TradingCard.Enums.BinderType;
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
    private final EnhancedTCIS INVENTORY_SYSTEM;

    /**
     * Constructs a Controller with given View and InventorySystem.
     * @param view the View component for I/O
     * @param system the InventorySystem model
     */
    public Controller(View view, EnhancedTCIS system) {
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
            VIEW.showMainMenu(hasCards, hasBinders, hasDecks, INVENTORY_SYSTEM.getCollectorEarnings()); // display options
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
     * Handle adding a card: new or increment existing count, with cancel option at any prompt.
     * @return the added or incremented card's name, or null if user aborts
     */
    private String handleAddCard() {
        // prompt for name or cancel
        String name = promptInput("input card name (or 'cancel' to abort): ");
        if (name == null || name.trim().equalsIgnoreCase("cancel")) {
            return null;
        }
        Card existing = INVENTORY_SYSTEM.findCardByNameInCollection(name);
        if (existing != null) {
            VIEW.showMessage("card already exists in collection");
            if (!VIEW.confirm("increment count instead? (yes/no): ")) {
                return null;
            }
            INVENTORY_SYSTEM.incrementCardInCollection(name);
            return name.trim().toLowerCase();
        }
        // prompt for rarity or cancel
        Rarity rarity = null;
        boolean exitFlag = false;
        while (!exitFlag) {
            VIEW.showRarityOptions(INVENTORY_SYSTEM.getRarityNames());
            String input = promptInput("input rarity (or 'cancel' to abort): ");
            if (input == null || input.trim().equalsIgnoreCase("cancel")) {
                return null;
            }
            try {
                rarity = Rarity.valueOf(input.trim().toUpperCase());
                exitFlag = true;
            } catch (IllegalArgumentException e) {
                VIEW.showError("invalid rarity: " + input);
            }
        }
        // prompt for variation or cancel
        Variation var = null;
        if (rarity != Rarity.RARE && rarity != Rarity.LEGENDARY) {
            var = Variation.NORMAL;
        } else {
            exitFlag = false;
            while (!exitFlag) {
                VIEW.showVariationOptions(INVENTORY_SYSTEM.getVariationNames());
                String input = promptInput("input variation (or 'cancel' to abort): ");
                if (input == null || input.trim().equalsIgnoreCase("cancel")) {
                    return null;
                }
                try {
                    var = Variation.valueOf(input.trim().toUpperCase());
                    exitFlag = true;
                } catch (IllegalArgumentException e) {
                    VIEW.showError("invalid variation: " + input);
                }
            }
        }
        // prompt for base value or cancel
        BigDecimal val = null;
        exitFlag = false;
        while (!exitFlag) {
            String valueStr = promptInput("input base value (or 'cancel' to abort): ");
            if (valueStr == null || valueStr.trim().equalsIgnoreCase("cancel")) {
                return null;
            }
            try {
                val = new BigDecimal(valueStr.trim());
                exitFlag = true;
            } catch (NumberFormatException e) {
                VIEW.showError("invalid number: " + valueStr);
            }
        }
        // create and add card
        Card c = new Card(name, rarity, var, val);
        INVENTORY_SYSTEM.addCardToCollection(c);
        VIEW.showMessage("card added: " + c.getName());
        return c.getName();
    }

    /**
     * Prompt and create a new Binder in the model.
     */
    private void handleCreateBinder() {
        String name = promptInput("input binder name (or 'cancel' to abort): ");
        if (name.trim().equalsIgnoreCase("cancel")) {
            return;
        }
        boolean exitFlag;
        BinderType binderType = null;
        exitFlag = false;
        while (!exitFlag) {
            VIEW.showBinderTypeOptions(INVENTORY_SYSTEM.getBinderTypes());
            String input = promptInput("input binder type (or 'cancel' to abort): ");
            if (input.trim().equalsIgnoreCase("cancel")) {
                return;
            }
            try {
                binderType = BinderType.valueOf(input.trim().toUpperCase());
                exitFlag = true;
            } catch (IllegalArgumentException e) {
                VIEW.showError("invalid binder type: " + input);
            }
        }

        INVENTORY_SYSTEM.createBinder(name, binderType);
        VIEW.showMessage("binder created: " + name);
    }

    /**
     * Prompt and create a new Deck in the model.
     */
    private void handleCreateDeck() {
        String name = promptInput("input deck name (or 'cancel' to abort): ");
        if (name.trim().equalsIgnoreCase("cancel")) {
            return;
        }
        boolean exitFlag;
        boolean sellable = false;
        exitFlag = false;
        while (!exitFlag) {
            String input = promptInput("is the deck sellable? (yes/no or cancel to abort): ");
            switch(input.trim().toLowerCase()) {
                case "cancel"   -> { return; }
                case "yes"      -> { sellable = true;
                                     exitFlag = true; }
                case "no"       -> exitFlag = true;
                default         -> invalid();
            }
        }
        INVENTORY_SYSTEM.createDeck(name, sellable);
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
                    else VIEW.showCardDetails(c.toString());
                }
                case "2" -> {
                    cardName = promptInput("card name: ");
                    VIEW.showMessage("card value: $" + INVENTORY_SYSTEM.getCardValue(cardName));
                    if(VIEW.confirm("Are you sure you want to sell this card? (yes/no): ")) {
                        INVENTORY_SYSTEM.sellCard(cardName);
                    }
                }
                case "3" -> back = true; // return to main menu
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
                case "2" ->  {
                    if(!INVENTORY_SYSTEM.getBinderNames().isEmpty()) {
                        handleViewBinder();
                    } else {
                        VIEW.showError("there are no binders found");
                    }
                } // In case there are no binders left
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
                case "2" -> {
                    if(!INVENTORY_SYSTEM.getDeckNames().isEmpty()) {
                        handleViewDeck();
                    } else {
                        VIEW.showError("there are no decks found");
                    }
                } // In case there are no decks left
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
        boolean back = false;
        while (!back) {
            boolean sellable = INVENTORY_SYSTEM.isBinderSellable(binderName);
            boolean hasCard = !INVENTORY_SYSTEM.isBinderEmpty(binderName);
            boolean isLuxury = INVENTORY_SYSTEM.isLuxuryBinder(binderName);
            VIEW.showBinderMenu(binderName, hasCard, sellable, isLuxury);
            if(sellable) {
                VIEW.showMessage("binder's real value: $" + INVENTORY_SYSTEM.getBinderValue(binderName));
            }
            if(isLuxury) {
                VIEW.showMessage("binder's set value: $" + INVENTORY_SYSTEM.getLuxuryBinderCustomPrice(binderName));
            }
            String opt = prompt();
            if(hasCard) {
                switch (opt) {
                    case "1" -> addCardToBinder(binderName);
                    case "2" -> removeCardFromBinder(binderName);
                    case "3" -> tradeInBinder(binderName);
                    case "4" -> viewBinder(binderName);
                    case "5" -> {
                        if(VIEW.confirm("Are you sure you want to delete this binder? (yes/no): ")) {
                            INVENTORY_SYSTEM.deleteBinder(binderName);
                        }
                        back = true;
                    }
                    case "6" -> {
                        if(sellable) {
                            if(isLuxury) {
                                try {
                                    BigDecimal currentValue = INVENTORY_SYSTEM.getBinderValue(binderName);
                                    String inputVal = promptInput("Enter new custom price: $");
                                    BigDecimal newValue = new BigDecimal(inputVal.trim());
                                    if (newValue.compareTo(currentValue) > 0) {
                                        INVENTORY_SYSTEM.setBinderPrice(binderName, newValue);
                                        VIEW.showMessage("Custom price set successfully.");
                                    } else {
                                        VIEW.showMessage("Price must be greater than current binder value: $" + currentValue);
                                    }
                                } catch (Exception e) {
                                    VIEW.showMessage("Failed to set custom price: " + e.getMessage());
                                }

                            } else {
                                if (VIEW.confirm("Are you sure you want to sell this binder? (yes/no): ")) {
                                    INVENTORY_SYSTEM.sellBinder(binderName);
                                }
                            }
                        }
                        back = true;
                    }
                    case "7" -> {
                        if (sellable) {
                            if(isLuxury) {
                                if (VIEW.confirm("Are you sure you want to sell this binder? (yes/no): ")) {
                                    INVENTORY_SYSTEM.sellBinder(binderName);
                                }
                            } else {
                                back = true;
                            }
                        } else {
                            invalid();
                        }
                    }
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
        while (!back) {
            boolean hasCard = !INVENTORY_SYSTEM.isDeckEmpty(deckName);
            boolean sellable = INVENTORY_SYSTEM.isDeckSellable(deckName);
            VIEW.showDeckMenu(deckName, hasCard, sellable);
            if(sellable) {
                VIEW.showMessage("deck value: $" + INVENTORY_SYSTEM.getDeckValue(deckName));
            }
            String opt = prompt();
            if (hasCard) {
                switch (opt) {
                    case "1" -> addCardToDeck(deckName);
                    case "2" -> removeCardFromDeck(deckName);
                    case "3" -> viewDeck(deckName);
                    case "4" -> {
                        if(VIEW.confirm("Are you sure you want to delete this deck? (yes/no): ")) {
                            INVENTORY_SYSTEM.deleteDeck(deckName);
                        }
                        back = true;
                    }
                    case "5" -> {
                        if(sellable) {
                            if(VIEW.confirm("Are you sure you want to sell this deck (yes/no): ")) {
                                INVENTORY_SYSTEM.sellDeck(deckName);
                            }
                        }
                            back = true;
                    }
                    case "6" -> {
                        if (sellable) {
                            back = true;
                        } else {
                            invalid();
                        }
                    }
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
        String out = promptInput("outgoing name (or 'cancel' to abort): ");
        if (out == null || out.trim().equalsIgnoreCase("cancel")) {
            return;
        }
        String incomingName = handleAddCard(); // reuse prompts for incoming card
        if(incomingName == null) {
            return;
        }
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
    private void removeCardFromDeck(String deckName) {
        String name = promptInput("card name: ");
        INVENTORY_SYSTEM.removeCardFromDeck(deckName, name);
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
        VIEW.showCardDetails(c.toString());
    }
}
