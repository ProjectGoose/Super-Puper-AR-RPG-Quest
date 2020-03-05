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
import android.util.Log;
import android.widget.Toast;

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

import static android.content.Context.LOCATION_SERVICE;

public class MapHandler implements OnMapReadyCallback {

    private Context context;
    private GoogleMap mMap;
    private boolean mapReady = false; //На случай, если карта не готова, чтобы не словить NullPointer
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;
    private SupportMapFragment mapFragment;

    private MovingUserCoordinates mu = new MovingUserCoordinates(this);

    //debug
    public static boolean isMocking = false;
    private LatLng testLL = new LatLng(10,10);

    //Объекты для отрисовки карты
    LatLng userLatLng;
    CircleOptions userCircleOptions = new CircleOptions()
            .center(testLL)
            .fillColor(Color.RED)
            .radius(1200);
    Circle userCircle;
    boolean firstStart = true;

    public void start(){
        if(isMocking)
            userLatLng = new LatLng(testLL.latitude, testLL.longitude);
        else{
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        }
        mu.execute();
    }

    public void stop(){
        mu.stop();
    }

    MapHandler(SupportMapFragment mapFragment, LocationManager locationManager, Context context){
        this.mapFragment = mapFragment;
        this.mLocationManager = locationManager;
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            userLatLng = new LatLng(testLL.latitude, testLL.longitude);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(firstStart){
            userCircle = googleMap.addCircle(userCircleOptions);
            firstStart = false;
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLatLng)
                .zoom(10)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        userCircle.setCenter(userLatLng);
        Log.d("MAP", "MapReady");
    }


    private class MovingUserCoordinates extends AsyncTask<Void, LatLng, Void> {
        private volatile boolean isPlaying = false;

        private OnMapReadyCallback callback;
        MovingUserCoordinates(OnMapReadyCallback callback){
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... values) {
            isPlaying = true;
            while (isPlaying) {
                if(isMocking) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(new LatLng(userLatLng.latitude, userLatLng.longitude + 0.01f));
                } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER,
                            1000, 1, mlocationListener);
                }
            }
            return null;
        }

        void stop(){
            isPlaying = false;
        }

        @Override
        protected void onProgressUpdate(LatLng... values) {
            super.onProgressUpdate(values);
            userLatLng = values[0];
            Log.d("MAP",userLatLng.latitude + " " + userLatLng.longitude  );
            mapFragment.getMapAsync(callback);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isPlaying = false;
            Log.d("MAP","not Playing"  );
        }
    }

    //реализуем интерфейс LocationListener
    LocationListener mlocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //обновляем значение локации игрока
            userLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            //при обновлении локации обновляем карту
            mapFragment.getMapAsync(MapHandler.this);

            Log.d("LocationChanged", "Longtitude " + location.getLongitude() + " Latitude " + location.getLatitude());
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
