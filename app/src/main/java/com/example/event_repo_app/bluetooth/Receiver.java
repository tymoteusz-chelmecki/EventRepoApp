package com.example.event_repo_app.bluetooth;

import static com.example.event_repo_app.Constants.FINISH_MESSAGE;

import android.bluetooth.BluetoothSocket;

import com.example.event_repo_app.EventViewModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import database.Event;

public class Receiver extends Thread {
    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final byte[] bytes = new byte[1024];
    private final EventViewModel eventViewModel;

    public Receiver(BluetoothSocket socket, EventViewModel eventViewModel) {
        this.socket = socket;
        this.eventViewModel = eventViewModel;
        InputStream tmp = null;
        try {
            tmp = socket.getInputStream();
        } catch (IOException e) {
        }
        inputStream = tmp;
    }

    @Override
    public void run() {
        while (true) {
            try {
                inputStream.read(bytes);
                String message = new String(bytes, StandardCharsets.UTF_8);
                if (message.equals(FINISH_MESSAGE)) {
                    break;
                }
                Event event = new Gson().fromJson(message, Event.class);
                eventViewModel.insert(event);
            } catch (IOException e) {
                break;
            }
        }
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            //Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
