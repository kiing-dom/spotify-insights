package com.kiing.spotsight.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpotifyInsightsCLI {
    
    private static final String BASE_URL = "http://localhost:8080/api/";

    public static void main(String[] args) throws Exception {
        SpotifyInsightsCLI cli = new SpotifyInsightsCLI();
        cli.run();
    }

    public void run() throws IOException {
        System.out.println("Spotify Insights CLI");
        System.out.println("1. Get User Profile");
        System.out.println("2. Get Top Tracks");
        System.out.println("Choose an Option: ");

        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
        int choice = Integer.parseInt(reader.readLine());

        switch (choice) {
            case 1 -> getUserProfile();
            default -> System.out.println("Invalid choice, bozo!");
        }
    }

    private void getUserProfile() {
        String userProfileEndpoint = BASE_URL + "/user-profile";
        String response = makeGetRequest(userProfileEndpoint);
        System.out.println("User Profile Data: " + response);
    }

    private String makeGetRequest(String endpoint) {
        
    }
}
