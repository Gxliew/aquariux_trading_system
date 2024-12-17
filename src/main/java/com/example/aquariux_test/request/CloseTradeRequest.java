package com.example.aquariux_test.request;

import lombok.NonNull;

public record CloseTradeRequest(
        @NonNull Long userId,
        @NonNull Long tradeId) {

}
