package com.example.event_repo_app;

import android.app.Application;
import android.location.LocationManager;

public class EventApplication extends Application {
    public static final int REQUEST_LOCATION_ACCESS = 1111;
    private LocationManager locationManager;

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }
}