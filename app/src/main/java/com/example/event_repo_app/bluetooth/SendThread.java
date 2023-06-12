package com.example.event_repo_app.bluetooth;

import static com.example.event_repo_app.Constants.UUID_STRING;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.event_repo_app.EventViewModel;

import java.io.IOException;
import java.util.UUID;

public class SendThread extends Thread {
    private final BluetoothSocket socket;
    private final BluetoothAdapter bluetoothAdapter;
    private final EventViewModel eventViewModel;

    @SuppressLint("MissingPermission")
    public SendThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter,
                      EventViewModel eventViewModel) {
        this.eventViewModel = eventViewModel;
        this.bluetoothAdapter = bluetoothAdapter;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STRING));
        } catch (IOException e) {
        }
        socket = tmp;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        bluetoothAdapter.cancelDiscovery();

        try {
            socket.connect();
        } catch (IOException connectException) {
            try {
                socket.close();
            } catch (IOException closeException) {
            }
            return;
        }
        Sender sender = new Sender(socket, eventViewModel);
        sender.start();
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
