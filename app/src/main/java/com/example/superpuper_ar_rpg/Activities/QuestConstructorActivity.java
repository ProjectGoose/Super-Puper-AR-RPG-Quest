package com.example.superpuper_ar_rpg.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.superpuper_ar_rpg.AppObjects.App;
import com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews.GroupOfView;
import com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews.GroupOfViewText;
import com.example.superpuper_ar_rpg.AppObjects.quest.TextUnit;
import com.example.superpuper_ar_rpg.AppObjects.quest.Unit;
import com.example.superpuper_ar_rpg.R;

import java.util.ArrayList;
import java.util.HashMap;


public class QuestConstructorActivity extends AppCompatActivity {

    class Pair{
        Pair(int value, View view){
            this.value = value;
            this.view = view;
        }
        public int value;
        public View view;
    }

    private ArrayList<String> unitNames = App.getInstance().getTypesOfUnits();
    private Spinner spinner;
    private Button bt_add, bt_remove, bt_send;
    private View unit;
    LinearLayout scrollViewContainer;
    ArrayList<Pair> units = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questconstructor);
        spinner = findViewById(R.id.sp_quests);
        bt_add = findViewById(R.id.bt_addUnit);
        bt_remove = findViewById(R.id.bt_removeUnit);
        bt_send = findViewById(R.id.bt_send);
        LinearLayout thisLayout = findViewById(R.id.constructorLayout);
        scrollViewContainer = findViewById(R.id.sview).findViewById(R.id.sviewContainer);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                switch(spinner.getSelectedItemPosition()) {
                    case 0:
                        unit = inflater.inflate(R.layout.unit_text, null);
                        ((TextView)unit.findViewById(R.id.num)).setText(String.valueOf(units.size()));
                        scrollViewContainer.addView(unit);
                        units.add(new Pair(1, unit));
                        break;
                    case 1:
                        unit = inflater.inflate(R.layout.unit_radio, null);
                        ((TextView)unit.findViewById(R.id.num)).setText(String.valueOf(units.size()));
                        scrollViewContainer.addView(unit);
                        units.add(new Pair(2, unit));
                        break;
                }
            }
        });

        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(units.size() != 0) {
                    scrollViewContainer.removeView(units.get(units.size() - 1).view);
                    units.remove(units.size() - 1);
                }
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
