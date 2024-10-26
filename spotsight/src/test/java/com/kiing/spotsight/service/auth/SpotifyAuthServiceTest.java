package com.kiing.spotsight.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.kiing.spotsight.model.token.TokenResponse;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

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

        // configure mocks for web client methods
        when(webClientMock.post()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(tokenUrl)).thenReturn(uriSpecMock);
        when(uriSpecMock.header(any(), any())).thenReturn(uriSpecMock);
        when(uriSpecMock.bodyValue(any())).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);

        // mock the response of WebClient to return a TokenResponse object
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("mock-access-token");
        tokenResponse.setTokenType("Bearer");
        tokenResponse.setExpiresIn(3600);
    }
}