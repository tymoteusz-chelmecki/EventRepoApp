package com.example.event_repo_app;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import database.Event;

public class ClientEventFetcher implements Runnable{
    private static final String HOST = "10.0.2.2";
    private static final int PORT = 8080;
    private static final String GET = "GET";
    private static final String SEPARATOR = "#";

    private final EventViewModel eventViewModel;

    public ClientEventFetcher(EventViewModel eventViewModel) {
        this.eventViewModel = eventViewModel;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(HOST, PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(GET);
            System.out.println("Sent GET request");
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String allEventsJson = (String) inputStream.readObject();
            List<String> eventJsons = Arrays.asList(allEventsJson.split(SEPARATOR));
            Gson gson = new Gson();
            List<Event> newEvents = eventJsons.stream()
                    .map(json -> gson.fromJson(json, Event.class))
                    .collect(Collectors.toList());

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> eventViewModel.getAllEvents().observeForever(events -> {
                Set<Event> localEventsSet = new HashSet<>(events);
                for (Event newEvent : newEvents) {
                    if (!localEventsSet.contains(newEvent)) {
                        eventViewModel.insert(newEvent);
                    }
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
