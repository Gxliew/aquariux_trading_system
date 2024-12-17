package com.example.aquariux_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.aquariux_test.entity.Trade;
import com.example.aquariux_test.request.CloseTradeRequest;
import com.example.aquariux_test.request.OpenTradeRequest;
import com.example.aquariux_test.service.TradeService;

@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/open-trade")
    public Trade openTrade(@RequestBody OpenTradeRequest request) {
        return tradeService.openTrade(request);
    }

    @PostMapping("/close-trade")
    public Trade closeTrade(@RequestBody CloseTradeRequest request) {
        return tradeService.closeTrade(request);
    }
}
