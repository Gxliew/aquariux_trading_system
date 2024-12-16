package com.example.aquariux_test.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.aquariux_test.entity.SymbolPrice;
import com.example.aquariux_test.entity.Trade;
import com.example.aquariux_test.entity.User;
import com.example.aquariux_test.enums.TradeStatus;
import com.example.aquariux_test.enums.TradeType;
import com.example.aquariux_test.repository.SymbolPriceRepository;
import com.example.aquariux_test.repository.TradeRepository;
import com.example.aquariux_test.repository.UserRepository;
import com.example.aquariux_test.request.CloseTradeRequest;
import com.example.aquariux_test.request.OpenTradeRequest;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    SymbolPriceRepository symbolPriceRepository;

    public ResponseEntity<Trade> openTrade(OpenTradeRequest request) {
        Optional<User> userOpt = userRepository.findById(request.userId());
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body(Trade.builder().build());
        }

        Optional<SymbolPrice> symbolPriceOpt = symbolPriceRepository.findBySymbol(request.symbol());

        if (!symbolPriceOpt.isPresent()) {
            return ResponseEntity.badRequest().body(Trade.builder().build());
        }

        BigDecimal openPrice = TradeType.BUY.equals(request.type()) ? symbolPriceOpt.get().getAskPrice()
                : symbolPriceOpt.get().getBidPrice();
        Trade trade = Trade.builder()
                        .userId(request.userId())
                        .type(request.type())
                        .status(TradeStatus.OPEN)
                        .lotSize(request.lotSize())
                        .openPrice(openPrice)
                        .openAt(LocalDateTime.now())
                        .symbolId(symbolPriceOpt.get().getId())
                        .build();

        tradeRepository.save(trade);

        return ResponseEntity.ok().body(trade);
    }

    public ResponseEntity<Trade> closeTrade(CloseTradeRequest request) {
        Optional<User> userOpt = userRepository.findById(request.userId());
        Optional<Trade> tradeOpt = tradeRepository.findByIdAndUserId(request.tradeId(), request.userId());
        if (!userOpt.isPresent() || !tradeOpt.isPresent() || (tradeOpt.isPresent() && !tradeOpt.get().getStatus().equals(TradeStatus.OPEN))) {
            return ResponseEntity.badRequest().body(Trade.builder().build());
        }

        Optional<SymbolPrice> symbolPriceOpt = symbolPriceRepository.findById(tradeOpt.get().getSymbolId());

        if (!symbolPriceOpt.isPresent()) {
            return ResponseEntity.badRequest().body(Trade.builder().build());
        }

        Trade trade = tradeOpt.get();

        BigDecimal closePrice = TradeType.BUY.equals(trade.getType()) ? symbolPriceOpt.get().getAskPrice()
        : symbolPriceOpt.get().getBidPrice();
        trade.setClosePrice(closePrice);
        trade.setCloseAt(LocalDateTime.now());
        trade.setProfit(this.calculateProfit(trade));
        trade.setStatus(TradeStatus.CLOSED);

        tradeRepository.save(trade);

        return ResponseEntity.ok().body(trade);
    }

    private BigDecimal calculateProfit(Trade trade) {
        //TO DO
        return BigDecimal.ZERO;
    }
}
