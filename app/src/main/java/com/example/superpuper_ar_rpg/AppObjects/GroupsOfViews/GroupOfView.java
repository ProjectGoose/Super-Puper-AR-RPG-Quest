package com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews;

public abstract class GroupOfView {
    private int type;

    public GroupOfView(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
