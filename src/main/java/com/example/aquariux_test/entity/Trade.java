package com.example.aquariux_test.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.aquariux_test.enums.TradeStatus;
import com.example.aquariux_test.enums.TradeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "trades")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long symbolId;

    @Enumerated(EnumType.STRING)
    private TradeType type;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @Column(name = "lot_size", precision = 10, scale = 2)
    private BigDecimal lotSize;

    @Column(name = "open_price")
    private BigDecimal openPrice;

    @Column(name = "close_price")
    private BigDecimal closePrice;

    @Column(name = "open_at")
    private LocalDateTime openAt;

    @Column(name = "close_at")
    private LocalDateTime closeAt;

    private BigDecimal profit;
}
