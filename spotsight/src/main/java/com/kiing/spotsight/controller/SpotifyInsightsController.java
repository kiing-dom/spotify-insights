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
public Mono<String> getUserProfile(@RequestParam(value = "accessToken", required = false) String accessToken, Model model) {
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

}
