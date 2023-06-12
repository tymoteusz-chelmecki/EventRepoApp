package com.example.event_repo_app.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_repo_app.DeviceRecyclerViewAdapter;
import com.example.event_repo_app.EventViewModel;
import com.example.event_repo_app.R;
import com.example.event_repo_app.bluetooth.AcceptThread;

public class ShareEventsActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1234;
    private RecyclerView recyclerView;
    private DeviceRecyclerViewAdapter recyclerViewAdapter;
    private AcceptThread server;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_events);
        recyclerView = findViewById(R.id.device_recycler_view);
        EventViewModel eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            System.out.println("BlueTooth adapter is null");
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
        int requestCode = 1;
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, requestCode);

        recyclerViewAdapter = new DeviceRecyclerViewAdapter(eventViewModel, bluetoothAdapter);
        recyclerView.setAdapter(recyclerViewAdapter);

        server = new AcceptThread(bluetoothAdapter, eventViewModel);
        server.start();
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Bluetooth permissions not granted - you cannot share events",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Bluetooth permissions granted", Toast.LENGTH_LONG).show();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                recyclerViewAdapter.addDevice(device);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.cancel();
        unregisterReceiver(receiver);
    }
}