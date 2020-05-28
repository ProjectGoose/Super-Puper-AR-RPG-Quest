package com.example.superpuper_ar_rpg.AppObjects;

import java.util.ArrayList;

public class App {

    private ArrayList<String> typesOfUnits = new ArrayList<>(); //временный варинат
    private static App instance;
    private App(){
        typesOfUnits.add("Текстовый вопрос");
        typesOfUnits.add("Вопрос с выбором");
    }
    public static App getInstance(){
        if(instance == null){
            instance = new App();
        }
        return instance;
    }

    public ArrayList<String> getTypesOfUnits(){
        return typesOfUnits;
    }



}
