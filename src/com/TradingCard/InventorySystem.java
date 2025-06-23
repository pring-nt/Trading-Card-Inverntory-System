package com.TradingCard;

import java.util.ArrayList;

public class InventorySystem {
    private final Collection collection;
    private final ArrayList<Deck> decks;
    private final ArrayList<Binder> binders;

    public InventorySystem() {
        this.collection = new Collection();
        this.decks = new ArrayList<>();
        this.binders = new ArrayList<>();
    }
}
