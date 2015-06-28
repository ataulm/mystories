package com.ataulm.wutson.episodes;

public class EpisodeNumber {

    private final int seasonNumber;
    private final int episodeNumber;

    public EpisodeNumber(int seasonNumber, int episodeNumber) {
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String toString() {
        return "S" + seasonNumber + "E" + episodeNumber;
    }

    public int getSeason() {
        return seasonNumber;
    }

    public int getEpisode() {
        return episodeNumber;
    }
}
