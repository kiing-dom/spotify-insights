package com.kiing.spotsight.model.user.top;

import java.util.*;

public class TopArtists {
    private List<Item> items;
    private int total;
    private int limit;
    private int offset;

    public static class Item {
        private String name;
        private List<String> genres;
        private Followers followers;
        private ExternalUrls externalUrls;
        private long popularity;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
        }

        public Followers getFollowers() {
            return followers;
        }

        public void setFollowers(Followers followers) {
            this.followers = followers;
        }

        public ExternalUrls getExternalUrls() {
            return externalUrls;
        }

        public void setExternalUrls(ExternalUrls externalUrls) {
            this.externalUrls = externalUrls;
        }

        public long getPopularity() {
            return popularity;
        }

        public void setPopularity(long popularity) {
            this.popularity = popularity;
        }
    }

    public static class Followers {
        private int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public static class ExternalUrls {
        private String spotify;

        public String getSpotify() {
            return spotify;
        }

        public void setSpotify(String spotify) {
            this.spotify = spotify;
        }
    }

    // Getters and setters for the main class
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}