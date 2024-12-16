package com.example.aquariux_test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.aquariux_test.entity.SymbolPrice;
import com.example.aquariux_test.service.SymbolPriceService;

@RestController
@RequestMapping("/symbol-prices")
public class SymbolPriceController {

    @Autowired
    private SymbolPriceService symbolPriceService;

    @GetMapping
    public List<SymbolPrice> getBinanceSymbolsPrice() {
        return symbolPriceService.getAggregatedSymbolPrice();
    }
}
