package com.example.superpuper_ar_rpg;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapHandler implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;
    private GoogleMap map; //Добавляем нормальный объект карты, а не все эти извращения
    private ArrayList<MapQuest> parsedQuests = new ArrayList<>();//Хранит квесты, запарсенные из json(предположительно) файла, полученного от сервера
    private ClusterManager<MarkerItem> clusterManager; //кластеризатор
    private MapView mapView;

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
    private boolean locating = false;

    public MapHandler(MapView mapView, LocationManager locationManager, Context context) {
        this.mapView = mapView;
        this.mLocationManager = locationManager;
        this.context = context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            User.getInstance().setCoordinates(new LatLng(testLL.latitude, testLL.longitude));
        }
    }

    public void start() {
        mapView.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        try {
            if (mLocationManager.getLastKnownLocation("GPS") != null) {
                User.getInstance().setCoordinates(new LatLng(mLocationManager.getLastKnownLocation("GPS").getLatitude(),
                        mLocationManager.getLastKnownLocation("GPS").getLatitude()));
                setUserMarker();
            }
        } catch (SecurityException e) {
            Log.d("TAG10", "SecurityException " + e.getMessage());
        }
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

    public void setCentering(boolean centering) {
        this.centering = centering;
        mapView.getMapAsync(this);
    }

    public boolean isCentering() {
        return centering;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraIdleListener(this);
        Log.d("MAP", "Zoom: " + Float.toString(googleMap.getCameraPosition().zoom));
        clusterManager = new ClusterManager<MarkerItem>(context, map);
        //map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        /*for(int i =0; i < parsedQuests.size(); i++) {
            MarkerItem markerItem = new MarkerItem(parsedQuests.get(i).coordinates.latitude,parsedQuests.get(i).coordinates.longitude);
            clusterManager.addItem(markerItem);
        }*/
    }

    //колбэк листенера
    /*@Override
    public void onCameraIdle(){
        *//*VisibleRegion visreg = map.getProjection().getVisibleRegion(); //получает координаты углов области карты, отображаемой на экране в данный момент
        MarkerAppender mApp = new MarkerAppender(visreg, parsedQuests);
        if(!mApp.isAlive()) {
            //mApp.start();
            Log.d("TAG", "Thread started");
        }*//*
    }*/

    private boolean flag = true;
    @Override
    public void onCameraIdle(){
        Log.d("TAG", "onCameraIdle");
        VisibleRegion visReg = map.getProjection().getVisibleRegion();
        ArrayList<MapQuest> parsedQuests1 = new ArrayList<>();
        //parsedQuests1 = NetworkService.getInstance().requestQuests(new QuestsBody(User.getInstance().getCoordinates(), visReg, "12345678"));
        if(flag) {
            parsedQuests1 = parsedQuests; //затычка
            flag = false;
        } else {
            parsedQuests1.clear();
            flag = true;
            Log.d("TAG", "flag = " + flag);
        }
        ArrayList<Marker> appendedMarkers = new ArrayList<>(clusterManager.getClusterMarkerCollection().getMarkers());
        clusterManager.clearItems();
        for(MapQuest buf: parsedQuests1){
            Log.d("TAG", "buf " + buf.coordinates);
            clusterManager.addItem(new MarkerItem(buf.coordinates.latitude, buf.coordinates.longitude));
        }
        clusterManager.cluster();
    }


    //скорее всего будет как то в процессе парсить маркеры в определенной области из json файла с маркерами
    public void getQuests(){
        Log.d("TAG", "getQuests");
        GetQuestsThread gqt = new GetQuestsThread();
        gqt.start();
    }

    //будет парсить маркеры
    class GetQuestsThread extends Thread{
        @Override
        public void run(){
            parsedQuests.add(new MapQuest("Проникнуть в рот мирэа", "По неизвестным причинам с 16 марта объяевлен карантин. " +
                    "Возможный лут: резиновые члены, кожаные костюмы, Карпов. " +
                    "Рекомендуется взять автомат, патроны, противогаз", new LatLng(55.669696, 37.481083)));
            parsedQuests.add(new MapQuest("Взорвать дом разраба", "text", new LatLng(55.671313, 37.285355)));
            parsedQuests.add(new MapQuest("Общага ВШЭ", "text", new LatLng(55.667187, 37.282811)));
            parsedQuests.add(new MapQuest("СОШ №1", "text", new LatLng(55.668836, 37.286733)));
            parsedQuests.add(new MapQuest("квест", "text", new LatLng(55.664982, 37.283637)));
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


    LocationListener mlocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            User.getInstance().setCoordinates(new LatLng(location.getLatitude(), location.getLongitude()));
            Toast.makeText(context, "Location updated " + User.getInstance().getCoordinates().latitude + " " + User.getInstance().getCoordinates().longitude, Toast.LENGTH_SHORT).show();
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
