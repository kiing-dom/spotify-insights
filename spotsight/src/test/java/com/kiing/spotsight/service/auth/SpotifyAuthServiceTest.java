package com.kiing.spotsight.service.auth;

import com.kiing.spotsight.model.token.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpotifyAuthServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyAuthServiceTest.class);
    
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
        // Create mocks for WebClient and its nested types
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec uriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpecMock = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec<?> headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        // Set up method call chain with doReturn to avoid issues with generics
        doReturn(uriSpecMock).when(webClientMock).post();
        doReturn(bodySpecMock).when(uriSpecMock).uri(anyString());
        doReturn(bodySpecMock).when(bodySpecMock).header(anyString(), anyString());
        doReturn(headersSpecMock).when(bodySpecMock).bodyValue(any());
        doReturn(responseSpecMock).when(headersSpecMock).retrieve();

        // Mock the response to return a TokenResponse object with a mocked access token
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("mock-access-token");
        tokenResponse.setTokenType("Bearer");
        tokenResponse.setExpiresIn(3600);
        doReturn(Mono.just(tokenResponse)).when(responseSpecMock).bodyToMono(TokenResponse.class);

        // Mock WebClient.Builder and provide the mock WebClient
        webClientBuilder = mock(WebClient.Builder.class);
        doReturn(webClientBuilder).when(webClientBuilder).baseUrl(anyString());
        doReturn(webClientMock).when(webClientBuilder).build();

        // Initialize SpotifyAuthService with the mocked WebClient builder
        spotifyAuthService = new SpotifyAuthService(webClientBuilder);

        // Log each step of the setup
        logger.info("WebClient and nested mocks set up successfully.");
    }

    @Test
    void testGetAccessToken() {
        logger.info("Testing getAccessToken method.");
        String accessToken = spotifyAuthService.getAccessToken().block();  // Blocking to get the result for testing
        assertEquals("mock-access-token", accessToken);  // Verify that the access token matches expected value
        logger.info("Access token retrieved: {}", accessToken);
    }
}
