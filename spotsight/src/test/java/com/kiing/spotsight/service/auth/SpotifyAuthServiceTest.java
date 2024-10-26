package com.kiing.spotsight.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class SpotifyAuthServiceTest {
    
    private SpotifyAuthService spotifyAuthService;

    @Value("${spotify.client.id}")
    private String clientId;
    
    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.auth.url}")
    private String tokenUrl;

    private WebClient.Builder webClientBuilder;

    
}
