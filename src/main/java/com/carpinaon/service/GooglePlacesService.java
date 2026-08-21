package com.carpinaon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                GooglePlacesResponse.Candidate primeiro = response.getCandidates().get(0);
                return new PlaceIdResponse(
                        primeiro.getPlaceId(),
                        primeiro.getName(),
                        primeiro.getFormattedAddress()
                );
            }
            return null; // não achou
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Erro ao chamar Google Places API: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar Place ID: " + e.getMessage());
        }
    }

    // Response do Google Places API
    public static class GooglePlacesResponse {
        private java.util.List<Candidate> candidates;
        private String status;

        public java.util.List<Candidate> getCandidates() { return candidates; }
        public void setCandidates(java.util.List<Candidate> candidates) { this.candidates = candidates; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public static class Candidate {
            private String placeId;
            private String name;
            private String formattedAddress;

            public String getPlaceId() { return placeId; }
            public void setPlaceId(String placeId) { this.placeId = placeId; }
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            public String getFormattedAddress() { return formattedAddress; }
            public void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress; }
        }
    }

    // Resposta pro controller
    public record PlaceIdResponse(String placeId, String nome, String enderecoFormatado) {}
}