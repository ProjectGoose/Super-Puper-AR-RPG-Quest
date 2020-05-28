package com.example.superpuper_ar_rpg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.superpuper_ar_rpg.Activities.QuestActivity;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuest;
import com.example.superpuper_ar_rpg.AppObjects.MarkerItem;
import com.example.superpuper_ar_rpg.AppObjects.User;
import com.example.superpuper_ar_rpg.AppObjects.quest.MapQuestBriefDto;
import com.example.superpuper_ar_rpg.Network.NetworkService;
import com.example.superpuper_ar_rpg.Network.QuestsRequestBody;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapHandler implements GoogleMap.OnCameraIdleListener {

    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap map; //Добавляем нормальный объект карты, а не все эти извращения
    private ArrayList<MapQuest> parsedQuests = new ArrayList<>();//Хранит квесты, запарсенные из json(предположительно) файла, полученного от сервера
    private ClusterManager<MarkerItem> clusterManager; //кластеризатор
    private int locationUpdateCounter = 0;
    private LocationCallback locationCallback;
    private Button questInfoButton;

    //debug
    public static boolean isMocking = false;
    private LatLng testLL = new LatLng(55.75, 37.61);

    //Объекты для отрисовки карты
    private final float RADIUS = 15f;
    private CircleOptions userCircleOptions = new CircleOptions()
            .center(testLL)
            .fillColor(Color.RED)
            .radius(RADIUS)
            .strokeWidth(0.1f);
    private Circle userCircle;
    private boolean firstStart = true;
    private boolean centering = false;
    private boolean locating = true;

    public MapHandler(GoogleMap map, Context context, FusedLocationProviderClient fusedLocationClient) {
        this.map = map;
        this.context = context;
        this.fusedLocationClient = fusedLocationClient;

        questInfoButton = ((Activity)context).findViewById(R.id.bt_questInfo);

        //Устанавливаем кластеризацию
        clusterManager = new ClusterManager<MarkerItem>(context, map);
        map.setOnCameraIdleListener(this);
        map.setOnMarkerClickListener(clusterManager);

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
            //При нажатии на маркер:
            @Override
            public boolean onClusterItemClick(MarkerItem item) {
                questInfoButton.setText(item.getTitle() + "\n" + item.getRating());
                questInfoButton.setVisibility(View.VISIBLE);

                questInfoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goToQuest = new Intent(context, QuestActivity.class);
                        goToQuest.putExtra("BriefDto",  new MapQuestBriefDto(item.getId(), item.getTitle(), item.getLatitude(), item.getLongitude(), item.getRating()));
                        goToQuest.putExtra("title", item.getTitle());
                        goToQuest.putExtra("rating", item.getRating());
                        ((Activity)context).startActivity(goToQuest);
                    }
                });
                return true; //true - чтобы отключить дефолтное поведение при нажатии
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("TAG-MapHandlerInf", "Map clicked");
                questInfoButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void start() {
        startLocationUpdates();
        getQuests();
    }

    public void stop() {
        stopLocationUpdates();
    }

    public void setLocating(boolean locating) {
        this.locating = locating;
        start();
    }

    public boolean isLocating() {
        return locating;
    }

    public void setCentering(boolean centering) { this.centering = centering; }

    public boolean isCentering() {
        return centering;
    }

    public void zoomUser(){
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    private boolean flag = true; //временно
    @Override
    public void onCameraIdle(){
        Log.d("TAG", "onCameraIdle");
        VisibleRegion visReg = map.getProjection().getVisibleRegion();
        ArrayList<MapQuestBriefDto> parsedQuests1 = new ArrayList<>();
        NetworkService.getInstance().requestQuestsInRange(User.getInstance().getToken(), new QuestsRequestBody(visReg), new Callback<ArrayList<MapQuestBriefDto>>() {
            @Override
            public void onResponse(Call<ArrayList<MapQuestBriefDto>> call, Response<ArrayList<MapQuestBriefDto>> response) {
                if(response.isSuccessful()){
                    Log.d("NETWORKInf", "Server returned array with " + response.body().size() + " quests");
                    parsedQuests1.addAll(response.body());
                    ArrayList<Marker> appendedMarkers = new ArrayList<>(clusterManager.getClusterMarkerCollection().getMarkers());
                    clusterManager.clearItems();
                    for(MapQuestBriefDto buf: parsedQuests1){
                        Log.d("TAG", "buf " + buf.getLatitude() + " " + buf.getLongitude());
                        clusterManager.addItem(new MarkerItem(buf.getLatitude(), buf.getLongitude(), buf.getTitle(), buf.getRating(),  buf.getId()));
                    }
                    clusterManager.cluster();
                } else {
                    Log.d("NETWORKInf", "Response is unsuccessful");
                }

            }
            @Override
            public void onFailure(Call<ArrayList<MapQuestBriefDto>> call, Throwable t) {
                Log.d("NETWORKInf", "Failed to get quests");
            }
        });
        Log.d("TAG-MapHandler-Inf", "Received: " + parsedQuests1.size());

    }

    public void getQuests(){
        Log.d("TAG", "getQuests");
        GetQuestsThread gqt = new GetQuestsThread();
        gqt.start();
    }

    //будет парсить маркеры
    class GetQuestsThread extends Thread{
        @Override
        public void run(){
//            parsedQuests.add(new MapQuest("Проникнуть в рот мирэа",
//                    "Возможный лут: кожаные костюмы, Карпов.\nОсобо опасно! ", new LatLng(55.669696, 37.481083), 10));
//            parsedQuests.add(new MapQuest("Взорвать дом разрабу", "text", new LatLng(55.671313, 37.285355), 2));
//            parsedQuests.add(new MapQuest("Общага ВШЭ", "text", new LatLng(55.667187, 37.282811), 8));
//            parsedQuests.add(new MapQuest("СОШ №1", "text", new LatLng(55.668836, 37.286733), 0));
//            parsedQuests.add(new MapQuest("квест", "text", new LatLng(55.664982, 37.283637), 6));
        }
    }


    private void startLocationUpdates(){
        //Инициализируем User.coordinates последними координатами
        fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    User.getInstance().setCoordinates(new LatLng(location.getLatitude(), location.getLongitude()));
                    Log.d("TAG-MapHandlerInf", "LastLocation set");
                    Toast.makeText(context, "LocationSet, " + User.getInstance().getCoordinates(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG-MapHandlerErr", "LastLocation can not be set");
                    Toast.makeText(context, "Location is not set", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        locationCallback = setLocationCallback();
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, mlocationListener);
        } catch (NullPointerException e) {
            Log.d("TAG1", "SecurityException: " + e.getMessage());
        }
    }

    private LocationCallback setLocationCallback(){
        locationCallback = new LocationCallback(){
            int n = 10;
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult != null){
                    Location loc = locationResult.getLocations().get(locationResult.getLocations().size()-1);
                    locationUpdateCounter++;
                    User.getInstance().setCoordinates(new LatLng(loc.getLatitude(), loc.getLongitude()));
                    //Обновляем местоположение пользователя на сервере каждые n приемов GPS
                    if(locationUpdateCounter == n){
                        locationUpdateCounter = 0;
                        NetworkService.getInstance().sendLocation(User.getInstance().getToken(), new LatLng(loc.getLatitude(), loc.getLongitude()));
                    }
                    //Toast.makeText(context, "Location updated " + User.getInstance().getCoordinates().latitude + " " + User.getInstance().getCoordinates().longitude, Toast.LENGTH_SHORT).show();
                    setUserMarker();
                } else {
                    Log.d("TAG-MapHandlerErr", "received location == null");
                }
            }
        };
        return (locationCallback);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        Log.d("TAG-MapHandlerInf", "Location updates removed");
    }

    //Вынес установку маркера игрока в отдельный метод
    private void setUserMarker() {
        if (firstStart) {
            userCircle = map.addCircle(userCircleOptions);
            firstStart = false;
        }

        if (centering) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(User.getInstance().getCoordinates())
                    .zoom(map.getCameraPosition().zoom)
                    .build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        if (locating) {
            userCircle.setCenter(User.getInstance().getCoordinates());
            if (!userCircle.isVisible()) {
                userCircle.setVisible(true);
            }
        } else {
            if (userCircle.isVisible()) {
                userCircle.setVisible(false);
            }
        }

    }
}
