package com.example.event_repo_app.bluetooth;

import static com.example.event_repo_app.Constants.FINISH_MESSAGE;

import android.bluetooth.BluetoothSocket;

import com.example.event_repo_app.EventViewModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import database.Event;

public class Sender extends Thread {
    private final BluetoothSocket socket;
    private final OutputStream outputStream;
    private byte[] bytes;
    private final EventViewModel eventViewModel;

    public Sender(BluetoothSocket socket, EventViewModel eventViewModel) {
        this.eventViewModel = eventViewModel;
        this.socket = socket;
        OutputStream tmp = null;
        try {
            tmp = socket.getOutputStream();
        } catch (IOException e) {
        }
        outputStream = tmp;
    }

    @Override
    public void run() {
        eventViewModel.getAllEvents().observeForever(events -> {
            try {
                for (Event event : events) {
                    String json = new Gson().toJson(event);
                    bytes = json.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(bytes);
                }
                outputStream.write(FINISH_MESSAGE.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
            }
        });
    }
}
