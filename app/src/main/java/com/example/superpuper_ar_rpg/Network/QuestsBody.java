package com.example.superpuper_ar_rpg.Network;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

public class QuestsBody {
    String token;
    double latitude;
    double longitude;
    double topLeftLatitude;
    double topLeftLongitude;
    double bottomRightLatitude;
    double bottomRightLongitude;

    QuestsBody(LatLng location, VisibleRegion visReg, String token){
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.topLeftLatitude = visReg.farLeft.latitude;
        this.topLeftLongitude = visReg.farLeft.longitude;
        this.bottomRightLatitude = visReg.nearRight.latitude;
        this.bottomRightLatitude = visReg.nearRight.longitude;
        this.token = token;
    }
}
