package com.example.superpuper_ar_rpg.AppObjects;

import android.graphics.Bitmap;

import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

//singleton
public class User {

    private LatLng coordinates;
    private String token;
    private String nickname;
    private Bitmap avatar;
    private int level;
    private int age;
    private Race race;
    private ArrayList<MapQuest> activeQuests = new ArrayList<>();

    enum Race{
        ORK("Орк"), ELF("Эльф"), HUMAN ("Человек"), HOBBIT("Хоббит");

        private String title;
        Race(String title){
            this.title = title;
        }

        public String getTitle(){ return title;};
    }

    public static User instance;

    private User(String token, String nickname, int level){
        this.coordinates = coordinates;
        this.token = token;
        this.nickname = nickname;
        this.level = level;
        this.coordinates = new LatLng(0,0);

        //TODO delete debug feature
        debugInitial();
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

    private void debugInitial(){
        age = 54;
        race = Race.ORK;
    }

    public ArrayList<MapQuest> getActiveQuests() {
        return activeQuests;
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
    public String getRace(){return race.title;}
    public int getAge(){return age;}

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }


}
