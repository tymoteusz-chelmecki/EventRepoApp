package com.example.event_repo_app;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.event_repo_app.EventApplication.REQUEST_LOCATION_ACCESS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

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
        showLocationButton.setOnClickListener(startNewActivity(ShowLocationActivity.class));

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_ACCESS);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50,
                new LocationListenerImpl());

        EventApplication application = (EventApplication) getApplicationContext();
        application.setLocationManager(locationManager);
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
                Toast.makeText(this, "You have to grant location access to this application.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permissions to access location granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class LocationListenerImpl implements LocationListener {

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
        }
    }
}