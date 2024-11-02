package com.kiing.spotsight.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kiing.spotsight.service.auth.SpotifyAuthService;

@Component
public class SpotifyInsightsCLI {
    
    private static final String BASE_URL = "http://localhost:8080/api/";
    private final SpotifyAuthService spotifyAuthService;
    private String accessToken;

    @Autowired
    public SpotifyInsightsCLI(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }

    public void run() throws IOException {
        System.out.println("Spotify Insights CLI");
        System.out.println("1. Authenticate");
        System.out.println("2. Get User Profile");
        System.out.println("Choose an Option: ");

        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
        int choice = Integer.parseInt(reader.readLine());

        switch (choice) {
            case 1 -> authenticateUser();
            case 2 -> {
                if(accessToken == null) {
                    System.out.println("Please authenticate first!");
                } else {
                    getUserProfile();
                }
            }
            default -> System.out.println("Invalid choice, bozo!");
        }
    }

    private void authenticateUser() throws IOException {
        System.out.println("Please go to the following URL to authorize: ");
        System.out.println(spotifyAuthService.getAuthorizationUrl());
        System.out.println("Paste the authorization code here: ");

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String code = reader.readLine();
            
            this.accessToken = spotifyAuthService.getAccessToken().block();
            spotifyAuthService.exchangeCodeForAccessToken(code);
            
            System.out.println("Authentication successful. Access token obtained.");
        } catch (IOException e) {
            System.err.println("An error occurred while reading input. Please Try again");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Authentication failed. Please check your input or try again later");
            e.printStackTrace();
        }
    }

    private void getUserProfile() throws IOException {
        String userProfileEndpoint = BASE_URL + "user-profile";

        HttpURLConnection conn = (HttpURLConnection) new URL(userProfileEndpoint).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            System.out.println("User Profile Data: " + content.toString());

        } else {
            System.out.println("Failed to fetch data: HTTP error code " + responseCode);
        }
    }
}
