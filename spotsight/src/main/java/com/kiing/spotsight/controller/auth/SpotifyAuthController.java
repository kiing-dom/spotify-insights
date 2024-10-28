package com.kiing.spotsight.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.kiing.spotsight.service.auth.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class SpotifyAuthController {
    
    private final SpotifyAuthService spotifyAuthService;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    public SpotifyAuthController(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }

    @GetMapping("/login/spotify")
    public RedirectView loginWithSpotify() {
        String authUrl = spotifyAuthService.getAuthorizationUrl();
        return new RedirectView(authUrl);
    }

    @GetMapping("/login/spotify/callback")
    public String handleSpotifyCallback(@RequestParam("code") String code) {
        spotifyAuthService.exchangeCodeForAccessToken(code);
        return "redirect:/dashboard";
    }
}
