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
            System.out.println("4. Get Top Tracks");
            System.out.println("5. Exit");
            System.out.print("Choose an Option: ");

            int choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1 -> authenticateUser();
                case 2 -> getUserProfile();
                case 3 -> getTopArtists();
                case 4 -> getTopTracks();
                case 5 -> {
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
        try {
            System.out.println("Please go to the following URL to authorize: ");
            System.out.println(spotifyAuthService.getAuthorizationUrl());
        } catch (Exception e) {
            System.out.println("Error closing reader: " + e.getMessage());
        }
    }

    private void getUserProfile() {
        try {
            // Prompt the user for the access token using BufferedReader
            System.out.print("Please enter your Spotify access token: ");
            String accessToken = spotifyAuthService.getStoredAccessToken();

            if (accessToken == null || accessToken.isEmpty()) {
                System.out.println("Access token is required. Please provide a valid token.");
                return;
            }

            spotifyUserService.getUserProfile(accessToken)
                    .doOnNext(user -> System.out.println("Got the user profile for " + user.getDisplayName()))
                    .doOnNext(user -> System.out.println("From Country: " + user.getCountry()))
                    .doOnError(e -> System.out.println("Error" + e.getMessage()))
                    .subscribe();
        } catch (Exception e) {
            System.out.println("An error occurred while trying to get user profile: " + e.getMessage());
        }
    }

    private void getTopArtists() throws IOException {
        try {
            System.out.print("Please enter the access token: ");
            String accessToken = spotifyAuthService.getStoredAccessToken();

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
        } catch (IOException e) {
            System.out.println("An error occurred while trying to get top artists: " + e.getMessage());
        }
    }

    private void getTopTracks() throws IOException {
        try {
            System.out.println("Please enter the access token: ");
            String accessToken = spotifyAuthService.getStoredAccessToken();

            System.out.println("Enter time range (short_term, medium_term, long_term)");
            String timeRange = reader.readLine();

            System.out.println("Enter Track Limit: ");
            int limit = Integer.parseInt(reader.readLine());

            System.out.println("Enter offset: ");
            int offset = Integer.parseInt(reader.readLine());

            if (accessToken.isEmpty()) {
                System.out.println("An access token is required. Please provide one");
                return;
            }

            spotifyUserService.getTopTracks(accessToken, timeRange, limit, offset)
                    .doOnNext(track -> System.out
                            .println("Track: " + track.getName() + " On Album: " + track.getAlbum().getName()))
                    .doOnNext(track -> System.out.println("Popularity: " + track.getPopularity()))
                    .doOnError(e -> System.out.println("Error" + e.getMessage()))
                    .subscribe();
        } catch (IOException e) {
            System.out.println("An error occurred while trying to get top tracks: " + e.getMessage());
        }
    }
}
