package com.example.event_repo_app;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.event_repo_app.EventApplication.REQUEST_LOCATION_ACCESS;
import static java.lang.String.format;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import database.Event;

public class CreateEventActivity extends AppCompatActivity {
    private EventViewModel eventViewModel;
    private EditText dateEditText;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private LocationManager locationManager;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        dateEditText = findViewById(R.id.input_text_date);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(view -> selectDate());

        hourPicker = findViewById(R.id.picker_hour);
        minutePicker = findViewById(R.id.picker_minute);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        latitudeEditText = findViewById(R.id.input_text_latitude);
        longitudeEditText = findViewById(R.id.input_text_longitude);
        Button getCurrentLocationButton = findViewById(R.id.create_get_location_button);
        getCurrentLocationButton.setOnClickListener(view -> fillCurrentLocation());

        locationManager = ((EventApplication) getApplicationContext()).getLocationManager();
        LocationListener listener = new CreateEventActivity.LocationListenerImpl();
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_ACCESS);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, listener);

        Button createButton = findViewById(R.id.create_create_button);
        createButton.setOnClickListener(view -> submitNewEvent());
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

    private void submitNewEvent() {
        String eventName = getInputString(R.id.input_text_name);
        String location = getInputString(R.id.input_text_location);
        List<Integer> selectedDate = Arrays.stream(getInputString(R.id.input_text_date)
                .split("-"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        int day = selectedDate.get(0);
        int month = selectedDate.get(1);
        int year = selectedDate.get(2);

        int hour = hourPicker.getValue();
        int minute = minutePicker.getValue();

        if (eventName.trim().isEmpty() || location.trim().isEmpty()) {
            Toast.makeText(CreateEventActivity.this,
                    "You have to specify event name and location", Toast.LENGTH_LONG).show();
        } else {
            Event event = new Event(eventName, location, day, month, year, hour, minute,
                    latitude, longitude);
            eventViewModel.insert(event);

            Toast.makeText(CreateEventActivity.this, "Created a new event", Toast.LENGTH_LONG).show();
        }
    }

    private String getInputString(int viewId) {
        return ((TextView) findViewById(viewId)).getText().toString();
    }

    private void fillCurrentLocation() {
        latitudeEditText.setText(String.valueOf(latitude));
        longitudeEditText.setText(String.valueOf(longitude));
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
            if (ActivityCompat.checkSelfPermission(CreateEventActivity.this, ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(CreateEventActivity.this, ACCESS_COARSE_LOCATION)
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