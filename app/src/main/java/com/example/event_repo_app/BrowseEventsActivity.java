package com.example.event_repo_app;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

public class BrowseEventsActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private Button browseByNameButton;
    private Button browseByDateButton;
    private Button browseByAreaButton;
    private Button showAllButton;
    private TextView nameEditText;
    private EditText dateEditText;
    private TextView areaEditText;

    private double latitude;
    private double longitude;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);

        fetchEventsFromServer();

        locationManager = ((EventApplication) getApplicationContext()).getLocationManager();
        LocationListener listener = new BrowseEventsActivity.LocationListenerImpl();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, listener);

        nameEditText = findViewById(R.id.input_text_browse_name);
        browseByNameButton = findViewById(R.id.button_browse_name);
        browseByNameButton.setOnClickListener(getBrowseByNameListener());

        dateEditText = findViewById(R.id.input_text_browse_date);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(view -> selectDate());

        browseByDateButton = findViewById(R.id.button_browse_date);
        browseByDateButton.setOnClickListener(getBrowseByDateListener());

        areaEditText = findViewById(R.id.input_text_browse_area);
        browseByAreaButton = findViewById(R.id.button_browse_area);
        browseByAreaButton.setOnClickListener(getBrowseByAreaListener());

        showAllButton = findViewById(R.id.button_show_all);
        showAllButton.setOnClickListener(startBrowseAllEventsActivity());
    }

    private void fetchEventsFromServer() {
        EventViewModel eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        ClientEventFetcher fetcher = new ClientEventFetcher(eventViewModel);
        Thread thread = new Thread(fetcher);
        thread.start();
    }

    private View.OnClickListener getBrowseByNameListener() {
        return view -> {
            String name = nameEditText.getText().toString();
            if (name.trim().isEmpty()) {
                Toast.makeText(BrowseEventsActivity.this, "You have to provide event name",
                        Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(BrowseEventsActivity.this, ShowEventsByNameActivity.class);
            intent.putExtra(Constants.BROWSE_NAME, name);
            startActivity(intent);
        };
    }

    private View.OnClickListener getBrowseByDateListener() {
        return view -> {
            String date = dateEditText.getText().toString();
            Intent intent = new Intent(BrowseEventsActivity.this, ShowEventsByDateActivity.class);
            intent.putExtra(Constants.BROWSE_DATE, date);
            startActivity(intent);
        };
    }

    private View.OnClickListener getBrowseByAreaListener() {
        return view -> {
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
        };
    }

    private View.OnClickListener startBrowseAllEventsActivity() {
        return view -> {
            Intent intent = new Intent(BrowseEventsActivity.this, ShowEventsActivity.class);
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

    private class LocationListenerImpl implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }
}