package com.example.superpuper_ar_rpg.UI;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.superpuper_ar_rpg.Activities.MapsActivity;
import com.example.superpuper_ar_rpg.R;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends Fragment{

    MapView mapView;
    private com.google.android.gms.maps.MapFragment mapFragment1;
    Fragment fg;
    SupportMapFragment supportMapFragment;
    View view;
    ImageButton btn_isCentering;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ActivityStateFrag", "OnCreate");
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d("ActivityStateFrag", "OnCreateView");
        view = inflater.inflate(R.layout.fragment_map, container, false);
        return(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        btn_isCentering = view.findViewById(R.id.btn_centering);
        btn_isCentering.setBackground(view.getContext().getDrawable(R.drawable.centering_off));
        btn_isCentering.setClickable(true);
        Log.d("ActivityState", "OnStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("ActivityStateFrag", "OnResume");
        mapView.onResume();
        //((MapsActivity)getActivity()).activateMap(mapView);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("ActivityStateFrag", "OnPause");
        mapView.onPause();
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d("ActivityStateFrag", "OnStop");
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("ActivityStateFrag", "OnDestroyView");
        mapView.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ActivityStateFrag", "OnDestroy");
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d("ActivityStateFrag", "OnAttach");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Log.d("ActivityStateFrag", "OnViewCreated");
        mapView =  (MapView) view.findViewById(R.id.map);
        if(mapView != null){
            Log.d("TAG", "mapView != null");
            mapView.onCreate(null);
            ((MapsActivity)getActivity()).activateMap(mapView);
        }
    }

}