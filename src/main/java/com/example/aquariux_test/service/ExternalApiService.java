package com.example.aquariux_test.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.aquariux_test.dto.BinanceResponseDto;

@Service
public class ExternalApiService {
    private final WebClient webClient;

    public ExternalApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<BinanceResponseDto> getBinanceSymbolsPrice() {
        return webClient.get()
                .uri("/ticker/bookTicker")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BinanceResponseDto>>() {
                    
                })
                .block();
    }
}
