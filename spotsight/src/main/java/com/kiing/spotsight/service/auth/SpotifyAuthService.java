package com.kiing.spotsight.service.auth;


import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiing.spotsight.model.token.TokenResponse;

import reactor.core.publisher.Mono;

@Service
public class SpotifyAuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(SpotifyAuthService.class);
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
        logger.info("Preparing to retrieve access token from {}", tokenUrl);
        return webClient.post()
                .uri(tokenUrl) // Log the URL here
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnNext(uri -> logger.info("Using URI: {}", tokenUrl)) // Log the URI used
                .map(TokenResponse::getAccessToken)
                .doOnError(e -> logger.error("Error retrieving access token: {}", e.getMessage()));
    }
    
}
