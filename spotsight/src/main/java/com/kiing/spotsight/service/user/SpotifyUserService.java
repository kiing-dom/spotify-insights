package com.kiing.spotsight.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kiing.spotsight.model.user.UserProfile;

import reactor.core.publisher.Mono;

@Service
public class SpotifyUserService {
    
    private static final Logger logger = LoggerFactory.getLogger(SpotifyUserService.class);
    private WebClient webClient;

    public SpotifyUserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.spotify.com/v1").build();
    }

    public Mono<UserProfile> getUserProfile(String accessToken) {

        return webClient.get()
            .uri("/me")
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(UserProfile.class)
            .doOnSuccess(profile -> logger.info("Successfully retrieved user profile for: {}", profile.getDisplayName()))
            .doOnError(e -> logger.error("Error retrieving user profile: {}", e.getMessage()));
    }
}
