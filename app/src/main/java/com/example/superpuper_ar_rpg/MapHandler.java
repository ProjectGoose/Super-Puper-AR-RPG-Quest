package com.example.superpuper_ar_rpg;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;

public class MapHandler implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;
    private SupportMapFragment mapFragment;
    private GoogleMap map; //Добавляем нормальный объект карты, а не все эти извращения
    private ArrayList<Quest> parsedQuests = new ArrayList<>();//Хранит квесты, запарсенные из json(предположительно) файла, полученного от сервера
    private ArrayList<Quest> appendedQuests = new ArrayList<>(); //Хранит установленные на карте в данный момент квесты


    //debug
    public static boolean isMocking = false;
    private LatLng testLL = new LatLng(55.75, 37.61);

    //Объекты для отрисовки карты
    LatLng userLatLng;
    private final float RADIUS = 15f;
    private CircleOptions userCircleOptions = new CircleOptions()
            .center(testLL)
            .fillColor(Color.RED)
            .radius(RADIUS)
            .strokeWidth(0.1f);
    private Circle userCircle;
    private boolean firstStart = true;
    private boolean centering = false;
    private boolean locating = false;

    MapHandler(SupportMapFragment mapFragment, LocationManager locationManager, Context context) {
        this.mapFragment = mapFragment;
        this.mLocationManager = locationManager;
        this.context = context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            userLatLng = new LatLng(testLL.latitude, testLL.longitude);
        }
    }

    public void start() {
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        try {
            if (mLocationManager.getLastKnownLocation("GPS") != null) {
                userLatLng = new LatLng(mLocationManager.getLastKnownLocation("GPS").getLatitude(),
                        mLocationManager.getLastKnownLocation("GPS").getLatitude());
                setUserMarker();
            }
        } catch (SecurityException e) {
            Log.d("TAG10", "SecurityException " + e.getMessage());
        }
        startLocationUpdates();
        getQuests(); //Временный метод - затычка, заполняет parsedQuests
    }


    public void stop() {
        stopLocationUpdates();
    }

    public void setLocating(boolean locating) {
        this.locating = locating;
        start();
        //mapFragment.getMapAsync(this);
    }

    public boolean isLocating() {
        return locating;
    }

    public void setCentering(boolean centering) {
        this.centering = centering;
        mapFragment.getMapAsync(this);
    }

    public boolean isCentering() {
        return centering;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraIdleListener(this); //листенер на передвижение карты
        Log.d("MAP", "Zoom: " + Float.toString(googleMap.getCameraPosition().zoom));
    }

    //колбэк листенера
    @Override
    public void onCameraIdle(){
        VisibleRegion visreg = map.getProjection().getVisibleRegion(); //получает координаты углов области карты, отображаемой на экране в данный момент
        MarkerAppender mApp = new MarkerAppender(visreg);
        if(!mApp.isAlive()) {
            mApp.run();
        }
    }

    //Поток, в котором будут расставляться маркеры квестов
    class MarkerAppender extends Thread {
        VisibleRegion visreg; //хранит
        MarkerAppender(VisibleRegion visreg){
            this.visreg = visreg;
        }
        @Override
        public void run(){
            //ставим маркеры квестов
            for(Quest buf: parsedQuests){
                if(visreg.latLngBounds.contains(buf.coordinates)) {
                    boolean flag = true;
                    for(Quest buf1: appendedQuests){
                        if(buf == buf1){
                            flag = false;
                        }
                    }
                    if(flag) {
                        buf.marker = map.addMarker(new MarkerOptions().position(buf.coordinates).title(buf.title));
                        appendedQuests.add(buf);
                        Log.d("TAG3.1", appendedQuests.get(appendedQuests.size() - 1) + " has been added to the map");
                    }
                }
            }
            //Удаляем маркеры, которые вышли за поле видимости
            Log.d("TAG1234", "Size of appendedQuests = " + appendedQuests.size());
            int size = appendedQuests.size();
            //Хз как сделать через for each, так как получаются траблы с удалением объектов массива
            for(int i = 0; i < size; i++){
                Log.d("TAG123466" , "i = " + i + " size = " +size);
                //если маркер, который находится на карте не в поле видимости, то удаляем его с карты и из массива маркеров, установленных на карте
                if(!visreg.latLngBounds.contains(appendedQuests.get(i).marker.getPosition())){
                    appendedQuests.get(i).marker.remove();
                    Log.d("TAG", appendedQuests.get(i).title + " has been removed");
                    appendedQuests.remove(i);
                    i--;
                    size = appendedQuests.size();
                    Log.d("TAG3.2", "size = " + size);
                }
            }
            Log.d("TAG", "=================================================================");
        }
    }

    //скорее всего будет как то в процессе парсить маркеры в определенной области из json файла с маркерами
    public void getQuests(){
        GetQuestsThread gqt = new GetQuestsThread();
        gqt.run();
    }

    //будет парсить маркеры
    class GetQuestsThread extends Thread{
        @Override
        public void run(){
            parsedQuests.add(new Quest("Проникнуть в рот мирэа", "По неизвестным причинам с 16 марта объяевлен карантин. " +
                    "Возможный лут: резиновые члены, кожаные костюмы, Карпов. " +
                    "Рекомендуется взять автомат, патроны, противогаз", new LatLng(55.669696, 37.481083)));
            parsedQuests.add(new Quest("Взорвать дом разраба", "text", new LatLng(55.671313, 37.285355)));
            parsedQuests.add(new Quest("Общага ВШЭ", "text", new LatLng(55.667187, 37.282811)));
            parsedQuests.add(new Quest("СОШ №1", "text", new LatLng(55.668836, 37.286733)));
            parsedQuests.add(new Quest("квест", "text", new LatLng(55.664982, 37.283637)));
        }
    }


    private void startLocationUpdates(){
            try {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, mlocationListener);
            } catch (SecurityException e) {
                Log.d("TAG1", "SecurityException: " + e.getMessage());
            }
    }

    //Запрещает LocationListener`у запрашивать координаты
    private void stopLocationUpdates() {
        mLocationManager.removeUpdates(mlocationListener);
    }

    //Вынес установку маркера игрока в отдельный метод
    private void setUserMarker() {
        if (firstStart) {
            userCircle = map.addCircle(userCircleOptions);
            firstStart = false;
        }

        if (centering) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(userLatLng)
                    .zoom(map.getCameraPosition().zoom)
                    .build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        if (locating) {
            userCircle.setCenter(userLatLng);
            if (!userCircle.isVisible()) {
                userCircle.setVisible(true);
            }
        } else {
            if (userCircle.isVisible()) {
                userCircle.setVisible(false);
            }
        }

    }


    LocationListener mlocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            Toast.makeText(context, "Location updated " + userLatLng.latitude + " " + userLatLng.longitude, Toast.LENGTH_SHORT).show();
            setUserMarker();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d("GPS", "Provider is enabled");
        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
