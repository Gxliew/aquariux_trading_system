package com.example.aquariux_test.repository;

import com.example.aquariux_test.entity.Trade;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    Optional<Trade> findByIdAndUserId(Long id, Long userId);

    List<Trade> findByUserIdOrderByOpenAtDesc(Long userId);
}