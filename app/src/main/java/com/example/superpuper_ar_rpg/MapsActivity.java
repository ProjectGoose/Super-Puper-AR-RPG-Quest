package com.example.superpuper_ar_rpg;

import androidx.appcompat.widget.Toolbar;
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MapsActivity extends FragmentActivity {

    private SupportMapFragment mapFragment;
    public static LocationManager mLocationManager;
    private ImageButton btnFindLocation;
    private ImageButton btnCentering;
    private MapView mapView;

    public MapHandler mapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Портретная ориентация, чтобы не было лишней головной боли пока
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //А вот тут убираем стандартную верхнюю панель
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Блок, отвечающий за панель навигации
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);

        //привязываем locationManager к сервису контроля за gps???
        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        // Получаем SupportMapFragment и получаем callback когда карта будет готова к работе
        //
        /*

        btnFindLocation = findViewById(R.id.btn_find_location);
        btnFindLocation.setBackground(getDrawable(R.drawable.target_off));
        btnCentering = findViewById(R.id.btn_centering);
        btnCentering.setVisibility(View.INVISIBLE);
        btnCentering.setBackground(getDrawable(R.drawable.centering_off));
        btnCentering.setClickable(false);
*/

        Log.d("ActivityState", "onCreate");
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView = findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapHandler = new MapHandler(mapView, mLocationManager, this);
            mapHandler.start();
        } else {
            Log.d("TAG", "mapView = null");
        }
        //проверям, разрешено ли использовать gps (надо дописать)
        Log.d("ActivityState", "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //TODO Permission is not granted
        }
        //Проверяем, включен ли gps
        if (!mLocationManager.isProviderEnabled("gps")) {
            GPSEnableDialog gpsEnable = new GPSEnableDialog();
            gpsEnable.show(getSupportFragmentManager(), "what`s the tag?");
        }
        //Если всё включено, то запускаем хэндлер

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

    /*public void onMapInteractionButtonPressed(View view){
        Log.d("Button", "Ouu, my");
        switch (view.getId()){
            case R.id.btn_find_location:
                if(mapHandler.isLocating()){
                    btnFindLocation.setBackground(getDrawable(R.drawable.target_off));
                    mapHandler.setLocating(false);
                    btnCentering.setVisibility(View.INVISIBLE);
                    btnCentering.setClickable(false);
                }
                else{
                    btnFindLocation.setBackground(getDrawable(R.drawable.target_on));
                    mapHandler.setLocating(true);
                    mapHandler.setCentering(false);
                    btnCentering.setVisibility(View.VISIBLE);
                    btnCentering.setClickable(true);
                }
                break;
            case R.id.btn_centering:
                if(mapHandler.isCentering()){
                    mapHandler.setCentering(false);
                    btnCentering.setBackground(getDrawable(R.drawable.centering_off));
                }
                else {
                    mapHandler.setCentering(true);
                    btnCentering.setBackground(getDrawable(R.drawable.centering_on));
                }
                break;
        }

    }*/
}
