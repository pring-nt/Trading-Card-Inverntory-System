package com.TradingCard;

import java.math.BigDecimal;

/**
 * Interface for objects that can be evaluated for value and sold.
 * <p>
 * Implementing classes must define how their value is calculated
 * and what happens when a sale occurs.
 */
public interface Sellable {
    /**
     * Sells the object, typically clearing its contents or marking it as sold,
     * and returns the total earnings from the sale.
     *
     * @return total earnings from the sale as {@link BigDecimal}
     */
    BigDecimal sell();

    /**
     * Computes the current value of the object without modifying it.
     *
     * @return current value as {@link BigDecimal}
     */
    BigDecimal getValue();
}
