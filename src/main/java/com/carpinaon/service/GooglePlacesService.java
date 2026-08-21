package com.carpinaon.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.List;
import reactor.core.publisher.Mono;

// Service pra buscar Place ID no Google Places API (Find Place from Text)
@Service
public class GooglePlacesService {

    @Autowired
    private WebClient googlePlacesClient;

    @Value("${google.places.api-key}")
    private String apiKey;

    // Busca Place ID pelo endereço (text query)
    public PlaceIdResponse buscarPlaceId(String endereco) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Google Places API Key não configurada");
        }

        try {
            GooglePlacesResponse response = googlePlacesClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/findplacefromtext/json")
                            .queryParam("input", endereco)
                            .queryParam("inputtype", "textquery")
                            .queryParam("fields", "place_id,name,formatted_address")
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(GooglePlacesResponse.class)
                    .block();

            if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
                GooglePlacesResponse.Candidate primeiro = response.candidates().get(0);
                return new PlaceIdResponse(
                        primeiro.placeId(),
                        primeiro.name(),
                        primeiro.formattedAddress()
                );
            }
            return null; // não achou
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Erro ao chamar Google Places API: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar Place ID: " + e.getMessage());
        }
    }

    // Response do Google Places API (só os campos que a gente usa)
    public record GooglePlacesResponse(List<Candidate> candidates, String status) {
        public record Candidate(String placeId, String name,
                                @JsonProperty("formatted_address") String formattedAddress) {}
    }

    // Resposta pro controller
    public record PlaceIdResponse(String placeId, String nome, String enderecoFormatado) {}
}