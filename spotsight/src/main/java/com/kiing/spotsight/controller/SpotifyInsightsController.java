package com.kiing.spotsight.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kiing.spotsight.service.auth.SpotifyAuthService;
import com.kiing.spotsight.service.user.SpotifyUserService;

import reactor.core.publisher.Mono;

@Controller
public class SpotifyInsightsController {

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyUserService spotifyUserService;

    public SpotifyInsightsController(SpotifyAuthService spotifyAuthService, SpotifyUserService spotifyUserService) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyUserService = spotifyUserService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/authenticate")
    public String authenticate(Model model) {
        String authUrl = spotifyAuthService.getAuthorizationUrl();
        model.addAttribute("authUrl", authUrl);
        return "authenticate";
    }

    @GetMapping("/user-profile")
    public Mono<String> getUserProfile(@RequestParam(value = "accessToken", required = false) String accessToken,
            Model model) {
        // Attempt to retrieve the stored token if none is provided
        if (accessToken == null || accessToken.isEmpty()) {
            accessToken = spotifyAuthService.getStoredAccessToken();
        }
        // Fetch the user profile with the available token
        return spotifyUserService.getUserProfile(accessToken)
                .doOnNext(user -> model.addAttribute("user", user))
                .doOnError(e -> model.addAttribute("error", e.getMessage()))
                .thenReturn("userProfile");
    }

    @GetMapping("/top-artists")
    public Mono<String> getTopArtists(@RequestParam(value = "timeRange", defaultValue = "medium_term") String timeRange,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            Model model) {
        String accessToken = spotifyAuthService.getStoredAccessToken();

        return spotifyUserService.getTopArtists(accessToken, timeRange, limit, offset)
                .collectList()
                .doOnNext(artists -> {
                    if (artists.isEmpty()) {
                        model.addAttribute("error", "No top artists found.");
                    } else {
                        model.addAttribute("artists", artists);
                    }
                })
                .doOnError(e -> model.addAttribute("error", e.getMessage()))
                .thenReturn("top-artists");

    }

    @GetMapping("/top-tracks")
    public Mono<String> getTopTracks(@RequestParam(value = "timeRange", defaultValue = "medium_term") String timeRange,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            Model model) {

        String accessToken = spotifyAuthService.getStoredAccessToken();

        return spotifyUserService.getTopTracks(accessToken, timeRange, limit, offset)
                .collectList()
                .doOnNext(tracks -> model.addAttribute("tracks", tracks))
                .doOnError(e -> model.addAttribute("error", e.getMessage()))
                .thenReturn("top-tracks");
    }
}
