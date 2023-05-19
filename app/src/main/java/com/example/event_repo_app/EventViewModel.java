package com.example.event_repo_app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import database.Event;
import database.Repository;

public class EventViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Event>> events;
    private final LiveData<List<Event>> eventsByName;
    private final LiveData<List<Event>> eventsByDate;
    private final MutableLiveData<String> mutableEventsByName = new MutableLiveData<>();
    private final MutableLiveData<String> mutableEventsByDate = new MutableLiveData<>();


    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        events = Objects.requireNonNull(repository.getAllEvents(), "Events LiveData cannot be null");
        eventsByName = Transformations.switchMap(mutableEventsByName, repository::getByName);
        eventsByDate = Transformations.switchMap(mutableEventsByDate, repository::getByDate);
    }

    public LiveData<List<Event>> getAllEvents() {
        return events;
    }

    public void insert(Event event) {
        repository.insert(event);
    }

    public LiveData<List<Event>> getEventsByName() {
        return eventsByName;
    }

    public LiveData<List<Event>> getEventsByDate() {
        return eventsByDate;
    }

    public List<Event> getEventsByArea(List<Event> events, int distance, double lat, double lon) {
        assert events != null;
        return events.stream()
                .filter(e ->
                        GeoUtils.getDistance(e.getLatitude(), e.getLongitude(), lat, lon) < distance)
                .collect(Collectors.toList());
    }

    public void delete(Event event) {
        repository.delete(event);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void setQueryName(String name) {
        mutableEventsByName.setValue(name);
    }

    public void setQueryDate(String date) {
        mutableEventsByDate.setValue(date);
    }
}
