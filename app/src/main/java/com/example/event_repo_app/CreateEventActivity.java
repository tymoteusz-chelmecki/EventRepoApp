package com.example.event_repo_app;

import static java.lang.String.format;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import database.Event;

public class CreateEventActivity extends AppCompatActivity {
    private Button createButton;
    private EventViewModel eventViewModel;
    private EditText dateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        dateEditText = findViewById(R.id.input_text_date);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(view -> selectDate());

        createButton = findViewById(R.id.create_create_button);
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

        String startHour = "14:00";
        double latitude = 23.05;
        double longitude = 108.40;

        if (eventName.trim().isEmpty() || location.trim().isEmpty()) {
            Toast.makeText(CreateEventActivity.this,
                    "You have to specify event name and location", Toast.LENGTH_LONG).show();
        } else {
            Event event = new Event(eventName, location, day, month, year, startHour,
                    latitude, longitude);
            eventViewModel.insert(event);

            Toast.makeText(CreateEventActivity.this, "Created a new event", Toast.LENGTH_LONG).show();
        }
    }

    private String getInputString(int viewId) {
        return ((TextView) findViewById(viewId)).getText().toString();
    }
}