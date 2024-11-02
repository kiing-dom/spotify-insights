package com.kiing.spotsight.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.kiing.spotsight.service.auth.SpotifyAuthService;

@Component
public class SpotifyInsightsCLI {
    
    private static final String BASE_URL = "http://localhost:8080/api/";
    private final SpotifyAuthService spotifyAuthService;
    private final BufferedReader reader;

    public SpotifyInsightsCLI(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() throws IOException {
        boolean running = true;

        while (running) {
            System.out.println("Spotify Insights CLI");
            System.out.println("1. Authenticate");
            System.out.println("2. Get User Profile");
            System.out.println("3. Exit");
            System.out.print("Choose an Option: ");

            int choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1 -> authenticateUser();
                case 2 -> getUserProfile();
                case 3 -> {
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
    
        HttpURLConnection conn = (HttpURLConnection) new URL(userProfileEndpoint).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        
        int responseCode = conn.getResponseCode();
        
        // Check if the access token is valid
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            
            in.close();
            System.out.println("User Profile Data: " + content.toString());
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            System.out.println("Invalid access token. Please check your token and try again.");
        } else {
            System.out.println("Failed to fetch data: HTTP error code " + responseCode);
        }
    
        
        // scanner.close();
    }
}
