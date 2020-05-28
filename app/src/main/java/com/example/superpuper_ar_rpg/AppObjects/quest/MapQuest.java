package com.example.superpuper_ar_rpg.AppObjects.quest;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MapQuest extends Quest {
    private LatLng coordinates;
    private double rating;

    ArrayList<Unit> units = new ArrayList<>();
    //Изображение, дата добавления, ссылки, автор...
    public MapQuest(String title, String txt, LatLng coord, int rating){
        this.title = title;
        this.coordinates = coord;
        this.text = txt;
        this.rating = rating;
    }

    public MapQuest(MapQuestDto dto){
        this.title = dto.getTitle();
        this.coordinates = new LatLng(dto.getLatitude(), dto.getLongitude());
        this.rating = dto.getRating();
        this.author = dto.getAuthor();
        this.id = dto.getId();
        this.text = dto.getText();
        TypeToken<ArrayList<Unit>> typeToken = new TypeToken<ArrayList<Unit>>() {};
        this.units = new GsonBuilder()
                .registerTypeAdapter(typeToken.getType(), new UnitArrayDeserializer())
                .create()
                .fromJson(dto.getBody(), typeToken.getType());
    }

    public MapQuest(MapQuestBriefDto briefDto, MapQuestDetailsDto detailsDto){
        this.id = briefDto.getId();
        this.title = briefDto.getTitle();
        this.author = detailsDto.getAuthor();
        this.coordinates = new LatLng( briefDto.getLatitude(), briefDto.getLongitude());
        this.rating = briefDto.getRating();
        this.body = detailsDto.getBody();
        this.text = detailsDto.getText();
        TypeToken<ArrayList<Unit>> typeToken = new TypeToken<ArrayList<Unit>>() {};
        this.units = new GsonBuilder()
                .registerTypeAdapter(typeToken.getType(), new UnitArrayDeserializer())
                .create()
                .fromJson(detailsDto.getBody(), typeToken.getType());
    }

    public double getRating(){
        return rating;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public double getLatitude() {
        return coordinates.latitude;
    }

    public double getLongitude() {
        return coordinates.longitude;
    }

    public String getText() {return text;};

    public ArrayList<Unit> getUnits(){ return units;}

    public MapQuestDto getDto(){
        MapQuestDto result = new MapQuestDto(this);
        result.setText(new Gson().toJson(units, ArrayList.class));
        return result;
    }
}

