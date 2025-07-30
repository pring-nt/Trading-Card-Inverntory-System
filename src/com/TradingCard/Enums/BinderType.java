package com.TradingCard.Enums;

/**
 * types of trading card binders.
 * <ul>
 *   <li>NON_CURATED – accepts all cards, no rarity restrictions</li>
 *   <li>PAUPER – accepts only common and uncommon cards</li>
 *   <li>RARES – accepts only rare and legendary cards</li>
 *   <li>LUXURY – premium binder (reserved for future use)</li>
 *   <li>COLLECTOR – high-value collector binder (reserved for future use)</li>
 * </ul>
 */
public enum BinderType {
    NON_CURATED,
    PAUPER,
    RARES,
    LUXURY,
    COLLECTOR
}
