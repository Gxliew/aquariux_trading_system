package com.example.aquariux_test.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.aquariux_test.response.WalletBalanceResponse;

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

    @Autowired
    private UserService userService;

    @SneakyThrows
    public Trade openTrade(OpenTradeRequest request) {
        Optional<User> userOpt = userRepository.findById(request.userId());
        if (userOpt.isEmpty()) {
            throw new BadRequestException("User not found!");
        }

        Optional<SymbolPrice> symbolPriceOpt = symbolPriceRepository.findBySymbol(request.symbol());

        if (symbolPriceOpt.isEmpty()) {
            throw new BadRequestException("Symbol price not found!");
        }

        WalletBalanceResponse walletBalanceResponse = userService.getUserWalletBalance(request.userId());
        BigDecimal openPrice = TradeType.BUY.equals(request.type()) ? symbolPriceOpt.get().getAskPrice()
                : symbolPriceOpt.get().getBidPrice();

        BigDecimal requiredWalletBalance = request.lotSize().multiply(openPrice);
        if (requiredWalletBalance.compareTo(walletBalanceResponse.walletBalance()) >= 0) {
            throw new BadRequestException("Not sufficient wallet balance!");
        }
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
                .creditAmount(requiredWalletBalance)
                .build();

        transactionRepository.save(transaction);
        return trade;
    }

    @SneakyThrows
    public Trade closeTrade(CloseTradeRequest request) {
        Optional<User> userOpt = userRepository.findById(request.userId());
        Optional<Trade> tradeOpt = tradeRepository.findByIdAndUserId(request.tradeId(), request.userId());
        if (userOpt.isEmpty()) {
            throw new BadRequestException("User not found!");
        }

        if (tradeOpt.isEmpty() || !TradeStatus.OPEN.equals(tradeOpt.get().getStatus())) {
            throw new BadRequestException("Invalid trade to close!");
        }

        Optional<SymbolPrice> symbolPriceOpt = symbolPriceRepository.findById(tradeOpt.get().getSymbolId());

        if (symbolPriceOpt.isEmpty()) {
            throw new BadRequestException("Symbol price not found!");
        }

        Trade trade = tradeOpt.get();

        BigDecimal closePrice = TradeType.BUY.equals(trade.getType()) ? symbolPriceOpt.get().getAskPrice()
                : symbolPriceOpt.get().getBidPrice();

        BigDecimal profit = this.calculateProfit(trade, closePrice);
        trade.setClosePrice(closePrice);
        trade.setCloseAt(LocalDateTime.now());
        trade.setProfit(profit);
        trade.setStatus(TradeStatus.CLOSED);

        tradeRepository.save(trade);
        
        Transaction transaction = Transaction.builder()
                .userId(request.userId())
                .debitAmount((trade.getLotSize().multiply(trade.getOpenPrice())).add(profit))
                .creditAmount(BigDecimal.ZERO)
                .build();

        transactionRepository.save(transaction);

        return trade;
    }

    private BigDecimal calculateProfit(Trade trade, BigDecimal closePrice) {
        try {
            BigDecimal profitPerLot = TradeType.BUY.equals(trade.getType())
                    ? (closePrice.subtract(trade.getOpenPrice()))
                    : (trade.getOpenPrice().subtract(closePrice));

            return trade.getLotSize().multiply(profitPerLot);
        } catch (Exception e) {
            System.out.println("Exception calculating profit:" + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
