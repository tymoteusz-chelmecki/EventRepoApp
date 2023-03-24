package com.example.event_repo_app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import database.Event;
import database.Repository;

public class EventViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Event>> events;

    public EventViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
        this.events = repository.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() {
        return events;
    }

    public void insert(Event event) {
        repository.insert(event);
    }

    public void delete(Event event) {
        repository.delete(event);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }
}
