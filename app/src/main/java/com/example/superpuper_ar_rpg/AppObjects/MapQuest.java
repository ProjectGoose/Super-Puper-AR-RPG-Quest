package com.example.superpuper_ar_rpg.AppObjects;

import com.example.superpuper_ar_rpg.AppObjects.Quest;
import com.google.android.gms.maps.model.LatLng;

public class MapQuest extends Quest {
    public LatLng coordinates;
    //Изображение, дата добавления, ссылки, автор...
    public MapQuest(String title, String txt, LatLng coord){
        this.title = title;
        this.coordinates = coord;
        this.text = txt;
    }
}

