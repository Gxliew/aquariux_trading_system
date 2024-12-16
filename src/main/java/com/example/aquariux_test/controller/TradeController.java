package com.example.aquariux_test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.aquariux_test.entity.SymbolPrice;
import com.example.aquariux_test.entity.Trade;
import com.example.aquariux_test.request.CloseTradeRequest;
import com.example.aquariux_test.request.OpenTradeRequest;
import com.example.aquariux_test.service.SymbolPriceService;
import com.example.aquariux_test.service.TradeService;

@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/open-trade")
    public ResponseEntity<Trade> openTrade(@RequestBody OpenTradeRequest request) {
        return tradeService.openTrade(request);
    }

    @PostMapping("/close-trade")
    public ResponseEntity<Trade> closeTrade(@RequestBody CloseTradeRequest request) {
        return tradeService.closeTrade(request);
    }
}
