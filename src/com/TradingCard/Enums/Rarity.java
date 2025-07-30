package com.TradingCard.Enums;

/**
 * Rarity levels for trading cards, indicating frequency and base value tier.
 * <ul>
 *   <li>COMMON – most frequently found, lowest value tier</li>
 *   <li>UNCOMMON – less common, moderate value tier</li>
 *   <li>RARE – seldom found, higher value tier</li>
 *   <li>LEGENDARY – exceptionally rare, top value tier</li>
 * </ul>
 */
public enum Rarity {
    /** The most commonly printed cards. */
    COMMON,

    /** Less common than COMMON, but not rare. */
    UNCOMMON,

    /** Produced in limited quantities and harder to find. */
    RARE,

    /** Extremely scarce, often highly valuable or promotional. */
    LEGENDARY
}
