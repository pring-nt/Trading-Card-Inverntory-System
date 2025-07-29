package com.TradingCard;
import com.TradingCard.Enums.BinderType;

import java.math.BigDecimal;
import java.util.*;

public class BinderManager {
    private final ArrayList<Binder> BINDERS;

    public BinderManager() {
        this.BINDERS = new ArrayList<>();
    }

    /**
     * Find a Binder by its name.
     *
     * @param name name of the binder to find
     * @return the Binder instance
     * @throws NoSuchElementException if no binder with that name exists
     */
    public Binder findBinderByName(String name) {
        for (Binder binder : this.BINDERS) {
            if (binder.getName().equalsIgnoreCase(name))
                return binder;
        }
        throw new NoSuchElementException("binder \"" + name + "\" not found");
    }

    /**
     * Create a new Binder of the given type and add it to the system.
     *
     * @param name name for the new binder
     * @param type which kind of binder to make
     * @throws IllegalStateException if a binder with that name already exists
     */
    public void createBinder(String name, BinderType type) {
        // checking for name duplicates
        for (Binder b : BINDERS) {
            if (b.getName().equalsIgnoreCase(name)) {
                throw new IllegalStateException("binder \"" + name + "\" already exists");
            }
        }

        // 2) instantiate the right subclass
        Binder newBinder = switch (type) {
            case NON_CURATED -> new NonCuratedBinder(name);
            case PAUPER -> new PauperBinder(name);
            case RARES -> new RaresBinder(name);
            case LUXURY -> new LuxuryBinder(name);
            case COLLECTOR -> new CollectorBinder(name);
        };

        // 3) add it
        BINDERS.add(newBinder);
    }

    /**
     * Delete a Binder, returning all its cards to the main collection.
     *
     * @param name name of the binder to delete
     * @return the removed cards
     * @throws NoSuchElementException if binder not found
     */
    public ArrayList<Card> deleteBinder(String name) {
        Binder target = findBinderByName(name);
        ArrayList<Card> cards = target.removeAllCards();
        this.BINDERS.remove(target);
        return cards;
    }

    /**
     * Remove a single card from a binder and return it to the collection.
     *
     * @param binderName name of binder to remove from
     * @param cardName   name of card to remove
     * @return the removed Card instance
     */
    public Card removeCardFromBinder(String binderName, String cardName) {
        Binder tBinder = findBinderByName(binderName);
        return tBinder.removeCardByName(cardName);
    }

    /**
     * Remove a card from the collection and add it into a binder slot.
     *
     * @param binderName name of binder
     * @param card       card being added to the binder
     * @return null if successful, the card if not
     */
    public Card addCardToBinder(String binderName, Card card) {
        Binder tBinder = findBinderByName(binderName);
        if (!tBinder.addCard(card)) { // add card failed
            return card;
        }
        return null; // added card successfully
    }

    /**
     * Retrieve names of all binders in the system.
     *
     * @return list of binder names
     */
    public ArrayList<String> getBinderNames() {
        ArrayList<String> binderNames = new ArrayList<>();
        for (Binder binder : this.BINDERS) {
            binderNames.add(binder.getName());
        }
        return binderNames;
    }

    /**
     * Sell a binder by name.
     *
     * @param name name of the binder to sell
     * @return the gross sale price (including any handling fee)
     * @throws NoSuchElementException if no binder with that name exists
     * @throws IllegalStateException  if the binder is not sellable
     */
    public BigDecimal sellBinder(String name) {
        // Find the binder (throw if it is not found)
        Binder binder = findBinderByName(name);

        // check if it is sellable, only Sellable binders can be sold
        if (!(binder instanceof Sellable sellable)) {
            throw new IllegalStateException("binder \"" + name + "\" cannot be sold");
        }

        BigDecimal price = sellable.sell();

        // remove the binder from the BINDERS array
        BINDERS.remove(binder);

        // Return the amount earned
        return price;
    }
}
