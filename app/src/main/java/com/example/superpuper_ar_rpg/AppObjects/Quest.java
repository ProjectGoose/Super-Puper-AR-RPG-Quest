package com.example.superpuper_ar_rpg.AppObjects;

import com.google.android.gms.maps.model.LatLng;

public abstract class Quest {
    public String title;
    public String text;
    public String author;

    public String getTitle(){
        return(title);
    }
    public String getText(){
        return(text);
    }
    public String getAuthor(){
        return(author);
    }
}
