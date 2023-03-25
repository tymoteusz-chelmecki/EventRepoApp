package com.example.event_repo_app;

import static java.lang.String.format;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BrowseEventsActivity extends AppCompatActivity {
    private EditText dateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_events);

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

        Button showButton = findViewById(R.id.button_show_all);
        showButton.setOnClickListener(startNewActivity(ShowEventsActivity.class));
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
}