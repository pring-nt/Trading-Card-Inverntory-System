    package com.TradingCard;
    import com.TradingCard.Enums.BinderType;

    import java.math.BigDecimal;
    import java.util.*;

    /**
     * Manages the lifecycle and interactions of binders in the system.
     * Provides creation, deletion, selling, and card transfer functionality.
     */
    public class BinderManager {
        /**
         * The list of all binders currently tracked.
         */
        private final ArrayList<Binder> BINDERS;

        /**
         * Constructs a new BinderManager with an empty binder list.
         */
        public BinderManager() {
            this.BINDERS = new ArrayList<>();
        }

        /**
         * Finds a binder by its name.
         *
         * @param name the name of the binder to find
         * @return the matching Binder instance
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
         * Creates and adds a new binder of the specified type.
         *
         * @param name the name of the new binder
         * @param type the type of binder to create
         * @throws IllegalStateException if a binder with the same name already exists
         */
        public void createBinder(String name, BinderType type) {
            for (Binder b : BINDERS) {
                if (b.getName().equalsIgnoreCase(name)) {
                    throw new IllegalStateException("binder \"" + name + "\" already exists");
                }
            }

            Binder newBinder = switch (type) {
                case NON_CURATED -> new NonCuratedBinder(name);
                case PAUPER -> new PauperBinder(name);
                case RARES -> new RaresBinder(name);
                case LUXURY -> new LuxuryBinder(name);
                case COLLECTOR -> new CollectorBinder(name);
            };

            BINDERS.add(newBinder);
        }

        /**
         * Sets a custom price for a binder with the given name, if it is a LuxuryBinder.
         * This method only affects binders that are instances of {@code LuxuryBinder}.
         * Non-luxury binders are ignored.
         *
         * @param name         the name of the binder
         * @param customPrice  the custom price to assign
         *
         * @throws NullPointerException if the binder does not exist
         */
        public void setCustomPrice(String name, BigDecimal customPrice) {
            Binder b = findBinderByName(name);
            if (b instanceof LuxuryBinder luxuryBinder) {
                luxuryBinder.setCustomPrice(customPrice);
            }
        }

        /**
         * Checks if the binder with the specified name is a {@code LuxuryBinder}.
         *
         * @param name the name of the binder to check
         * @return {@code true} if the binder is an instance of {@code LuxuryBinder}, {@code false} otherwise
         *
         * @throws NullPointerException if no binder with the specified name exists
         */
        public boolean isLuxuryBinder(String name) {
            Binder b = findBinderByName(name);
            return b instanceof LuxuryBinder;
        }

        /**
         * Gets the custom price of a LuxuryBinder.
         *
         * @param name the name of the binder
         * @return the custom price set for the LuxuryBinder
         * @throws NoSuchElementException if no binder with the given name exists
         * @throws IllegalStateException if the binder is not a LuxuryBinder
         */
        public BigDecimal getLuxuryBinderCustomPrice(String name) {
            Binder binder = findBinderByName(name);
            if (!(binder instanceof LuxuryBinder luxuryBinder)) {
                throw new IllegalStateException("Binder \"" + name + "\" is not a LuxuryBinder.");
            }
            return luxuryBinder.getCustomPrice();
        }



        /**
         * Deletes a binder and returns all cards it contained.
         *
         * @param name the name of the binder to delete
         * @return the cards that were inside the binder
         * @throws NoSuchElementException if no binder with that name exists
         */
        public ArrayList<Card> deleteBinder(String name) {
            Binder target = findBinderByName(name);
            ArrayList<Card> cards = target.removeAllCards();
            this.BINDERS.remove(target);
            return cards;
        }

        /**
         * Removes a card from a binder by name.
         *
         * @param binderName the name of the binder to remove from
         * @param cardName the name of the card to remove
         * @return the removed Card instance
         * @throws NoSuchElementException if the binder does not exist
         */
        public Card removeCardFromBinder(String binderName, String cardName) {
            Binder tBinder = findBinderByName(binderName);
            return tBinder.removeCardByName(cardName);
        }

        /**
         * Adds a card to a binder.
         *
         * @param binderName the name of the target binder
         * @param card the card to add
         * @return null if successful; the card itself if adding failed
         * @throws NoSuchElementException if the binder does not exist
         */
        public Card addCardToBinder(String binderName, Card card) {
            Binder tBinder = findBinderByName(binderName);
            if (!tBinder.addCard(card)) {
                return card;
            }
            return null;
        }

        /**
         * Returns the names of all binders currently managed.
         *
         * @return a list of binder names
         */
        public ArrayList<String> getBinderNames() {
            ArrayList<String> binderNames = new ArrayList<>();
            for (Binder binder : this.BINDERS) {
                binderNames.add(binder.getName());
            }
            return binderNames;
        }

        /**
         * Sells a binder by name.
         *
         * @param name the name of the binder to sell
         * @return the total sale price, including any handling fee
         * @throws NoSuchElementException if no binder with that name exists
         * @throws IllegalStateException if the binder is not sellable
         */
        public BigDecimal sellBinder(String name) {
            Binder binder = findBinderByName(name);

            if (!binder.isSellable()) {
                throw new IllegalStateException("binder \"" + name + "\" cannot be sold");
            }

            BigDecimal price = ((Sellable) binder).sell();
            BINDERS.remove(binder);
            return price;
        }

        /**
         * Gets the current value of a binder without selling it.
         *
         * @param name the name of the binder
         * @return the binder's estimated value (excluding handling fees)
         * @throws NoSuchElementException if no binder with that name exists
         * @throws IllegalStateException if the binder is not sellable
         */
        public BigDecimal getBinderValue(String name) {
            Binder binder = findBinderByName(name);

            if (!binder.isSellable()) {
                throw new IllegalStateException("binder \"" + name + "\" cannot be sold");
            }

            return ((Sellable) binder).getValue();
        }

        /**
         * Checks if the binder with the given name is sellable.
         *
         * <p>This delegates to the binder's {@code isSellable()} method, which indicates
         * whether the binder supports being sold through the inventory system.</p>
         *
         * @param name the name of the binder to check
         * @return {@code true} if the binder is sellable; {@code false} otherwise
         * @throws NoSuchElementException if no binder with the given name exists
         */
        public boolean isBinderSellable(String name) {
            Binder binder = findBinderByName(name);
            return binder.isSellable();
        }

        /**
         * Checks if the binder with the given name is empty
         *
         * <p>This delegates to the binder's {@code isEmpty()} method, which indicates
         * whether the binder supports being sold through the inventory system.</p>
         *
         * @param name the name of the binder to check
         * @return {@code true} if the binder is empty; {@code false} otherwise
         * @throws NoSuchElementException if no binder with the given name exists
         */
        public boolean isBinderEmpty(String name) {
            Binder binder = findBinderByName(name);
            return binder.isEmpty();
        }
    }
