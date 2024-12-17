package com.example.aquariux_test.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.aquariux_test.entity.SymbolPrice;
import com.example.aquariux_test.entity.Trade;
import com.example.aquariux_test.entity.Transaction;
import com.example.aquariux_test.entity.User;
import com.example.aquariux_test.enums.TradeStatus;
import com.example.aquariux_test.enums.TradeType;
import com.example.aquariux_test.repository.SymbolPriceRepository;
import com.example.aquariux_test.repository.TradeRepository;
import com.example.aquariux_test.repository.TransactionRepository;
import com.example.aquariux_test.repository.UserRepository;
import com.example.aquariux_test.request.CloseTradeRequest;
import com.example.aquariux_test.request.OpenTradeRequest;

import lombok.SneakyThrows;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SymbolPriceRepository symbolPriceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @SneakyThrows
    public Trade openTrade(OpenTradeRequest request) {
        Optional<User> userOpt = userRepository.findById(request.userId());
        if (!userOpt.isPresent()) {
            throw new BadRequestException("User not found!");
        }

        Optional<SymbolPrice> symbolPriceOpt = symbolPriceRepository.findBySymbol(request.symbol());

        if (!symbolPriceOpt.isPresent()) {
            throw new BadRequestException("Symbol price not found!");
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

        Transaction transaction = Transaction.builder()
                .userId(request.userId())
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(openPrice)
                .build();

        transactionRepository.save(transaction);
        return trade;
    }

    @SneakyThrows
    public Trade closeTrade(CloseTradeRequest request) {
        Optional<User> userOpt = userRepository.findById(request.userId());
        Optional<Trade> tradeOpt = tradeRepository.findByIdAndUserId(request.tradeId(), request.userId());
        if (!userOpt.isPresent()) {
            throw new BadRequestException("User not found!");
        }

        if (!tradeOpt.isPresent() || !TradeStatus.OPEN.equals(tradeOpt.get().getStatus())) {
            throw new BadRequestException("Invalid trade to close!");
        }

        Optional<SymbolPrice> symbolPriceOpt = symbolPriceRepository.findById(tradeOpt.get().getSymbolId());

        if (!symbolPriceOpt.isPresent()) {
            throw new BadRequestException("Symbol price not found!");
        }

        Trade trade = tradeOpt.get();

        BigDecimal closePrice = TradeType.BUY.equals(trade.getType()) ? symbolPriceOpt.get().getAskPrice()
                : symbolPriceOpt.get().getBidPrice();

        BigDecimal profit = this.calculateProfit(trade);
        trade.setClosePrice(closePrice);
        trade.setCloseAt(LocalDateTime.now());
        trade.setProfit(profit);
        trade.setStatus(TradeStatus.CLOSED);

        tradeRepository.save(trade);

        Transaction transaction = Transaction.builder()
                .userId(request.userId())
                .debitAmount(profit.compareTo(BigDecimal.ZERO) >= 0 ? profit : BigDecimal.ZERO)
                .creditAmount(profit.compareTo(BigDecimal.ZERO) <= 0 ? profit.abs() : BigDecimal.ZERO)
                .build();

        transactionRepository.save(transaction);

        return trade;
    }

    private BigDecimal calculateProfit(Trade trade) {
        // TO DO
        return BigDecimal.ZERO;
    }
}
