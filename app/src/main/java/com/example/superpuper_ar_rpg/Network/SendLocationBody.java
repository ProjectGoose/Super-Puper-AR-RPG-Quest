package com.example.superpuper_ar_rpg.Network;

public class SendLocationBody {
    private double latitude;
    private double longitude;

    SendLocationBody(double lat, double lng){
        this.latitude = lat;
        this.longitude = lng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
