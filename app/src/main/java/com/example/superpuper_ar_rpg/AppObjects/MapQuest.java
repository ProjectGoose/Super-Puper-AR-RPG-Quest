package com.example.superpuper_ar_rpg.AppObjects;

import com.example.superpuper_ar_rpg.AppObjects.Quest;
import com.google.android.gms.maps.model.LatLng;

public class MapQuest extends Quest {
    private LatLng coordinates;
    private double rating;
    //Изображение, дата добавления, ссылки, автор...
    public MapQuest(String title, String txt, LatLng coord, int rating){
        this.title = title;
        this.coordinates = coord;
        this.text = txt;
        this.rating = rating;
    }

    public double getRating(){
        return rating;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
}

