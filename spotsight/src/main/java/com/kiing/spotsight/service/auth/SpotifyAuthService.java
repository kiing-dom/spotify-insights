package com.kiing.spotsight.service.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiing.spotsight.model.token.TokenResponse;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Service
public class SpotifyAuthService {

    private static final Logger logger = LoggerFactory.getLogger(SpotifyAuthService.class);
    private final WebClient webClient;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.auth-url}")
    private String tokenUrl;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    public SpotifyAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://accounts.spotify.com").build();
    }

    @PostConstruct
    private void init() {
        logger.info("Client ID: {}", clientId);
        logger.info("Client Secret: {}", clientSecret);
        logger.info("Token URL: {}", tokenUrl);
        
        if (clientId == null || clientSecret == null || tokenUrl == null) {
            logger.error("One or more required properties are null!");
            throw new IllegalStateException("Required Spotify properties are not properly initialized");
        }
    }

    public Mono<String> getAccessToken() {
        logger.info("Preparing to retrieve access token from {}", tokenUrl);
        return webClient.post()
                .uri(tokenUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnNext(uri -> logger.info("Using URI: {}", tokenUrl))
                .map(TokenResponse::getAccessToken)
                .doOnError(e -> logger.error("Error retrieving access token: {}", e.getMessage()));
    }

    public String getAuthorizationUrl() {
        String scope = "user-read private user-read-email user-top-read";
        return String.format("%s?clienit_id=%s&response_type=code&redirect_uri=%s&scope=%s",
                                "https://accounts.spotify.com/authorize", clientId, redirectUri, scope);
    }

    public void exchangeCodeForAccessToken(String code) {
        String encodedCredentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));

        try {
            TokenResponse tokenResponse = webClient.post()
                .uri("/api/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                                            .with("code", code)
                                            .with("redirect_uri", redirectUri))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
            
            String accessToken = tokenResponse.getAccessToken();
            logger.info("Access token retrieved successfully: {}", accessToken);
                // maybe use reactive streams later
        } catch (WebClientResponseException e) {
            logger.error("Error exchanging code for access token: Status - {}, Message - {}", e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
        }
    }
}