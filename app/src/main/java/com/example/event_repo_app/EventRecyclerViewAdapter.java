package com.example.event_repo_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.Event;

public class EventRecyclerViewAdapter
        extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Event> events = new ArrayList<>();

    public EventRecyclerViewAdapter() {

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
        holder.date.setText(event.getDate());
        holder.startHour.setText(event.getStartHour());
        holder.latitude.setText(String.valueOf(event.getLatitude()));
        holder.longitude.setText(String.valueOf(event.getLongitude()));
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
        private final TextView latitude;
        private final TextView longitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            location = itemView.findViewById(R.id.item_location);
            date = itemView.findViewById(R.id.item_date);
            startHour = itemView.findViewById(R.id.item_start_hour);
            latitude = itemView.findViewById(R.id.item_latitude);
            longitude = itemView.findViewById(R.id.item_longitude);
        }
    }
}
