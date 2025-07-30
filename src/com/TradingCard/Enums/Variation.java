package com.TradingCard.Enums;

/**
 * possible card artwork variations.
 * <ul>
 *   <li>NORMAL – base art</li>
 *   <li>EXTENDED_ART – +50% value</li>
 *   <li>FULL_ART – +100% value</li>
 *   <li>ALT_ART – +200% value</li>
 * </ul>
 */
public enum Variation {
    /** Standard card appearance. */
    NORMAL,

    /** Card featuring extended artwork beyond the frame. */
    EXTENDED_ART,

    /** Card printed as full‐art with minimal or no border. */
    FULL_ART,

    /** Alternate artwork variant, often a special or promotional print. */
    ALT_ART
}
