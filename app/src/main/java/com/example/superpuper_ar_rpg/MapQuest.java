package com.example.superpuper_ar_rpg;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapQuest extends Quest {
    public LatLng coordinates;
    //Изображение, дата добавления, ссылки, автор...
    MapQuest(String title, String txt, LatLng coord){
        this.title = title;
        this.coordinates = coord;
        this.text = txt;
    }
}

