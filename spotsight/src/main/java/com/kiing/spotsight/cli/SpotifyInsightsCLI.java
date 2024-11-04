package com.kiing.spotsight.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.kiing.spotsight.service.auth.SpotifyAuthService;
import com.kiing.spotsight.service.user.SpotifyUserService;

@Component
public class SpotifyInsightsCLI {
    
    private static final String BASE_URL = "http://localhost:8080/api/";
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
        // reader.close();
    }

    private void authenticateUser() {
        System.out.println("Please go to the following URL to authorize: ");
        System.out.println(spotifyAuthService.getAuthorizationUrl());
        // Additional logic to store access token if necessary
    }

    private void getUserProfile() throws IOException {
        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);
        
        // Prompt the user for the access token
        System.out.print("Please enter your Spotify access token: ");
        String accessToken = scanner.nextLine();
        
        String userProfileEndpoint = BASE_URL + "user-profile";
    
        // Check if the access token is provided
        if (accessToken == null || accessToken.isEmpty()) {
            System.out.println("Access token is required. Please provide a valid token.");
            // scanner.close();
            return; // Exit the method if no token is provided
        }
    
        spotifyUserService.getUserProfile(accessToken)
            .doOnNext(user -> System.out.println("Got the user profile for " + user.getDisplayName()))
            .doOnNext(user -> System.out.println("From Country: " + user.getCountry()))
            .doOnError(e -> System.out.println("Error" + e.getMessage()))
            .subscribe();
        // Check if the access token is valid
        
        
        // scanner.close();
    }

    private void getTopArtists()  {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the access token: ");
        String accessToken = scanner.nextLine();

        System.out.println("Enter time range (short_term, medium_term, long_term): ");
        String timeRange = scanner.nextLine();

        System.out.println("Enter Artist Limit (e.g. 10): ");
        int limit = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter offset (e.g. 0): ");
        int offset = Integer.parseInt(scanner.nextLine());

        if(accessToken.isEmpty()) {
            System.out.println("An access token is required. Please provide one");
            return;
        }

        spotifyUserService.getTopArtists(accessToken, timeRange, limit, offset)
            .doOnNext(artist -> System.out.println("Artists: " + artist.getName()))
            .doOnError(e -> System.out.println("Error: " + e.getMessage()))
            .subscribe();
    }
}
