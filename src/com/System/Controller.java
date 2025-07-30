package com.System;

import com.TradingCard.*;
import com.TradingCard.Enums.BinderType;
import com.TradingCard.Enums.Rarity;
import com.TradingCard.Enums.Variation;

import java.math.BigDecimal;

/**
 * Controller for the Trading Card Inventory System (TCIS).
 * <p>
 * Implements CLI-driven control flow between the View and the InventorySystem model.
 * Handles user input, invokes model operations, and delegates display to the View.
 */
public class Controller {
    /**
     * The View component handling all CLI input/output.
     */
    private final View VIEW;

    /**
     * The core model instance for the enhanced Trading Card Inventory System.
     */
    private final EnhancedTCIS INVENTORY_SYSTEM;


    /**
     * Constructs a Controller with the specified View and InventorySystem.
     *
     * @param view   the View component for user I/O
     * @param system the InventorySystem model for business logic
     */
    public Controller(View view, EnhancedTCIS system) {
        this.VIEW = view;
        this.INVENTORY_SYSTEM = system;
    }

    /**
     * Runs the main application loop, displaying menus, processing user choices,
     * and handling exit confirmation.
     */
    public void run() {
        boolean exitFlag = false;
        while (!exitFlag) {
            boolean hasCards = !INVENTORY_SYSTEM.getCardCollection().getSortedCopy().isEmpty();
            boolean hasBinders = !INVENTORY_SYSTEM.getBinderNames().isEmpty();
            boolean hasDecks = !INVENTORY_SYSTEM.getDeckNames().isEmpty();
            VIEW.showMainMenu(hasCards, hasBinders, hasDecks, INVENTORY_SYSTEM.getCollectorEarnings());

            String choice = prompt();
            try {
                switch (choice) {
                    case "1" -> handleAddCard();
                    case "2" -> { if (!hasBinders) handleCreateBinder(); else handleManageBinderMenu(); }
                    case "3" -> { if (!hasDecks) handleCreateDeck(); else handleManageDeckMenu(); }
                    case "4" -> { if (hasCards) handleViewCollection(); else exitFlag = true; }
                    case "5" -> { if (hasCards) handleAdjustCount(); else invalid(); }
                    case "6" -> { if (hasCards) exitFlag = true; else invalid(); }
                    default  -> invalid();
                }
            } catch (Exception e) {
                VIEW.showError(e.getMessage());
            }

            if (exitFlag) {
                boolean confirm = VIEW.confirm("Are you sure you want to exit? (yes/no): ");
                if (!confirm) exitFlag = false;
            }
        }
        VIEW.closeScanner();
        VIEW.showMessage("closing program... goodbye!");
    }

    /**
     * Handles adding a card: prompts for name, rarity, variation, and value,
     * adds new card or increments existing count.
     *
     * @return the name of the added or incremented card, or null if aborted
     */
    private String handleAddCard() {
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
        Card c = new Card(name, rarity, var, val);
        INVENTORY_SYSTEM.addCardToCollection(c);
        VIEW.showMessage("card added: " + c.getName());
        return c.getName();
    }

    /**
     * Prompts to create a new Binder by name and type.
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
     * Prompts to create a new Deck by name and sellable flag.
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
                case "yes"      -> { sellable = true; exitFlag = true; }
                case "no"       -> exitFlag = true;
                default         -> invalid();
            }
        }
        INVENTORY_SYSTEM.createDeck(name, sellable);
        VIEW.showMessage("deck created: " + name);
    }

    /**
     * Shows collection and prompts to adjust the count of a selected card.
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
     * Displays collection list and submenu for viewing or selling cards.
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
                    } else {
                        VIEW.showCardDetails(c.toString());
                    }
                }
                case "2" -> {
                    cardName = promptInput("card name: ");
                    VIEW.showMessage("card value: $" + INVENTORY_SYSTEM.getCardValue(cardName));
                    if(VIEW.confirm("Are you sure you want to sell this card? (yes/no): ")) {
                        INVENTORY_SYSTEM.sellCard(cardName);
                    }
                }
                case "3" -> back = true;
                default -> invalid();
            }
        }
    }

    /**
     * Shows binder management menu and routes to create or view binders.
     */
    private void handleManageBinderMenu() {
        String choice;
        boolean back = false;
        while(!back) {
            VIEW.showManageBinderMenu();
            choice = prompt();
            switch (choice) {
                case "1" -> handleCreateBinder();
                case "2" -> { if(!INVENTORY_SYSTEM.getBinderNames().isEmpty()) handleViewBinder(); else VIEW.showError("there are no binders found"); }
                case "3" -> back = true;
                default -> invalid();
            }
        }
    }

    /**
     * Shows deck management menu and routes to create or view decks.
     */
    private void handleManageDeckMenu() {
        String choice;
        boolean back = false;
        while(!back) {
            VIEW.showManageDeckMenu();
            choice = prompt();
            switch (choice) {
                case "1" -> handleCreateDeck();
                case "2" -> { if(!INVENTORY_SYSTEM.getDeckNames().isEmpty()) handleViewDeck(); else VIEW.showError("there are no decks found"); }
                case "3" -> back = true;
                default -> invalid();
            }
        }
    }

