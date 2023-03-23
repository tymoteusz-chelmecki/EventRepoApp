package com.example.event_repo_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Button createButton = (Button) findViewById(R.id.create_create_button);
        createButton.setOnClickListener(view -> submitNewEvent());
    }

    private void submitNewEvent() {
        String eventName = getInputString(R.id.input_text_name);
        String location = getInputString(R.id.input_text_location);
        String date = getInputString(R.id.input_text_date);
        String hour = getInputString(R.id.input_text_hour);
        if (eventName.isEmpty() || location.isEmpty() || date.isEmpty()) {
            Toast.makeText(CreateEventActivity.this,
                    "You have to specify event name, location and date", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CreateEventActivity.this, "Created new event", Toast.LENGTH_LONG).show();
        }
    }

    private String getInputString(int viewId) {
        return ((TextView) findViewById(viewId)).getText().toString();
    }
}