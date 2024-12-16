package com.example.aquariux_test.repository;

import com.example.aquariux_test.entity.SymbolPrice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolPriceRepository extends JpaRepository<SymbolPrice, Long> {
    public Optional<SymbolPrice> findBySymbol(String symbol);
}