package com.example.event_repo_app;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_repo_app.bluetooth.SendThread;

import java.util.ArrayList;

public class DeviceRecyclerViewAdapter
        extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder> {
    private final ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private final EventViewModel eventViewModel;
    private final BluetoothAdapter bluetoothAdapter;

    public DeviceRecyclerViewAdapter(EventViewModel eventViewModel, BluetoothAdapter bluetoothAdapter) {
        this.eventViewModel = eventViewModel;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BluetoothDevice device = devices.get(position);
        holder.deviceName.setText(device.getName());
        holder.shareAll.setOnClickListener(sendAllEvents(device));
    }

    private View.OnClickListener sendAllEvents(BluetoothDevice device) {
        return view -> {
            SendThread sendThread = new SendThread(device, bluetoothAdapter, eventViewModel);
            sendThread.start();
        };
    }


    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void addDevice(BluetoothDevice device) {
        devices.add(device);
        notifyItemRangeChanged(0, devices.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView deviceName;
        private final Button shareAll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.item_device_name);
            shareAll = itemView.findViewById(R.id.button_share_bluetooth);
        }
    }
}
