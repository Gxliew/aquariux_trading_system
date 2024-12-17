package com.example.aquariux_test.request;

import java.math.BigDecimal;

import com.example.aquariux_test.enums.TradeType;

import lombok.NonNull;

public record OpenTradeRequest(
        @NonNull Long userId,
        @NonNull String symbol,
        @NonNull TradeType type,
        @NonNull BigDecimal lotSize) {

}
