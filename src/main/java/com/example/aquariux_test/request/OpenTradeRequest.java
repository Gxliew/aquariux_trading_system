package com.example.aquariux_test.request;

import java.math.BigDecimal;

import com.example.aquariux_test.enums.TradeType;

public record OpenTradeRequest(
    Long userId,
    String symbol,
    TradeType type,
    BigDecimal lotSize
) {

}
