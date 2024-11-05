package com.kiing.spotsight.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import com.kiing.spotsight.service.auth.SpotifyAuthService;
import com.kiing.spotsight.service.user.SpotifyUserService;

@Component
public class SpotifyInsightsCLI {

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyUserService spotifyUserService;
    private final BufferedReader reader;

    public SpotifyInsightsCLI(SpotifyAuthService spotifyAuthService, SpotifyUserService spotifyUserService) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyUserService = spotifyUserService;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() throws IOException {
        boolean running = true;

        while (running) {
            System.out.println("Spotify Insights CLI");
            System.out.println("1. Authenticate");
            System.out.println("2. Get User Profile");
            System.out.println("3. Get Top Artists");
            System.out.println("4. Exit");
            System.out.print("Choose an Option: ");

            int choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1 -> authenticateUser();
                case 2 -> getUserProfile();
                case 3 -> getTopArtists();
                case 4 -> {
                    System.out.println("Exiting...");
                    running = false;
                }
                default -> System.out.println("Invalid choice, please try again!");
            }
        }

        // Close the BufferedReader when done
        reader.close();
    }

    private void authenticateUser() {
        System.out.println("Please go to the following URL to authorize: ");
        System.out.println(spotifyAuthService.getAuthorizationUrl());
    }

    private void getUserProfile() throws IOException {
        // Prompt the user for the access token using BufferedReader
        System.out.print("Please enter your Spotify access token: ");
        String accessToken = reader.readLine();

        if (accessToken == null || accessToken.isEmpty()) {
            System.out.println("Access token is required. Please provide a valid token.");
            return;
        }

        spotifyUserService.getUserProfile(accessToken)
            .doOnNext(user -> System.out.println("Got the user profile for " + user.getDisplayName()))
            .doOnNext(user -> System.out.println("From Country: " + user.getCountry()))
            .doOnError(e -> System.out.println("Error" + e.getMessage()))
            .subscribe();
    }

    private void getTopArtists() throws IOException {
        System.out.print("Please enter the access token: ");
        String accessToken = reader.readLine();

        System.out.print("Enter time range (short_term, medium_term, long_term): ");
        String timeRange = reader.readLine();

        System.out.print("Enter Artist Limit (e.g. 10): ");
        int limit = Integer.parseInt(reader.readLine());

        System.out.print("Enter offset (e.g. 0): ");
        int offset = Integer.parseInt(reader.readLine());

        if (accessToken.isEmpty()) {
            System.out.println("An access token is required. Please provide one.");
            return;
        }

        spotifyUserService.getTopArtists(accessToken, timeRange, limit, offset)
            .doOnNext(artist -> System.out.println("Artists: " + artist.getName()))
            .doOnError(e -> System.out.println("Error: " + e.getMessage()))
            .subscribe();
    }
}
