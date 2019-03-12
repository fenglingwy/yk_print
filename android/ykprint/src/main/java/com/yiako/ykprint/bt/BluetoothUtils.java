package com.yiako.ykprint.bt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.dascom.print.ESCPOS;

import java.io.IOException;
import java.util.UUID;

public class BluetoothUtils {
    public static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private Context context;
    private BluetoothUtils.DiscoveryListener listener;

    public static  BluetoothSocket bluetoothSocket;
    public static  ESCPOS escpos;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //BluetoothDevice
            if (BluetoothDevice.ACTION_FOUND.equals(action) && BluetoothUtils.this.listener != null) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothUtils.this.listener.onDiscovery(device);
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action) && BluetoothUtils.this.listener != null) {
                BluetoothUtils.this.listener.onBtBondStatusChange(intent);
            } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action) && BluetoothUtils.this.listener != null) {
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action) && BluetoothUtils.this.listener != null) {
                Log.d("log","ACTION_ACL_CONNECTED");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action) && BluetoothUtils.this.listener != null) {
                Log.d("log","ACTION_ACL_DISCONNECT_REQUESTED");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action) && BluetoothUtils.this.listener != null) {
                Log.d("log","ACTION_ACL_DISCONNECTED");
                //BluetoothAdapter
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) && BluetoothUtils.this.listener != null) {
                BluetoothUtils.this.listener.onFinish();
                BluetoothUtils.this.context.unregisterReceiver(this);
//                BluetoothUtils.this.listener = null;
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action) && BluetoothUtils.this.listener != null) {
                BluetoothUtils.this.listener.onStart();
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action) && BluetoothUtils.this.listener != null) {
                BluetoothUtils.this.listener.onBtStatusChanged(intent);
            }

        }
    };

    public BluetoothUtils(Context context) {
        this.context = context;
    }

    public boolean openBluetooth() {
        return !this.adapter.isEnabled() && this.adapter.enable();
    }

    public void openBluetooth(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), requestCode);
    }

    public void openBluetooth(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), requestCode);
    }

    public void openBluetooth(android.app.Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), requestCode);
    }

    public boolean isEnable() {
        return this.adapter.isEnabled();
    }

    public boolean closeBluetooth() {
        return this.adapter.disable();
    }

    public void startDiscovery(BluetoothUtils.DiscoveryListener listener) {
        cancelDiscovery();
        if (!this.adapter.isDiscovering()) {
            this.listener = listener;
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            this.context.registerReceiver(this.receiver, filter);
            this.adapter.startDiscovery();
        }

    }

    public void cancelDiscovery() {
        this.adapter.cancelDiscovery();
    }

    public BluetoothSocket connectDevice(BluetoothDevice device) {
        this.adapter.cancelDiscovery();

        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            return socket;
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public BluetoothSocket connectRemoteDevice(String address) {
        this.adapter.cancelDiscovery();
        BluetoothDevice device = this.adapter.getRemoteDevice(address);

        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            return socket;
        } catch (IOException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public interface DiscoveryListener {
        //开始搜索
        void onStart();

        //发现新设备
        void onDiscovery(BluetoothDevice var1);

        //搜索完成
        void onFinish();

        //蓝牙状态该变
        void onBtStatusChanged(Intent intent);

        //绑定状态改变
        void onBtBondStatusChange(Intent intent);

        //蓝牙配对请求
        void onPairingRequest(Intent intent);
    }
}
