package com.example.aquariux_test.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TradeType {
    BUY("BUY"),
    SELL("SELL");

    private final String value;
}
