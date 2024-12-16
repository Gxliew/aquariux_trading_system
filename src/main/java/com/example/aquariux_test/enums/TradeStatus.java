package com.example.aquariux_test.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TradeStatus {
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private final String value;
}
