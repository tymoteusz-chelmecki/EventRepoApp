package com.example.event_repo_app.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.Manifest.permission.INTERNET;
import static com.example.event_repo_app.Constants.EVENTS_EXTRA;
import static com.example.event_repo_app.EventApplication.REQUEST_LOCATION_ACCESS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.event_repo_app.EventApplication;
import com.example.event_repo_app.R;
import com.google.gson.Gson;

import java.util.Arrays;

import database.Event;

public class MainActivity extends AppCompatActivity {
    private final static String[] PERMISSIONS = new String[]{
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION,
        INTERNET,
        ACCESS_NETWORK_STATE,
        BLUETOOTH,
        BLUETOOTH_ADMIN,
        BLUETOOTH_CONNECT,
        BLUETOOTH_SCAN
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createButton = findViewById(R.id.button_create);
        createButton.setOnClickListener(startNewActivity(CreateEventActivity.class));
        Button browseButton = findViewById(R.id.button_browse);
        browseButton.setOnClickListener(startNewActivity(BrowseEventsActivity.class));
        Button shareButton = findViewById(R.id.button_share);
        shareButton.setOnClickListener(startNewActivity(ShareEventsActivity.class));
        Button showLocationButton = findViewById(R.id.button_show_location);
        showLocationButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            Event event1 = new Event("name", "loc", 20, 9, 2023, 12, 45, 10.0, 40.0);
            Event event2 = new Event("name of e2", "locstion 2", 20, 9, 2023, 12, 45, 25.0, 32.0);
            Gson gson = new Gson();
            String eventsJson = gson.toJson(Arrays.asList(event1, event2));
            intent.putExtra(EVENTS_EXTRA, eventsJson);
            startActivity(intent);
        });

        boolean hasPermissions = true;
        for(String permission: PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                hasPermissions = false;
            }
        }
        if (!hasPermissions) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_LOCATION_ACCESS);
            return;
        }
        if (((EventApplication) getApplicationContext()).getLocationManager() == null) {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50,
                    new LocationListenerImpl());

            EventApplication application = (EventApplication) getApplicationContext();
            application.setLocationManager(locationManager);
        }
    }

    private View.OnClickListener startNewActivity(Class<? extends AppCompatActivity> newActivity) {
        return view -> {
            Intent intent = new Intent(MainActivity.this, newActivity);
            startActivity(intent);
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_ACCESS) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You have to grant location and internet access to this application.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permissions to access location and internet connection granted",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}