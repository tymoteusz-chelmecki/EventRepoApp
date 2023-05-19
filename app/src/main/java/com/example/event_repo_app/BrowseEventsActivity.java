package com.example.event_repo_app;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.event_repo_app.EventApplication.REQUEST_LOCATION_ACCESS;
import static java.lang.String.format;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

public class BrowseEventsActivity extends AppCompatActivity {
    private EditText dateEditText;
    private LocationManager locationManager;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);

        fetchEventsFromServer();

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_ACCESS);
            return;
        }
        locationManager = ((EventApplication) getApplicationContext()).getLocationManager();
        LocationListener listener = new BrowseEventsActivity.LocationListenerImpl();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, listener);


        Button browseByNameButton = findViewById(R.id.button_browse_name);
        browseByNameButton.setOnClickListener(view -> {
                String name = ((TextView) findViewById(R.id.input_text_browse_name)).getText().toString();
                if (name.trim().isEmpty()) {
                    Toast.makeText(BrowseEventsActivity.this, "You have to provide event name",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(BrowseEventsActivity.this, ShowEventsByNameActivity.class);
                intent.putExtra(Constants.BROWSE_NAME, name);
                startActivity(intent);
        });

        dateEditText = findViewById(R.id.input_text_browse_date);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(view -> selectDate());

        Button browseByDateButton = findViewById(R.id.button_browse_date);
        browseByDateButton.setOnClickListener(view -> {
            String date = dateEditText.getText().toString();
            Intent intent = new Intent(BrowseEventsActivity.this, ShowEventsByDateActivity.class);
            intent.putExtra(Constants.BROWSE_DATE, date);
            startActivity(intent);
        });

        TextView areaEditText = findViewById(R.id.input_text_browse_area);

        Button browseByAreaButton = findViewById(R.id.button_browse_area);
        browseByAreaButton.setOnClickListener(view -> {
            int distance;
            try {
                distance = Integer.parseInt(areaEditText.getText().toString());
            } catch (Exception e) {
                Toast.makeText(BrowseEventsActivity.this,
                                "You have to input a number", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(BrowseEventsActivity.this,
                    "Your location is: (" + latitude + ", " + longitude + ")", Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(BrowseEventsActivity.this, ShowEventsByAreaActivity.class);
            intent.putExtra(Constants.BROWSE_AREA, distance);
            intent.putExtra(Constants.LATITUDE, latitude);
            intent.putExtra(Constants.LONGITUDE, longitude);
            startActivity(intent);
        });

        Button showButton = findViewById(R.id.button_show_all);
        showButton.setOnClickListener(startNewActivity(ShowEventsActivity.class));
    }

    private void fetchEventsFromServer() {
        EventViewModel eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        ClientEventFetcher fetcher = new ClientEventFetcher(eventViewModel);
        Thread thread = new Thread(fetcher);
        thread.start();
    }

    private View.OnClickListener startNewActivity(Class<? extends AppCompatActivity> newActivity) {
        return view -> {
            Intent intent = new Intent(BrowseEventsActivity.this, newActivity);
            startActivity(intent);
        };
    }

    private void selectDate() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog pickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> dateEditText.setText(
                        format(Locale.getDefault(), "%d-%d-%d",
                                selectedDay, selectedMonth + 1, selectedYear))
                , year, month, day);
        pickerDialog.show();
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
            if (ActivityCompat.checkSelfPermission(BrowseEventsActivity.this, ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(BrowseEventsActivity.this, ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }
}