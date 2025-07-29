package com.TradingCard;

import java.math.BigDecimal;

public interface Sellable {
    BigDecimal sell();
    BigDecimal getValue();
}
