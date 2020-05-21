package com.example.superpuper_ar_rpg.AppObjects;

import com.example.superpuper_ar_rpg.AppObjects.Quest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapQuest extends Quest {
    private LatLng coordinates;
    private double rating;
    //Изображение, дата добавления, ссылки, автор...
    public MapQuest(String title, String txt, LatLng coord, double rating, long id){
        this.title = title;
        this.coordinates = coord;
        this.text = txt;
        this.rating = rating;
        this.id = id;
    }

    public double getRating(){
        return rating;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}

