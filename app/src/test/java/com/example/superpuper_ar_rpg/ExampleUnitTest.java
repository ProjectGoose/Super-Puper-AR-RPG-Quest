package com.example.superpuper_ar_rpg;

import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuest;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestDto;
import com.example.superpuper_ar_rpg.AppObjects.quest.RadioUnit;
import com.example.superpuper_ar_rpg.AppObjects.quest.Unit;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        RadioUnit temp = new RadioUnit("Сколько ног у собаки?");
        MapQuest mapQuest = new MapQuest("Квест по зоологии", "Text", new LatLng(23.245, 51.322), 5);
        mapQuest.getUnits().add(temp);
        MapQuestDto dto = new MapQuestDto(mapQuest);
        String str = new GsonBuilder().setPrettyPrinting().create().toJson(dto);
        System.out.println(str);
    }
}