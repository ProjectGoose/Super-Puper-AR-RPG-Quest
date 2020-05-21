package com.example.superpuper_ar_rpg.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.superpuper_ar_rpg.AppObjects.MapQuest;
import com.example.superpuper_ar_rpg.AppObjects.MarkerItem;
import com.example.superpuper_ar_rpg.AppObjects.User;
import com.example.superpuper_ar_rpg.Network.NetworkService;
import com.example.superpuper_ar_rpg.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestActivity extends AppCompatActivity {

    private TextView tv_title, tv_rating, tv_description;
    private Button bt_accept;
    private boolean isCloseEnough;
    private boolean isAccepted = false;
    MapQuest thisQuest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        tv_title = findViewById(R.id.tv_title);
        tv_rating = findViewById(R.id.tv_rating);
        tv_description = findViewById(R.id.tv_description);
        bt_accept = findViewById(R.id.bt_accept);

        thisQuest = new MapQuest((getIntent().getExtras().getString("title")),
                "",
                new LatLng(getIntent().getExtras().getDouble("lat"),
                getIntent().getExtras().getDouble("lng")),
                getIntent().getExtras().getDouble("rating"),
                getIntent().getExtras().getLong("questID"));

        tv_title.setText(thisQuest.getTitle());
        tv_rating.setText(Double.toString(thisQuest.getRating()));

        //Делаем запрос на параметры квеста и устанавливаем оставшиеся поля
        NetworkService.getInstance().requestQuestBody(User.getInstance().getToken(), getIntent().getExtras().getLong("questID"), new Callback<MapQuest>() {
            @Override
            public void onResponse(Call<MapQuest> call, Response<MapQuest> response) {
                if(response.isSuccessful()) {
                    thisQuest.setCoordinates(response.body().getCoordinates());
                    thisQuest.setAuthor(response.body().getAuthor());
                    //Log.d("TAG-QuestActivityInf", response.body().getAuthor() + " " + response.body().getText());
                    //Log.d("TAG-QuestActivityInf", "lat: " + getIntent().getExtras().getDouble("lat") + "lng: " + getIntent().getExtras().getDouble("lng"));
                } else {
                    Log.d("TAG-QuestActivityErr", "Smth goes wrong");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        //устанавливаем надпись на кнопке
        setButtonState();

        bt_accept.setOnClickListener(new View.OnClickListener() {
            //Возможно, нужно создать отдельный класс Application, храянящий все настраиваемые параметры, типа радиус доступности квеста
            LatLngBounds bounds = toBounds(thisQuest.getCoordinates(), 20);
            @Override
            public void onClick(View v) {
                if(bounds.contains(User.getInstance().getCoordinates())){

                } else {
                    Toast.makeText(QuestActivity.this, R.string.toast_closeToQuest, Toast.LENGTH_SHORT).show();
                }
            }
        });


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
        //надо вынести в поток?
        for(long id: User.getInstance().getActiveQuests()){
            if(thisQuest.getId() == id){
                isAccepted = true;
            }
        }
        if(isAccepted){
            bt_accept.setText(R.string.btn_rejectQuest);
        }
    }
}
