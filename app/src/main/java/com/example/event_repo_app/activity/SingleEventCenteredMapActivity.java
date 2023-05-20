package com.example.event_repo_app.activity;

import static com.example.event_repo_app.Constants.MAIN_EVENT_EXTRA;

import com.google.gson.Gson;

import org.osmdroid.util.BoundingBox;

import database.Event;

public class SingleEventCenteredMapActivity extends MapActivity {
    private Event mainEvent;

    @Override
    protected void getEventsFromIntent() {
        super.getEventsFromIntent();
        Gson gson = new Gson();
        mainEvent = gson.fromJson(getIntent().getStringExtra(MAIN_EVENT_EXTRA), Event.class);
    }

    @Override
    protected void setInitialZoomAndPosition() {
        map.addOnFirstLayoutListener((v, left, top, right, bottom) -> {
            double latitude = mainEvent.getLatitude();
            double longitude = mainEvent.getLongitude();
            BoundingBox box = new BoundingBox(latitude + 0.002, longitude + 0.002,
                    latitude - 0.002, longitude - 0.002);
            map.zoomToBoundingBox(box, false, 100);
            map.invalidate();
        });
    }
}
