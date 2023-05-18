package com.example.event_repo_app;

import com.google.gson.Gson;

import java.io.ObjectOutputStream;
import java.net.Socket;

import database.Event;

public class ClientEventSender implements Runnable{
    private static final String HOST = "10.0.2.2";
    private static final int PORT = 8080;
    private static final String PUT = "PUT";

    private final Event event;

    public ClientEventSender(Event event) {
        this.event = event;
    }

    @Override
    public void run() {
        try  {
            Socket socket = new Socket(HOST, PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            String putEventMessage = getMessage();
            outputStream.writeObject(putEventMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMessage() {
        return PUT + new Gson().toJson(event);
    }
}
