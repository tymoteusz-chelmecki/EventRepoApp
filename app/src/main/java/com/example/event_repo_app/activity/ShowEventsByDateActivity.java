package com.example.event_repo_app.activity;

import static com.example.event_repo_app.Constants.EVENTS_EXTRA;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_repo_app.Constants;
import com.example.event_repo_app.EventRecyclerViewAdapter;
import com.example.event_repo_app.EventViewModel;
import com.example.event_repo_app.R;
import com.google.gson.Gson;

public class ShowEventsByDateActivity extends AppCompatActivity {
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

        String date = getIntent().getExtras().getString(Constants.BROWSE_DATE);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.setQueryDate(date);
        eventViewModel.getEventsByDate().observe(this, events -> {
            recyclerViewAdapter.setEvents(events);
            showMapButton.setOnClickListener(view -> {
                Intent intent = new Intent(ShowEventsByDateActivity.this, MapActivity.class);
                Gson gson = new Gson();
                String eventsJson = gson.toJson(events);
                intent.putExtra(EVENTS_EXTRA, eventsJson);
                startActivity(intent);
            });
        });
    }
}