package com.example.superpuper_ar_rpg;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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

import static android.content.Context.LOCATION_SERVICE;

public class MapHandler implements OnMapReadyCallback {

    private Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;
    private SupportMapFragment mapFragment;

    private MovingUserCoordinates mu;

    //debug
    public static boolean isMocking = false;
    private LatLng testLL = new LatLng(55.75,37.61);

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

    MapHandler(SupportMapFragment mapFragment, LocationManager locationManager, Context context){
        this.mapFragment = mapFragment;
        this.mLocationManager = locationManager;
        this.context = context;
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            userLatLng = new LatLng(testLL.latitude, testLL.longitude);
        }
        mu = new MovingUserCoordinates(this);
    }

    public void start(){
        if(locating) {
            if (isMocking)
                userLatLng = new LatLng(testLL.latitude, testLL.longitude);
            else {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                try {
                    if (mLocationManager.getLastKnownLocation("GPS") != null) {
                        userLatLng = new LatLng(mLocationManager.getLastKnownLocation("GPS").getLatitude(),
                                mLocationManager.getLastKnownLocation("GPS").getLatitude());
                    }
                } catch (SecurityException e) {
                    Log.d("TAG10", "SecurityException " + e.getMessage());
                }
            }
        }
        if (!mu.isPlaying && locating)
            mu.execute();
    }

    public void stop(){
        mu.stop();
    }

    public void setLocating(boolean locating){
        this.locating = locating;
        start();
        mapFragment.getMapAsync(this);
    }
    public boolean isLocating() {
        return locating;
    }

    public void setCentering(boolean centering){
        this.centering = centering;
        mapFragment.getMapAsync(this);
    }
    public boolean isCentering(){
        return centering;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(firstStart){
            userCircle = googleMap.addCircle(userCircleOptions);
            firstStart = false;
        }

        if(centering) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(userLatLng)
                    .zoom(googleMap.getCameraPosition().zoom)
                    .build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        if(locating) {
            userCircle.setCenter(userLatLng);
            if(!userCircle.isVisible()){
                userCircle.setVisible(true);
            }
        }
        else{
            if(userCircle.isVisible()){
                userCircle.setVisible(false);
            }
        }
        Log.d("MAP", "Zoom: " + Float.toString(googleMap.getCameraPosition().zoom));
    }

    private class MovingUserCoordinates extends AsyncTask<Void, LatLng, Void> {
        private volatile boolean isPlaying = false;

        private OnMapReadyCallback callback;

        LocationListener mlocationListener;

        MovingUserCoordinates(OnMapReadyCallback callback) {
            this.callback = callback;
            mlocationListener = new LocationListener() {
                //реализуем интерфейс LocationListener
                //Обновлять карту имеет смысл только при обновлении координат, при этом это невозможно сделать в методе doInBackground,
                //значит это надо делать в onProgressUpdate, значит метод publishProgress должен вызываться в onLocationChanged,
                //поэтому реализовать интерфейс LocationListener нужно внутри MovingUserCoordinates
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("TAG1", location.getLatitude() + " " + location.getLongitude());
                    publishProgress(new LatLng(location.getLatitude(), location.getLongitude()));
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //проверяем разрешение на gps, если нет, то просим подтвердить, это уже прописано в LoginActivty,
            // здесь - на всякий случай, иначе вылетает SecurityException
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mapFragment.getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 42);
            }

            isPlaying = true;
        }

        @Override
        protected Void doInBackground(Void... values) {
            while (isPlaying) {
                if (isMocking) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("TAG3", "Mocking AsyncTask started");
                    publishProgress(new LatLng(userLatLng.latitude, userLatLng.longitude + 0.0001f));
                } else if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                    //Создаем свой Looper, который нужен, чтобы...
                    Looper lpr = Looper.myLooper();
                    lpr.prepare();
                    Log.d("TAG2", "GPS AsyncTask started");
                    try {
                        if(locating)
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                1000, 1, mlocationListener);
                    } catch (SecurityException e) {
                        Log.d("TAG2.2", "SecurityException" + e.getMessage());
                    }
                    lpr.loop();
                    lpr.quitSafely();
                } else {
                    Log.d("TAG7", "No permission");
                }
            }
            Log.d("TAG9", "AsyncTask completed");
            return null;
        }

        void stop() {
            isPlaying = false;
            Log.d("TAG8", "MapHandler stopped, isPlaying = " + isPlaying);
        }

        @Override
        protected void onProgressUpdate(LatLng... values) {
            super.onProgressUpdate(values);
            userLatLng = values[0];
            Log.d("TAG6", userLatLng.latitude + " " + userLatLng.longitude);
            mapFragment.getMapAsync(callback);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("MAP", "not Playing");
        }
    }
}
