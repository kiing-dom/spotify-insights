package com.kiing.spotsight.model.user.top;

import java.util.List;
public class TopTracks {
    private List<Item> items;
    private int total;
    private int limit;
    private int offset;
    public static class Item {
        private String name;
        private int popularity;
        private Album album;

        public String getName() {
            return name;
        }
        public int getPopularity() {
            return popularity;
        }

        public Album getAlbum() {
            return album;
        }



    }

    public static class Album {
        private String name;
        private String albumType;
        private int totalTracks;
        private String releaseDate;

        public String getName() {
            return name;
        }

        public String getAlbumType() {
            return albumType;
        }

        public int getTotalTracks() {
            return totalTracks;
        }

        public String getReleaseDate() {
            return releaseDate;
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}
