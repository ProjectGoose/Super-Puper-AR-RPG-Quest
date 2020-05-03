package com.example.superpuper_ar_rpg.AppObjects;

import com.google.android.gms.maps.model.LatLng;

public abstract class Quest {
    protected String title;
    protected String text;
    protected String author;

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
