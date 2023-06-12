package com.example.event_repo_app;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

public class GeoUtils {
    private static final double EARTH_RADIUS = 6371.0;
    private static final double DEG_TO_RAD = Math.PI / 180.0;

    private GeoUtils() {}

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = degToRad(lat2 - lat1);
        double dLon = degToRad(lon2 - lon1);
        double a = sin(dLat/2.0) * sin(dLat/2.0) + cos(degToRad(lat1)) * cos(degToRad(lat2)) *
                        sin(dLon/2.0) * sin(dLon/2.0);
        double c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a));
        return c * EARTH_RADIUS;
    }

    private static double degToRad(double deg) {
        return deg * DEG_TO_RAD;
    }

    @SuppressLint("MissingPermission")
    public static Location getLastLocation(LocationManager locationManager) {
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (locationGPS == null && locationNetwork != null) {
            return locationNetwork;
        }
        if (locationGPS != null && locationNetwork == null) {
            return locationGPS;
        }
        if (locationGPS != null && locationNetwork != null) {
            return (locationGPS.getTime() > locationNetwork.getTime())? locationGPS : locationNetwork;
        }
        return null;
    }
}
