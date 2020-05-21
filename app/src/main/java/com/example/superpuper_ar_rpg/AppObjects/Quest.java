package com.example.superpuper_ar_rpg.AppObjects;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class Quest {
    @SerializedName("id")
    @Expose
    protected long id;
    @SerializedName("title")
    @Expose
    protected String title;
    @SerializedName("text")
    @Expose
    protected String text;
    @SerializedName("author")
    @Expose
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
