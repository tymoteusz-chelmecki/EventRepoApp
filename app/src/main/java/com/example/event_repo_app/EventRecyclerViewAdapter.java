package com.example.event_repo_app;

import static com.example.event_repo_app.Constants.EVENTS_EXTRA;
import static com.example.event_repo_app.Constants.MAIN_EVENT_EXTRA;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_repo_app.activity.SingleEventCenteredMapActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import database.Event;

public class EventRecyclerViewAdapter
        extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Event> events = new ArrayList<>();
    private final Activity activity;

    public EventRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.name.setText(event.getName());
        holder.location.setText(event.getLocation());
        holder.date.setText(String.format(Locale.getDefault(),
                "%d-%d-%d", event.getDay(), event.getMonth(), event.getYear()));
        holder.startHour.setText(String.format(Locale.getDefault(), "%d:%01d",
                event.getHour(), event.getMinute()));
        holder.showOnMap.setOnClickListener(showOnMap(event));
    }

    private View.OnClickListener showOnMap(Event event) {
        return view -> {
            Intent intent = new Intent(activity, SingleEventCenteredMapActivity.class);
            Gson gson = new Gson();
            String eventsJson = gson.toJson(events);
            String mainEventJson = gson.toJson(event);
            intent.putExtra(EVENTS_EXTRA, eventsJson);
            intent.putExtra(MAIN_EVENT_EXTRA, mainEventJson);
            activity.startActivity(intent);
        };
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = new ArrayList<>(events);
        notifyItemRangeChanged(0, events.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView location;
        private final TextView date;
        private final TextView startHour;
        private final Button showOnMap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            location = itemView.findViewById(R.id.item_location);
            date = itemView.findViewById(R.id.item_date);
            startHour = itemView.findViewById(R.id.item_start_hour);
            showOnMap = itemView.findViewById(R.id.button_show_on_map);
        }
    }
}
