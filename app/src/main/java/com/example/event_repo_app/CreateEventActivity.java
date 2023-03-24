package com.example.event_repo_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import database.Event;

public class CreateEventActivity extends AppCompatActivity {
    private Button createButton;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        createButton = findViewById(R.id.create_create_button);
        createButton.setOnClickListener(view -> submitNewEvent());
    }

    private void submitNewEvent() {
        String eventName = getInputString(R.id.input_text_name);
        String location = getInputString(R.id.input_text_location);

        String date = "20-02-2023";
        String startHour = "14:00";
        double latitude = 23.05;
        double longitude = 108.40;

        if (eventName.trim().isEmpty() || location.trim().isEmpty()) {
            Toast.makeText(CreateEventActivity.this,
                    "You have to specify event name and location", Toast.LENGTH_LONG).show();
        } else {
            Event event = new Event(eventName, location, date, startHour, latitude, longitude);
            eventViewModel.insert(event);

            Toast.makeText(CreateEventActivity.this, "Created a new event", Toast.LENGTH_LONG).show();
        }
    }

    private String getInputString(int viewId) {
        return ((TextView) findViewById(viewId)).getText().toString();
    }
}