package com.example.superpuper_ar_rpg;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Quest {
    Quest(String title, String txt, LatLng coord){
        this.title = title;
        this.coordinates = coord;
        this.text = txt;
    }
    public Marker marker;
    public LatLng coordinates;
    public String title;
    public String text;
    //Изображение, дата добавления, ссылки, автор...
}
