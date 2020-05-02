package com.example.superpuper_ar_rpg.AppObjects;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
//singleton
public class User {

    private LatLng coordinates;
    private String token;
    private String nickname;
    private Bitmap avatar;
    private int level;

    public static User instance;

    private User(String token, String nickname, int level){
        this.coordinates = coordinates;
        this.token = token;
        this.nickname = nickname;
        this.level = level;
        this.coordinates = new LatLng(0,0);
    }

    public static User getInstance() throws NullPointerException{
        if(instance == null){
            throw new NullPointerException("User is not initialized, use firsSetInstance");
        } else {
            return (instance);
        }
    }

    public static boolean firstSetInstance(String token, String nickname, int level){
        if(instance == null){
            instance = new User(token, nickname, level);
            return true;
        } else {
            return false;
        }
    }


    public LatLng getCoordinates(){
        return(coordinates);
    }
    public String getNickname(){
        return(nickname);
    }
    public int getLevel(){
        return(level);
    }
    public String getToken(){
        return(token);
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }
}
