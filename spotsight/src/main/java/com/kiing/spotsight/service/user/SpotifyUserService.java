package com.kiing.spotsight.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.kiing.spotsight.model.user.UserProfile;
import com.kiing.spotsight.model.user.top.TopArtists;
import com.kiing.spotsight.model.user.top.TopTracks;

import reactor.core.publisher.Flux;
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

    public Flux<TopArtists.Item> getTopArtists(String accessToken, String timeRange, int limit, int offset) {
        
        String uri = UriComponentsBuilder.fromUriString("/me/top/artists")
            .queryParam("time_range", timeRange)
            .queryParam("limit", limit)
            .queryParam("offset", offset)
            .toUriString();

        return webClient.get()
            .uri(uri)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(TopArtists.class)
            .flatMapMany(response -> Flux.fromIterable(response.getItems()))
            .doOnError(e -> logger.error("Error retrieving top artists: {}", e.getMessage()));
    }

    public Flux<TopTracks.Item> getTopTracks(String accessToken, String timeRange, int limit, int offset) {
        String uri = UriComponentsBuilder.fromUriString("/me/top/tracks")
            .queryParam("time_range", timeRange)
            .queryParam("limit", limit)
            .queryParam("offset", offset)
            .toUriString();

        return webClient.get()
            .uri(uri)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(TopTracks.class)
            .flatMapMany(response -> Flux.fromIterable(response.getItems()))
            .doOnError(e -> logger.error("Error retrieving top tracks: {}", e.getMessage()));
    }
}
