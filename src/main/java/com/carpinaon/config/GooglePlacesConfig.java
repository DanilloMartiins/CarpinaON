package com.carpinaon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// Cliente HTTP pro Google Places API (buscar Place ID pelo endereço)
@Configuration
public class GooglePlacesConfig {

    @Value("${google.places.base-url}")
    private String baseUrl;

    @Value("${google.places.api-key}")
    private String apiKey;

    @Bean
    public WebClient googlePlacesClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}