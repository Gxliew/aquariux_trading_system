package com.example.aquariux_test.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquariux_test.entity.Trade;
import com.example.aquariux_test.entity.Transaction;
import com.example.aquariux_test.entity.User;
import com.example.aquariux_test.exception.CustomBadRequestException;
import com.example.aquariux_test.repository.TradeRepository;
import com.example.aquariux_test.repository.TransactionRepository;
import com.example.aquariux_test.repository.UserRepository;
import com.example.aquariux_test.request.UserRequest;
import com.example.aquariux_test.response.WalletBalanceResponse;
import lombok.SneakyThrows;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TradeRepository tradeRepository;

    public boolean createUser(UserRequest userRequest) {
        User user = User.builder().name(userRequest.name()).email(userRequest.email()).build();

        userRepository.save(user);

        Transaction transaction = Transaction.builder()
                .userId(user.getId())
                .debitAmount(new BigDecimal(50000))
                .creditAmount(BigDecimal.ZERO)
                .build();

        transactionRepository.save(transaction);
        return true;
    }

    @SneakyThrows
    public WalletBalanceResponse getUserWalletBalance(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new CustomBadRequestException("User not found!");
        }

        List<Transaction> transactions = transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);

        BigDecimal totalDebitAmount = transactions.stream()
                .map(e -> Objects.nonNull(e.getDebitAmount()) ? e.getDebitAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCreditAmount = transactions.stream()
                .map(e -> Objects.nonNull(e.getCreditAmount()) ? e.getCreditAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal walletBalance = totalDebitAmount.subtract(totalCreditAmount);

        return new WalletBalanceResponse(userId,
                walletBalance.compareTo(BigDecimal.ZERO) >= 0 ? walletBalance : BigDecimal.ZERO, transactions);
    }

    @SneakyThrows
    public List<Trade> getUserTradeHistory(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new CustomBadRequestException("User not found!");
        }

        return tradeRepository.findByUserIdOrderByOpenAtDesc(userId);
    }
}
