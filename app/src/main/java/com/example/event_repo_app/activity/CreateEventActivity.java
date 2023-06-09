package com.example.event_repo_app.activity;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.event_repo_app.ClientEventSender;
import com.example.event_repo_app.EventApplication;
import com.example.event_repo_app.EventViewModel;
import com.example.event_repo_app.GeoUtils;
import com.example.event_repo_app.R;

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

    @SuppressLint("MissingPermission")
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

        if (((EventApplication) getApplicationContext()).getLocationManager() == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    new LocationListenerImpl());
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                    new LocationListenerImpl());

            EventApplication application = (EventApplication) getApplicationContext();
            application.setLocationManager(locationManager);
        } else {
            locationManager = ((EventApplication) getApplicationContext()).getLocationManager();
        }
        LocationListener listener = new CreateEventActivity.LocationListenerImpl();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);

        Location lastLocation = GeoUtils.getLastLocation(locationManager);
        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
        }

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
        String dateString = getInputString(R.id.input_text_date);
        if (dateString.trim().isEmpty()) {
            Toast.makeText(CreateEventActivity.this,
                    "You have to specify date", Toast.LENGTH_LONG).show();
            return;
        }
        List<Integer> selectedDate = Arrays.stream(dateString
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

            sendToServer(event);

            Toast.makeText(CreateEventActivity.this, "Created a new event", Toast.LENGTH_LONG).show();
        }
    }

    private void sendToServer(Event event) {
        try {
            ClientEventSender sender = new ClientEventSender(event);
            Thread thread = new Thread(sender);
            thread.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String getInputString(int viewId) {
        return ((TextView) findViewById(viewId)).getText().toString();
    }

    private void fillCurrentLocation() {
        latitudeEditText.setText(String.valueOf(latitude));
        longitudeEditText.setText(String.valueOf(longitude));
    }

    private class LocationListenerImpl implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    }
}