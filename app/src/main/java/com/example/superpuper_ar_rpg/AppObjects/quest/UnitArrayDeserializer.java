package com.example.superpuper_ar_rpg.AppObjects.quest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UnitArrayDeserializer implements JsonDeserializer<ArrayList<Unit>> {
    @Override
    public ArrayList<Unit> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Unit> units = new ArrayList<>();

        JsonArray data = json.getAsJsonArray();
        for(int i = 0; i < data.size(); i++){
            JsonObject current = (JsonObject)data.get(i);
            int type = current.get("type").getAsInt();
            switch (type) {
                case 1:
                    units.add(new Gson().fromJson(current, TextUnit.class));
                    break;
                case 2:
                    units.add(new Gson().fromJson(current, RadioUnit.class));
                    break;
            }
        }

        return units;
    }
}
