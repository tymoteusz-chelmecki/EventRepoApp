package com.example.event_repo_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BrowseEventsActivity extends AppCompatActivity {

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
        Button showButton = findViewById(R.id.button_show_all);
        showButton.setOnClickListener(startNewActivity(ShowEventsActivity.class));
    }

    private View.OnClickListener startNewActivity(Class<? extends AppCompatActivity> newActivity) {
        return view -> {
            Intent intent = new Intent(BrowseEventsActivity.this, newActivity);
            startActivity(intent);
        };
    }
}