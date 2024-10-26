package com.kiing.spotsight.service.auth;


import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Service
public class SpotifyAuthService {
    
    private final WebClient webClient;

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.auth.url}")
    private String tokenUrl;

    public SpotifyAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://accounts.spotify.com").build();
    }
    
    public Mono<String> getAccessToken() {
        String authHeader = "Basic " +  Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        return webClient.post()
            .uri(tokenUrl)
            .header("Authorization", authHeader)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .bodyValue("grant_type=client_credentials")
            .retrieve()
            .bodyToMono(String.class)
            .map(this::parseAccessToken)
            .onErrorResume(WebClientResponseException.class, ex -> {
                System.err.println("Error fetching access token: " + ex.getMessage());
                return Mono.empty();
            });
            
    }

    private String parseAccessToken(String response) {

    }
}
