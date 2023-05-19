package com.example.event_repo_app;

import static com.example.event_repo_app.Constants.EVENTS_EXTRA;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import database.Event;

@SuppressWarnings("UnstableApiUsage")
public class MapActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        Configuration.getInstance().load(context,
                PreferenceManager.getDefaultSharedPreferences(context));

        setContentView(R.layout.activity_map);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        Gson gson = new Gson();
        Type eventsType = new TypeToken<List<Event>>() {}.getType();
        ArrayList<Event> events = gson.fromJson(getIntent().getStringExtra(EVENTS_EXTRA), eventsType);

        for (Event event : events) {
            map.getOverlays().add(getMarker(event));
        }
        map.invalidate();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset((int) (dm.widthPixels * 0.75), 10);
        map.getOverlays().add(mScaleBarOverlay);

        map.setMinZoomLevel(7d);
        map.addOnFirstLayoutListener((v, left, top, right, bottom) -> {
            double lat = events.get(0).getLatitude();
            double lon = events.get(0).getLongitude();
            BoundingBox b = new BoundingBox(lat - 10, lon + 10, lat + 10, lon - 10);
            map.zoomToBoundingBox(b, false, 100);
            map.invalidate();
        });

        requestPermissionsIfNecessary(new String[]{
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
    }

    private Marker getMarker(Event event) {
        GeoPoint startPoint = new GeoPoint(event.getLatitude(), event.getLongitude());
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setTitle(event.getName() + "\n" + event.getLocation());
        return marker;
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>(Arrays.asList(permissions));
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}