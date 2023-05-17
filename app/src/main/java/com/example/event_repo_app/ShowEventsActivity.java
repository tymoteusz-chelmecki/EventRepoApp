package com.example.event_repo_app;

import static com.example.event_repo_app.Constants.EVENTS_EXTRA;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

public class ShowEventsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter recyclerViewAdapter;
    private EventViewModel eventViewModel;
    private Button showMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        showMapButton =  findViewById(R.id.button_show_map);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapter = new EventRecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getAllEvents().observe(this, events -> {
            recyclerViewAdapter.setEvents(events);
            showMapButton.setOnClickListener(view -> {
                Intent intent = new Intent(ShowEventsActivity.this, MapActivity.class);
                Gson gson = new Gson();
                String eventsJson = gson.toJson(events);
                intent.putExtra(EVENTS_EXTRA, eventsJson);
                startActivity(intent);
            });
        });
    }
}