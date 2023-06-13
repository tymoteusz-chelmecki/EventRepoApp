package com.example.event_repo_app.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.Manifest.permission.INTERNET;
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

import com.example.event_repo_app.EmptyLocationListener;
import com.example.event_repo_app.EventApplication;
import com.example.event_repo_app.R;

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    new EmptyLocationListener());

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