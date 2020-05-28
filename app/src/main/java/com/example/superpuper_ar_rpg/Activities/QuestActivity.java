package com.example.superpuper_ar_rpg.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews.GroupOfView;
import com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews.GroupOfViewRadio;
import com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews.GroupOfViewText;
import com.example.superpuper_ar_rpg.AppObjects.User;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuest;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestBriefDto;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestDetailsDto;
import com.example.superpuper_ar_rpg.AppObjects.quest.RadioUnit;
import com.example.superpuper_ar_rpg.AppObjects.quest.TextUnit;
import com.example.superpuper_ar_rpg.AppObjects.quest.Unit;
import com.example.superpuper_ar_rpg.Network.NetworkService;
import com.example.superpuper_ar_rpg.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestActivity extends AppCompatActivity {


    private TextView tv_title, tv_rating, tv_description;
    private Button bt_accept;
    private Button bt_pass;
    private boolean isCloseEnough;
    private boolean isAccepted = false;
    private LinearLayout thisLayout, questUnitsLayout;
    private ArrayList<GroupOfView> units = new ArrayList<>();
    //private MapQuestBriefDto briefDto;
    MapQuest thisQuest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        tv_title = findViewById(R.id.tv_title);
        tv_rating = findViewById(R.id.tv_rating);
        tv_description = findViewById(R.id.tv_description);
        bt_accept = findViewById(R.id.bt_accept);
        thisLayout = findViewById(R.id.quest_units_layout);
        questUnitsLayout = findViewById(R.id.quest_units_layout);

        MapQuestBriefDto briefDto = getIntent().getParcelableExtra("BriefDto");



        //Делаем запрос на параметры квеста и устанавливаем оставшиеся поля
        NetworkService.getInstance().requestQuestBody(User.getInstance().getToken(), briefDto.getId(), new Callback<MapQuestDetailsDto>() {
            @Override
            public void onResponse(Call<MapQuestDetailsDto> call, Response<MapQuestDetailsDto> response) {
                if(response.isSuccessful()) {
                    Log.d("TAG-QuestActivity-Inf", " d " + response.body());
                    thisQuest = new MapQuest(briefDto, response.body());
                    tv_title.setText(thisQuest.getTitle());
                    tv_rating.setText(String.format("%.2f", thisQuest.getRating()));
                    tv_description.setText(thisQuest.getText());
                } else {
                    Log.d("TAG-QuestActivityErr", "Smth goes wrong");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        //устанавливаем первоначальную надпись на кнопке
        setButtonState();

        bt_accept.setOnClickListener(new View.OnClickListener() {
            //Возможно, нужно создать отдельный класс Application, храянящий все настраиваемые параметры, типа радиус доступности квеста
            @Override
            public void onClick(View v) {
                LatLngBounds bounds = toBounds(thisQuest.getCoordinates(), 20);
                if(!isAccepted) {
                    if (true /*bounds.contains(User.getInstance().getCoordinates())*/) {
                        isAccepted = true;
                        bt_accept.setText(R.string.btn_rejectQuest);
                        ArrayList<Unit> unitsQuest = thisQuest.getUnits();
                        for (int i = 0; i < unitsQuest.size(); i++) {
                            Unit unit = unitsQuest.get(i);
                            //интовый type нужно бы заменить на указатель на класс
                            switch (unit.getType()) {
                                case 1:
                                    setViewText(unit);
                                    break;
                                case 2:
                                    setViewRadio(unit);
                                    break;
                            }
                        }
                        bt_pass = new Button(QuestActivity.this);
                        bt_pass.setText("Сдать");
                        thisLayout.addView(bt_pass);
                        bt_pass.setOnClickListener(new View.OnClickListener(){
                            boolean flag = true;
                            @Override
                            public void onClick(View v) {
                                for(GroupOfView group: units){
                                    switch (group.getType()){
                                        case 1:
                                            flag = ((GroupOfViewText)group).isCorrect();
                                            break;
                                        case 2:
                                            flag = ((GroupOfViewRadio)group).isCorrect();
                                            break;
                                    }
                                }
                                if(flag){
                                    Toast.makeText(QuestActivity.this, "Вы великолепны!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(QuestActivity.this, "Вы не великолепны!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    } else {
                        Toast.makeText(QuestActivity.this, R.string.toast_closeToQuest, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    isAccepted = false;
                    bt_accept.setText(R.string.btn_pickQuest);
                    for(GroupOfView group: units){
                        switch (group.getType()){
                            case 1:
                                questUnitsLayout.removeView(((GroupOfViewText)group).getQuestion());
                                questUnitsLayout.removeView(((GroupOfViewText) group).getAnswer());
                                break;
                            case 2:
                                questUnitsLayout.removeView(((GroupOfViewRadio)group).getQuestion());
                                questUnitsLayout.removeView(((GroupOfViewRadio) group).getRadioGroup());
                                break;
                        }
                    }
                    thisLayout.removeView(bt_pass);
                }
            }
        });


    }

    @Override
    public void onStop(){
        super.onStop();
        if(isAccepted){
            User.getInstance().getActiveQuests().add(thisQuest);
        } else {
            User.getInstance().getActiveQuests().remove(thisQuest);
        }
    }

    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        //координаты соответствующих углов LatLngBounds
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

    void setButtonState(){
        for(MapQuest quest: User.getInstance().getActiveQuests()){
            if(thisQuest == quest){
                isAccepted = true;
            }
        }
        if(isAccepted){
            bt_accept.setText(R.string.btn_rejectQuest);
        }
    }

    void setViewText(Unit unit){
        LinearLayout container = new LinearLayout(QuestActivity.this);
        container.setBackgroundColor(Color.GRAY);
        container.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(QuestActivity.this);
        EditText et = new EditText(QuestActivity.this);
        tv.setText(unit.getQuestion());
        tv.setTextColor(Color.WHITE);
        et.setTextColor(Color.LTGRAY);
        Log.d("TAG-QuestActivityInf", "question " + unit.getQuestion());
        container.addView(tv);
        container.addView(et);
        container.setPadding(0, 10,0,0);
        questUnitsLayout.addView(container);
        units.add(new GroupOfViewText(((TextUnit)unit).getCorrectAnswer(), tv, et));
    }

    void setViewRadio(Unit unit){
        LinearLayout container = new LinearLayout(QuestActivity.this);
        container.setBackgroundColor(Color.GRAY);
        container.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(QuestActivity.this);
        tv.setText(unit.getQuestion());
        tv.setTextColor(Color.WHITE);
        RadioGroup rg = new RadioGroup(QuestActivity.this);
        for(String ans: ((RadioUnit)unit).getAnswers()){
            RadioButton rb = new RadioButton(QuestActivity.this);
            rb.setText(ans);
            rb.setTextColor(Color.LTGRAY);
            Log.d("TAG-QuestActivityInf", "question " + unit.getQuestion());
            rg.addView(rb);
        }
        container.addView(tv);
        container.addView(rg);
        container.setPadding(0, 10,0,0);
        questUnitsLayout.addView(container);
        units.add(new GroupOfViewRadio(((RadioUnit) unit).getCorrectAnswer(), tv, rg));
    }


}
