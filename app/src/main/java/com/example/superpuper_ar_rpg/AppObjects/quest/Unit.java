package com.example.superpuper_ar_rpg.AppObjects.quest;

import android.util.Log;

public abstract class Unit {
    private int type;
    private String question;

    Unit(int type, String question){
        this.question = question;
        Log.d("TAG-Unit", " " + question);
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
