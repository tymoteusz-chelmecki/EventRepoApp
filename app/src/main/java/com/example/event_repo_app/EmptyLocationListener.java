package com.example.event_repo_app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class EmptyLocationListener implements LocationListener {
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onLocationChanged(final Location location) {}
}
