package com.example.event_repo_app.bluetooth;

import static com.example.event_repo_app.Constants.UUID_STRING;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.example.event_repo_app.EventViewModel;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {
    private final BluetoothServerSocket serverSocket;
    private final EventViewModel eventViewModel;

    @SuppressLint("MissingPermission")
    public AcceptThread(BluetoothAdapter bluetoothAdapter, EventViewModel eventViewModel) {
        this.eventViewModel = eventViewModel;
        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("NAME",
                    UUID.fromString(UUID_STRING));
        } catch (IOException e) {
        }
        serverSocket = tmp;
    }

    @Override
    public void run() {
        BluetoothSocket socket;
        while (true) {
            try {
                socket = serverSocket.accept();
                if (socket != null) {
                    Receiver receiver = new Receiver(socket, eventViewModel);
                    receiver.start();
                    serverSocket.close();
                    break;
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            //Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
