package com.example.event_repo_app;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import static com.example.event_repo_app.EventApplication.REQUEST_LOCATION_ACCESS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createButton = findViewById(R.id.button_create);
        createButton.setOnClickListener(startNewActivity(CreateEventActivity.class));
        Button showButton = findViewById(R.id.button_show);
        showButton.setOnClickListener(startNewActivity(ShowEventsActivity.class));
        Button shareButton = findViewById(R.id.button_share);
        shareButton.setOnClickListener(startNewActivity(ShareEventsActivity.class));
        Button showLocationButton = findViewById(R.id.button_show_location);
        showLocationButton.setOnClickListener(startNewActivity(ShowLocationActivity.class));

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_ACCESS);
            return;
        }

        EventApplication application = (EventApplication) getApplicationContext();
        application.setLocationManager(locationManager);
    }

    private View.OnClickListener startNewActivity(Class<? extends AppCompatActivity> newActivity) {
        return view -> {
            Intent intent = new Intent(MainActivity.this, newActivity);
            startActivity(intent);
        };
    }
}