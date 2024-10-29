package com.kiing.spotsight.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.kiing.spotsight.model.user.UserProfile;
import com.kiing.spotsight.service.user.SpotifyUserService;

import reactor.core.publisher.Mono;

@RestController
public class SpotifyUserController {
    
    private SpotifyUserService spotifyUserService;

    public SpotifyUserController(SpotifyUserService spotifyUserService) {
        this.spotifyUserService = spotifyUserService;
    }

    @GetMapping("/api/user-profile")
    public Mono<UserProfile> getUserProfile(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.replace("Bearer ", "");
        return spotifyUserService.getUserProfile(accessToken);
    }
}
