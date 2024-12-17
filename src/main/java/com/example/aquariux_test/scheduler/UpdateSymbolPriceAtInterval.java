package com.example.aquariux_test.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.aquariux_test.service.SymbolPriceService;

@Component
public class UpdateSymbolPriceAtInterval {
    @Autowired
    private SymbolPriceService symbolPriceService;

    @Scheduled(fixedRate = 10000)
    public void runTask() {
        symbolPriceService.updateSymbolPrice();
    }
}
