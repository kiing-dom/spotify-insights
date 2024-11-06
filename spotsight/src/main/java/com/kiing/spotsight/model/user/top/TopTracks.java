package com.kiing.spotsight.model.user.top;
public class TopTracks {
    
    public static class Item {
        private String name;
        private int popularity;
        private Album album;
        private Artists artists;

        public String getName() {
            return name;
        }
        public int getPopularity() {
            return popularity;
        }

        public Album getAlbum() {
            return album;
        }

        public Artists getArtists() {
            return artists;
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

    public static class Artists {
        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }
}
