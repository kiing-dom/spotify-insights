package com.kiing.spotsight.service.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SpotifyApiClient {

    private final WebClient webClient;

    public SpotifyApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.spotify.com/v1").build();
    }
}
