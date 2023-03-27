package com.example.event_repo_app;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

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
}
