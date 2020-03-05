package com.example.superpuper_ar_rpg;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
//    private int locationRequestCode = 1000;
    private LocationManager mLocationManager;
    private double wayLatitude =  43, wayLongitude = 169;
    private SupportMapFragment mapFragment;

    ArrayList<LatLng> track = new ArrayList<>(); //Трэк

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //привязываем locationManager к сервису контроля за gps???

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putAll(outState);
        outState.putParcelableArrayList("track", track);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Проверяем, включен ли gps
        if(!mLocationManager.isProviderEnabled("gps")){
            GPSEnableDialog gpsEnable = new GPSEnableDialog();
            gpsEnable.show(getSupportFragmentManager(), "what`s the tag?");
        }

        //проверям, разрешено ли использовать gps (надо дописать)
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }

        //Обновляем координаты вызывая mlocationListener через callback
        mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 100000, 100, mlocationListener);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedInstanceState.getBundle("");
        track = savedInstanceState.getParcelableArrayList("track");
        for (LatLng pos: track) {
            mMap.addMarker(new MarkerOptions().position(pos).title("You were here"));
        }
    }

    //создаем окно с просьбой включить gps
    public static class GPSEnableDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_GPSEnableMessage);
            builder.setPositiveButton(R.string.dialog_GPSEnableAgree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //пиздуй в настройки включать геолокацию
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.dialog_GPSEnableRefuse, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //ну как хочешь
                    dialogInterface.cancel();
                }
            });
            return builder.create();
        }
    }

    //реализуем интерфейс LocationListener
    LocationListener mlocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //при обновлении локации обновляем карту
            Toast.makeText(MapsActivity.this, "LocationChanged", Toast.LENGTH_SHORT).show();
            mapFragment.getMapAsync(MapsActivity.this);
            LatLng myCoordinates = new LatLng(location.getLatitude(),location.getLongitude());
            track.add(myCoordinates);
            mMap.addMarker(new MarkerOptions().position(myCoordinates).title("Marker in ... what is it?"));
            Log.d("LocationChanged", "Longtitude " + location.getLongitude() + " Latitude " + location.getLatitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
//                    LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
//                    Log.d("TAG2", "Latitude " + wayLatitude + " Longtitude " + wayLongitude);
//                    track.add(new LatLng())
//
//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions().position(myCoordinates).title("Marker in ... what is it?"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
//                    Toast.makeText(MapsActivity.this, "Ha ha, found you, Lenny", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MapsActivity.this, "Location == null", Toast.LENGTH_SHORT).show();
                    Log.e("TAG3", "Latitude " + wayLatitude + " Longtitude " + wayLongitude);
                }
        });
    }
}
