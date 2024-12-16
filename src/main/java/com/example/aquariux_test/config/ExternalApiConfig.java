package com.example.aquariux_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ExternalApiConfig {
    @Bean
    public WebClient binanceWebClient(WebClient.Builder builder) {
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
        return WebClient.builder().baseUrl("https://api.binance.com/api/v3").exchangeStrategies(strategies).build();
    }
}
