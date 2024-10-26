package com.kiing.spotsight.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

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

    @BeforeEach
    public void setUp() {
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec uriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);
    }
}