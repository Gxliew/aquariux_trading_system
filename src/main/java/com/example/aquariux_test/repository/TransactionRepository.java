package com.example.aquariux_test.repository;

import com.example.aquariux_test.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}