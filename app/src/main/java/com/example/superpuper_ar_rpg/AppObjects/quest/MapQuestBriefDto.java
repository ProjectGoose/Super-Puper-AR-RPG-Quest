package com.example.superpuper_ar_rpg.AppObjects.quest;

import android.os.Parcel;
import android.os.Parcelable;

public class MapQuestBriefDto implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(rating);
    }

    public static final Parcelable.Creator<MapQuestBriefDto> CREATOR = new Parcelable.Creator<MapQuestBriefDto>() {

        @Override
        public MapQuestBriefDto createFromParcel(Parcel source) {
            long id = source.readLong();
            String title = source.readString();
            double latitude = source.readDouble();
            double longitude = source.readDouble();
            double rating = source.readDouble();

            return new MapQuestBriefDto(id, title, latitude, longitude, rating);
        }

        @Override
        public MapQuestBriefDto[] newArray(int size) {
            return new MapQuestBriefDto[size];
        }
    };
}
