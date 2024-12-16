package com.example.aquariux_test.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquariux_test.dto.BinanceResponseDto;
import com.example.aquariux_test.entity.SymbolPrice;
import com.example.aquariux_test.repository.SymbolPriceRepository;

@Service
public class SymbolPriceService {

    @Autowired
    private ExternalApiService externalApiService;

    @Autowired
    private SymbolPriceRepository symbolPriceRepository;

    public List<SymbolPrice> getAggregatedSymbolPrice() {
        return symbolPriceRepository.findAll();
    }

    public void updateSymbolPrice() {
        List<BinanceResponseDto> symbols = this.getLatestSymbolPrices();
        System.out.println("All symbols:" + symbols.stream().map(e -> e.symbol()).toList());
        for (BinanceResponseDto symbol : symbols) {
            Optional<SymbolPrice> symbolPriceOpt = symbolPriceRepository.findBySymbol(symbol.symbol());
            if (symbolPriceOpt.isPresent()) {
                SymbolPrice updatedSymbolPrice = symbolPriceOpt.get();
                updatedSymbolPrice.setAskPrice(new BigDecimal(symbol.askPrice()));
                updatedSymbolPrice.setBidPrice(new BigDecimal(symbol.bidPrice()));
                symbolPriceRepository.save(updatedSymbolPrice);
            } else {
                SymbolPrice symbolPrice = SymbolPrice.builder().symbol(symbol.symbol())
                        .askPrice(new BigDecimal(symbol.askPrice())).bidPrice(new BigDecimal(symbol.bidPrice()))
                        .build();
                symbolPriceRepository.save(symbolPrice);
            }

        }
    }

    private List<BinanceResponseDto> getLatestSymbolPrices() {
        List<BinanceResponseDto> symbols = externalApiService.getBinanceSymbolsPrice();

        return getRequiredSymbols(symbols);
    }

    private List<BinanceResponseDto> getRequiredSymbols(List<BinanceResponseDto> rawResponseSymbols) {
        List<String> requiredSymbolNames = List.of("BTCUSDT", "ETHUSDT");
        return rawResponseSymbols.stream().filter(e -> requiredSymbolNames.contains(e.symbol())).toList();
    }
}