    /**
     * Displays and operates on a specific binder's contents.
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
            if(sellable) VIEW.showMessage("binder's real value: $" + INVENTORY_SYSTEM.getBinderValue(binderName));
            if(isLuxury) VIEW.showMessage("binder's set value: $" + INVENTORY_SYSTEM.getLuxuryBinderCustomPrice(binderName));
            String opt = prompt();
            if(hasCard) {
                switch (opt) {
                    case "1" -> addCardToBinder(binderName);
                    case "2" -> removeCardFromBinder(binderName);
                    case "3" -> tradeInBinder(binderName);
                    case "4" -> viewBinder(binderName);
                    case "5" -> { if(VIEW.confirm("Are you sure you want to delete this binder? (yes/no): ")) INVENTORY_SYSTEM.deleteBinder(binderName); back = true; }
                    case "6" -> {
                        manageBinderPricingOrSell(binderName, sellable, isLuxury);
                        back = true;
                    }
                    case "7" -> { if(sellable && !isLuxury) back = true; else if(sellable && VIEW.confirm("Are you sure you want to sell this binder? (yes/no): ")) INVENTORY_SYSTEM.sellBinder(binderName); }
                    default -> invalid();
                }
            } else {
                switch (opt) {
                    case "1" -> addCardToBinder(binderName);
                    case "2" -> { INVENTORY_SYSTEM.deleteBinder(binderName); back = true; }
                    case "3" -> back = true;
                    default -> invalid();
                }
            }
        }
    }

    /**
     * Displays and operates on a specific deck's contents.
     */
    private void handleViewDeck() {
        VIEW.showDeckNames(INVENTORY_SYSTEM.getDeckNames());
        String deckName = promptInput("select deck: ");
        boolean back = false;
        while (!back) {
            boolean hasCard = !INVENTORY_SYSTEM.isDeckEmpty(deckName);
            boolean sellable = INVENTORY_SYSTEM.isDeckSellable(deckName);
            VIEW.showDeckMenu(deckName, hasCard, sellable);
            if(sellable) VIEW.showMessage("deck value: $" + INVENTORY_SYSTEM.getDeckValue(deckName));
            String opt = prompt();
            if (hasCard) {
                switch (opt) {
                    case "1" -> addCardToDeck(deckName);
                    case "2" -> removeCardFromDeck(deckName);
                    case "3" -> viewDeck(deckName);
                    case "4" -> { if(VIEW.confirm("Are you sure you want to delete this deck? (yes/no): ")) INVENTORY_SYSTEM.deleteDeck(deckName); back = true; }
                    case "5" -> { if(sellable && VIEW.confirm("Are you sure you want to sell this deck (yes/no): ")) INVENTORY_SYSTEM.sellDeck(deckName); back = true; }
                    case "6" -> { if(sellable) back = true; else invalid(); }
                    default -> invalid();
                }
            } else {
                switch (opt) {
                    case "1" -> addCardToDeck(deckName);
                    case "2" -> { INVENTORY_SYSTEM.deleteDeck(deckName); back = true; }
                    case "3" -> back = true;
                    default -> invalid();
                }
            }
        }
    }

    /**
     * Prompts the user for a menu selection.
     *
     * @return the user's selection as a String
     */
    private String prompt() {
        return VIEW.readLine("choose an option: ");
    }

    /**
     * Prompts with a custom message and returns the user's input.
     *
     * @param msg the message to display
     * @return the input entered by the user, or null if none
     */
    private String promptInput(String msg) {
        return VIEW.readLine(msg);
    }

    /**
     * Displays an error for invalid menu choices.
     */
    private void invalid() {
        VIEW.showError("invalid option");
    }

    /**
     * Adds a card from the collection to the specified binder.
     *
     * @param bName the name of the binder
     */
    private void addCardToBinder(String bName) {
        String cName = promptInput("card name: ");
        INVENTORY_SYSTEM.addCardToBinder(bName, cName);
        VIEW.showMessage("added to binder");
    }

    /**
     * Removes a card from a binder back into the collection.
     *
     * @param bName the name of the binder
     */
    private void removeCardFromBinder(String bName) {
        String cName = promptInput("card name: ");
        INVENTORY_SYSTEM.removeCardFromBinder(bName, cName);
        VIEW.showMessage("removed from binder");
    }

    /**
     * Displays the contents of the specified binder.
     *
     * @param bName the name of the binder
     */
    private void viewBinder(String bName) {
        VIEW.showBinder(INVENTORY_SYSTEM.findBinderByName(bName));
    }

    /**
     * Handles trading a card in a binder, removing one and adding another.
     *
     * @param bName the name of the binder
     */
    private void tradeInBinder(String bName) {
        String out = promptInput("outgoing name (or 'cancel' to abort): ");
        if (out == null || out.trim().equalsIgnoreCase("cancel")) {
            return;
        }
        String incomingName = handleAddCard();
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
     * Adds a card from the collection to the specified deck.
     *
     * @param deckName the name of the deck
     */
    private void addCardToDeck(String deckName) {
        String name = promptInput("card name: ");
        INVENTORY_SYSTEM.addCardToDeck(deckName, name);
        VIEW.showMessage("added to deck");
    }

    /**
     * Removes a card from a deck back into the collection.
     *
     * @param deckName the name of the deck
     */
    private void removeCardFromDeck(String deckName) {
        String name = promptInput("card name: ");
        INVENTORY_SYSTEM.removeCardFromDeck(deckName, name);
        VIEW.showMessage("removed from deck");
    }

    /**
     * Displays deck details and optionally views a specific card.
     *
     * @param deckName the name of the deck
     */
    private void viewDeck(String deckName) {
        Deck d = INVENTORY_SYSTEM.findDeckByName(deckName);
        VIEW.showDeck(d);
        if (!VIEW.confirm("do you want to view a specific card? (yes/no): ")) {
            return;
        }
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

    /**
     * Manages pricing or selling logic for a binder.
     *
     * @param binderName the binder to manage
     * @param sellable   whether the binder is sellable
     * @param isLuxury   whether the binder is luxury
     */
    private void manageBinderPricingOrSell(String binderName, boolean sellable, boolean isLuxury) {
        if (sellable) {
            if (isLuxury) {
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
    }
}
