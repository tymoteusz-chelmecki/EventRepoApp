package com.example.event_repo_app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import database.Event;
import database.Repository;

public class EventViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Event>> events;
    private final LiveData<List<Event>> eventsByName;
    private final MutableLiveData<String> mutableEventsByName = new MutableLiveData<>();

    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        events = repository.getAllEvents();
        eventsByName = Transformations.switchMap(mutableEventsByName, repository::getByName);
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

    public void delete(Event event) {
        repository.delete(event);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void setQueryName(String name) {
        mutableEventsByName.setValue(name);
    }
}
