package com.example.superpuper_ar_rpg.Activities;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.superpuper_ar_rpg.AppObjects.User;
import com.example.superpuper_ar_rpg.MapHandler;
import com.example.superpuper_ar_rpg.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static LocationManager mLocationManager;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private int requestCode;
    private final String permissions[]= {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION, INTERNET};

    public MapHandler mapHandler;
    private GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        //Портретная ориентация, чтобы не было лишней головной боли пока
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //А вот тут убираем стандартную верхнюю панель
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Блок, отвечающий за панель навигации: устанавливаем контроллер на панель
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);


        if(!checkPermission(this, permissions)){
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d("ActivityState", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //activateMap(mapView); //удивительно, но если это раскомментировать, то при входе из описания квеста карта ломается
        Log.d("ActivityState", "onStart");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        mapHandler = new MapHandler(gmap, this, fusedLocationClient);
        mapHandler.start();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Проверяем, включен ли gps
        if (!mLocationManager.isProviderEnabled("gps")) {
            GPSEnableDialog gpsEnable = new GPSEnableDialog();
            gpsEnable.show(getSupportFragmentManager(), "what`s the tag?");
        }
        Log.d("ActivityState", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ActivityState", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapHandler.stop();
        Log.d("ActivityState", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ActivityState", "onDestroy");
    }

    //Вызывается фрагментом при каждом его пересоздании
    public void activateMap(MapView thisMapView){
        thisMapView = findViewById(R.id.map);
        thisMapView.getMapAsync(this);
    }

    private boolean checkPermission(Context context, String[] permissions){
        Boolean flag = true;
        for(String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG-MapsActivityInf", "Permission " + permission + " is not granted");
                flag = false;
            }
        }
        return(flag);
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
                    //отправляем в настройки включать геолокацию
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.dialog_GPSEnableRefuse, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //не включаем
                    dialogInterface.cancel();
                }
            });
            return builder.create();
        }
    }

    public void onMapInteractionButtonPressed(View view){
        Log.d("Button", "Ouu, my");
        if (view.getId() == R.id.btn_centering) {
            if (mapHandler.isCentering()) {
                mapHandler.setCentering(false);
                view.setBackground(getDrawable(R.drawable.centering_off));
            } else {
                if(mLocationManager.isProviderEnabled("gps")){
                    mapHandler.setCentering(true);
                    mapHandler.zoomUser();
                    view.setBackground(getDrawable(R.drawable.centering_on));
                }
            }
        }

    }
}
