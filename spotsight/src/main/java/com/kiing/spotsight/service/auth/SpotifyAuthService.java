package com.kiing.spotsight.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SpotifyAuthService {
    
    private final WebClient webClient;

    public SpotifyAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://accounts.spotify.com").build();
    }
    
}
