package com.example.superpuper_ar_rpg;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuest;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestDto;
import com.example.superpuper_ar_rpg.AppObjects.quest.RadioUnit;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        RadioUnit temp = new RadioUnit("Сколько ног у собаки?");
        MapQuest mapQuest = new MapQuest("Сколько ног у собаки?", "Text", new LatLng(23.245, 51.322), 5);
        MapQuestDto dto = new MapQuestDto(mapQuest);
        String str = new GsonBuilder().setPrettyPrinting().create().toJson(dto);
        System.out.println(str);
    }
}
