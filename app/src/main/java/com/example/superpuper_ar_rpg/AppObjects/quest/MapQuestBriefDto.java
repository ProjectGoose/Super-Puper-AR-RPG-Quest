package com.example.superpuper_ar_rpg.AppObjects.quest;

public class MapQuestBriefDto {
    private long id;
    private String title;
    private double latitude;
    private double longitude;
    private double rating;

    public MapQuestBriefDto(long id, String title, double latitude, double longitude, double rating) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRating() {
        return rating;
    }
}
