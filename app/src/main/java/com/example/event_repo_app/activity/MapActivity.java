package com.example.event_repo_app.activity;

import static com.example.event_repo_app.Constants.EVENTS_EXTRA;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.event_repo_app.R;
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
import java.util.List;

import database.Event;

@SuppressWarnings("UnstableApiUsage")
public class MapActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private static final double MIN_ZOOM_LEVEL = 7.0;

    protected Context context;
    protected MapView map;
    protected ArrayList<Event> events;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        Configuration.getInstance().load(context,
                PreferenceManager.getDefaultSharedPreferences(context));
        requestPermissionsIfNecessary();

        setContentView(R.layout.activity_map);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMinZoomLevel(MIN_ZOOM_LEVEL);

        getEventsFromIntent();

        addEventMarkersToMap();

        setScaleBarOverLay();

        setInitialZoomAndPosition();
    }

    protected void getEventsFromIntent() {
        Gson gson = new Gson();
        Type eventsType = new TypeToken<List<Event>>() {}.getType();
        events = gson.fromJson(getIntent().getStringExtra(EVENTS_EXTRA), eventsType);
    }

    private void setScaleBarOverLay() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(map);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(displayMetrics.widthPixels / 2, 10);
        map.getOverlays().add(scaleBarOverlay);
    }

    protected void addEventMarkersToMap() {
        for (Event event : events) {
            map.getOverlays().add(getMarker(event));
        }
        map.invalidate();
    }

    private Marker getMarker(Event event) {
        GeoPoint startPoint = new GeoPoint(event.getLatitude(), event.getLongitude());
        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setTitle(event.getName() + "\n" + event.getLocation());
        return marker;
    }

    protected void setInitialZoomAndPosition() {
        double firstLatitude = events.get(0).getLatitude();
        double firstLongitude = events.get(0).getLongitude();
        double minLatitude = firstLatitude;
        double maxLatitude = firstLatitude;
        double minLongitude = firstLongitude;
        double maxLongitude = firstLongitude;

        for (Event event : events) {
            double latitude = event.getLatitude();
            double longitude = event.getLongitude();
            minLatitude = Math.min(minLatitude, latitude);
            maxLatitude = Math.max(maxLatitude, latitude);
            minLongitude = Math.min(minLongitude, longitude);
            maxLongitude = Math.max(maxLongitude, longitude);
        }

        double finalMaxLatitude = maxLatitude;
        double finalMaxLongitude = maxLongitude;
        double finalMinLatitude = minLatitude;
        double finalMinLongitude = minLongitude;
        map.addOnFirstLayoutListener((v, left, top, right, bottom) -> {
            BoundingBox box = new BoundingBox(finalMaxLatitude + 0.02, finalMaxLongitude + 0.02,
                    finalMinLatitude - 0.02, finalMinLongitude - 0.02);
            map.zoomToBoundingBox(box, false, 100);
            map.invalidate();
        });
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
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Failed to get necessary permissions", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermissionsIfNecessary() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}