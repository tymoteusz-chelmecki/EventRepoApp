package com.example.event_repo_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowEventsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter recyclerViewAdapter;
    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapter = new EventRecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getAllEvents()
                .observe(this, events -> recyclerViewAdapter.setEvents(events));
    }
}