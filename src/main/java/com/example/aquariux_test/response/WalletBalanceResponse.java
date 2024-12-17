package com.example.aquariux_test.response;

import java.math.BigDecimal;
import java.util.List;

import com.example.aquariux_test.entity.Transaction;

public record WalletBalanceResponse(
    Long userId,
    BigDecimal walletBalance,
    List<Transaction> transactions
) {

}
