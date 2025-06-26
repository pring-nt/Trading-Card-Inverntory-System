package com.System;
import com.TradingCard.*;
import com.TradingCard.Enums.Rarity;
import com.TradingCard.Enums.Variation;

import java.math.BigDecimal;

public class Controller {
    private final View VIEW;
    private final InventorySystem INVENTORY_SYSTEM;

    public Controller(View v, InventorySystem i) {
        this.VIEW = v;
        this.INVENTORY_SYSTEM = i;
    }

    public void run() {
        boolean exitFlag = false;
        while (!exitFlag) {
            boolean hasCards   = !INVENTORY_SYSTEM.getCardCollection().getSortedCopy().isEmpty();
            boolean hasBinders = !INVENTORY_SYSTEM.getBinderNames().isEmpty();
            boolean hasDecks   = !INVENTORY_SYSTEM.getDeckNames().isEmpty();

            VIEW.showMainMenu(hasCards, hasBinders, hasDecks);
            String choice = prompt();
            try {
                switch (choice) {
                    case "1" -> handleAddCard(); // show add card option
                    case "2" -> { // show a binder option
                        if (!hasBinders) {
                            handleCreateBinder();
                        } else {
                            handleManageBinderMenu();
                        }
                    }
                    case "3" -> { // show a deck option
                        if (!hasDecks) {
                            handleCreateDeck();
                        } else {
                            handleManageDeckMenu();
                        }
                    }
                    case "4" -> { //
                        if (hasCards) {
                            handleViewCollection();
                        } else {
                            exitFlag = true;
                        }
                    }
                    case "5" -> {
                        if (hasCards) {
                            handleAdjustCount();
                        } else {
                            invalid();
                        }
                    }
                    case "6" -> {
                        if (hasCards) {
                            exitFlag = true;
                        } else {
                            invalid();
                        }
                    }
                    default -> invalid();
                }
            } catch (Exception e) {
                VIEW.showError(e.getMessage());
            }

            if(exitFlag) {
                if(!VIEW.confirm("are you sure? (yes/no): ")) {
                    exitFlag = false;
                }
            }
        }
        VIEW.closeScanner();
        VIEW.showMessage("closing program...\ngoodbye!");
    }

    private Card handleAddCard() {
        String name = promptInput("card name: ");
        Card existing = INVENTORY_SYSTEM.findCardByNameInCollection(name);
        if (existing != null) {
            VIEW.showMessage("card already exists in collection");
            if (VIEW.confirm("do you want to increment the count of this card instead? (yes/no): ")) {
                INVENTORY_SYSTEM.incrementCardInCollection(name);
                return existing;
            }
            else return null;
        }
        Rarity rarity = promptRarity();
        Variation var = promptVariation(rarity);
        BigDecimal val = promptBaseValue();
        Card c = new Card(name, rarity, var, val);
        INVENTORY_SYSTEM.addCardToCollection(c);
        VIEW.showMessage("card added: " + c.getName());
        return c;
    }

    private void handleCreateBinder() {
        String name = promptInput("binder name: ");
        INVENTORY_SYSTEM.createBinder(name);
        VIEW.showMessage("binder created: " + name);
    }

    private void handleCreateDeck() {
        String name = promptInput("deck name: ");
        INVENTORY_SYSTEM.createDeck(name);
        VIEW.showMessage("deck created: " + name);
    }

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
                case "2" -> back = true;
                default -> invalid();
            }
        }
    }

    private void handleManageBinderMenu() {
        String choice;
        boolean back = false;
        while(!back) {
            VIEW.showManageBinderMenu();
            choice = prompt();
            switch (choice) {
                case "1" -> handleCreateBinder();
                case "2" -> handleViewBinder();
                case "3" -> back = true;
                default -> invalid();
            }
        }
    }

    private void handleManageDeckMenu() {
        String choice;
        boolean back = false;
        while(!back) {
            VIEW.showManageDeckMenu();
            choice = prompt();
            switch (choice) {
                case "1" -> handleCreateDeck();
                case "2" -> handleViewDeck();
                case "3" -> back = true;
                default -> invalid();
            }
        }
    }

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
            } else {
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
            } else {
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

    private String prompt() {
        return VIEW.prompt("choose an option: ");
    }

    private String promptInput(String s) {
        return VIEW.prompt(s);
    }

    private void invalid() {
        VIEW.showError("invalid option");
    }

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

    private BigDecimal promptBaseValue() {
        while (true) {
            try {
                return new BigDecimal(promptInput("base value: "));
            } catch (NumberFormatException e) {
                VIEW.showError("invalid number");
            }
        }
    }
    private void addCardToBinder(String bName) {
        String name = promptInput("card name> ");
        INVENTORY_SYSTEM.addCardToBinder(bName, name);
        VIEW.showMessage("added to binder");
    }

    private void removeCardFromBinder(String bName) {
        String name = promptInput("card name> ");
        INVENTORY_SYSTEM.removeCardFromBinder(bName, name);
        VIEW.showMessage("removed from binder");
    }

    private void viewBinder(String bName) {
        VIEW.showBinder(INVENTORY_SYSTEM.findBinderByName(bName));
    }

    private void tradeInBinder(String bName) {
        String out = promptInput("outgoing name: ");
        Card incoming = handleAddCard(); // reuse prompts for incoming card
        boolean proceed = INVENTORY_SYSTEM.tradeCard(bName, out, incoming, false);
        if (!proceed) {
            boolean yes = VIEW.confirm("value difference ≥ $1, proceed? (yes/no): ");
            INVENTORY_SYSTEM.tradeCard(bName, out, incoming, yes);
        }
        VIEW.showMessage("trade completed");
    }

    private void addCardToDeck(String deckName) {
        String name = promptInput("card name: ");
        INVENTORY_SYSTEM.addCardToDeck(deckName, name);
        VIEW.showMessage("added to deck");
    }

    private void deleteCardFromDeck(String deckName) {
        String name = promptInput("card name: ");
        INVENTORY_SYSTEM.deleteCardFromDeck(deckName, name);
        VIEW.showMessage("removed from deck");
    }

    private void viewDeck(String deckName) {
        Deck d = INVENTORY_SYSTEM.findDeckByName(deckName);
        VIEW.showDeck(d);

        if(!VIEW.confirm("do you want to view a specific card? (yes/no): ")) {
            return;
        }

        String choice = promptInput("view a card by number or name: ");
        Card c;

        if (choice.matches("\\d+")) {
            int idx = Integer.parseInt(choice) - 1;
            if (idx < 0 || idx >= d.getCopyOfCards().size()) {
                VIEW.showError("invalid card number");
                return;
            }
            c = d.getCardAtIndex(idx);
        } else {
            c = d.findByCardName(choice);
            if (c == null) {
                VIEW.showError("card \"" + choice + "\" not found in deck");
                return;
            }
        }

        VIEW.showCardDetails(c);
    }
}
