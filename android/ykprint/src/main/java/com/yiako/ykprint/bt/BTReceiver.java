package com.yiako.ykprint.bt;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BTReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action) ) {
            Log.d("log","BTReceiver-------------ACTION_ACL_DISCONNECTED");
            BluetoothUtils.bluetoothSocket = null;
            BluetoothUtils.escpos = null;
            //BluetoothAdapter
        }
    }
}
