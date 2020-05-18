package com.example.superpuper_ar_rpg.Network;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

public class QuestsRequestBody {
    private double top;
    private double left;
    private double bottom;
    private double right;

    public double getTop() {
        return top;
    }

    public double getLeft() {
        return left;
    }

    public double getBottom() {
        return bottom;
    }

    public double getRight() {
        return right;
    }

    public QuestsRequestBody(VisibleRegion visReg){
        this.top = visReg.nearRight.longitude;
        this.bottom = visReg.nearLeft.longitude;
        this.left = visReg.nearLeft.latitude;
        this.right = visReg.farLeft.latitude;
    }
}
