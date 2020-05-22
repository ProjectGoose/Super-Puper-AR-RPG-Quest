package com.example.superpuper_ar_rpg.AppObjects.quest;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class Quest {
    protected long id;
    protected String title;
    protected String text;
    protected String body;
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
    public long getId() {return id;}
    public String getBody() { return body; }
}
