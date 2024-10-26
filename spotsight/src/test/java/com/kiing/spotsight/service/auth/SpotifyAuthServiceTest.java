package com.kiing.spotsight.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import com.kiing.spotsight.model.token.TokenResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SpotifyAuthServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SpotifyAuthServiceTest.class);

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private SpotifyAuthService spotifyAuthService;

    private final String TEST_CLIENT_ID = "test-client-id";
    private final String TEST_CLIENT_SECRET = "test-client-secret";
    private final String TEST_TOKEN_URL = "/api/token";
    private final String TEST_ACCESS_TOKEN = "test-access-token";

    @BeforeEach
    void setUp() {
        logger.info("Setting up SpotifyAuthServiceTest...");

        // Set up mock chain
        doReturn(webClientBuilder).when(webClientBuilder).baseUrl(anyString());
        doReturn(webClient).when(webClientBuilder).build();
        doReturn(requestBodyUriSpec).when(webClient).post();
        doReturn(requestBodyUriSpec).when(requestBodyUriSpec).uri(anyString());
        doReturn(requestBodyUriSpec).when(requestBodyUriSpec).header(anyString(), anyString());
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();

        // Manually create the service with the mocked builder
        spotifyAuthService = new SpotifyAuthService(webClientBuilder);

        // Set required properties
        ReflectionTestUtils.setField(spotifyAuthService, "clientId", TEST_CLIENT_ID);
        ReflectionTestUtils.setField(spotifyAuthService, "clientSecret", TEST_CLIENT_SECRET);
        ReflectionTestUtils.setField(spotifyAuthService, "tokenUrl", TEST_TOKEN_URL);

        logger.info("Initialized SpotifyAuthService with Client ID: {}", TEST_CLIENT_ID);
    }

    @Test
    void testGetAccessToken_Success() {
        logger.info("Executing testGetAccessToken_Success...");

        // Prepare test data
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(TEST_ACCESS_TOKEN);

        // Mock the response
        doReturn(Mono.just(tokenResponse)).when(responseSpec).bodyToMono(TokenResponse.class);

        // Execute and verify
        StepVerifier.create(spotifyAuthService.getAccessToken())
                .expectNext(TEST_ACCESS_TOKEN)
                .verifyComplete();

        // Verify the WebClient calls
        verify(webClient).post();
        verify(requestBodyUriSpec).uri(TEST_TOKEN_URL);
        verify(requestBodyUriSpec).header("Content-Type", "application/x-www-form-urlencoded");
        verify(requestBodyUriSpec).bodyValue(
                "grant_type=client_credentials&client_id=" + TEST_CLIENT_ID + "&client_secret=" + TEST_CLIENT_SECRET);

        logger.info("Successfully retrieved access token: {}", TEST_ACCESS_TOKEN);
    }

    @Test
    void testGetAccessToken_Error() {
        logger.info("Executing testGetAccessToken_Error...");

        // Mock error response
        RuntimeException testException = new RuntimeException("Test error");
        doReturn(Mono.error(testException)).when(responseSpec).bodyToMono(TokenResponse.class);

        // Execute and verify
        StepVerifier.create(spotifyAuthService.getAccessToken())
                .expectError(RuntimeException.class)
                .verify();

        logger.error("Expected error occurred while retrieving access token: {}", testException.getMessage());
    }
}
