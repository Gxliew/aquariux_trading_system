package com.example.aquariux_test.response;

import java.math.BigDecimal;

public record WalletBalanceResponse(
    Long userId,
    BigDecimal walletBalance
) {

}
