package com.kiing.spotsight.service.auth;

import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class SpotifyAuthService {
    
    private final WebClient webClient;

    public SpotifyAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://accounts.spotify.com").build();
    }
    
    public Mono<String> getAccessToken(String clientId, String clientSecret) {
        String auth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret.getBytes()));

        return webClient.post()
            .uri("/api/token")
            .header("Authorization", "Basic " + auth)
            .bodyValue("grant_type=client_credentials")
            .retrieve()
            .bodyToMono(String.class)
            .map(response -> parseAccessToken(response));
    }

    private String parseAccessToken(String response) {
        
    }
}
