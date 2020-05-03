package com.example.superpuper_ar_rpg.AppObjects;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final double mRating;
    private final String mDescription;

    public MarkerItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = "";
        mSnippet = "";
        mRating = 0;
        mDescription = "";
    }

    public MarkerItem(double lat, double lng, String title, String snippet, double mRating, String description) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        this.mRating = mRating;
        mDescription = description;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public double getRating(){
        return mRating;
    }

    public String getDescription() {return mDescription;}
}