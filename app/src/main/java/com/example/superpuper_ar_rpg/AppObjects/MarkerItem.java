package com.example.superpuper_ar_rpg.AppObjects;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {
    private double latitude;
    private double longitude;
    private final String mTitle;
    private final double mRating;
    private final long id;

    private LatLng mPosition = new LatLng(latitude, longitude);

    public MarkerItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = "";
        mRating = 0;
        id = 0;
        latitude = 0;
        longitude = 0;
    }

    public MarkerItem(double lat, double lng, String title, double mRating, long id) {
        this.latitude = lat;
        this.longitude = lng;
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        this.mRating = mRating;
        this.id = id;
    }

    @Override
    public LatLng getPosition() {
        mPosition = new LatLng(latitude, longitude);
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public double getRating(){
        return mRating;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}