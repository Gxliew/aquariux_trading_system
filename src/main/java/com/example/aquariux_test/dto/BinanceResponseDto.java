package com.example.aquariux_test.dto;

public record BinanceResponseDto(
        String symbol,
        String bidPrice,
        String bidQty,
        String askPrice,
        String askQty) {

}
