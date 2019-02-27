package com.yiako.bluetoothprinter.bt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public  class BluetoothActivity extends Activity implements BtInterface {


    /**
     * blue tooth broadcast receiver
     */
    protected BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                btStartDiscovery(intent);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                btFinishDiscovery(intent);
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                btStatusChanged(intent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                btFoundDevice(intent);
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                btBondStatusChange(intent);
            } else if ("android.bluetooth.device.action.PAIRING_REQUEST".equals(action)) {
                btPairingRequest(intent);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        BtUtil.registerBluetoothReceiver(mBtReceiver, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BtUtil.unregisterBluetoothReceiver(mBtReceiver, this);
    }

    @Override
    public void btStartDiscovery(Intent intent) {

    }

    @Override
    public void btFinishDiscovery(Intent intent) {

    }

    @Override
    public void btStatusChanged(Intent intent) {

    }

    @Override
    public void btFoundDevice(Intent intent) {

    }

    @Override
    public void btBondStatusChange(Intent intent) {

    }

    @Override
    public void btPairingRequest(Intent intent) {

    }


    protected String getPrinterName() {
        String dName = PrintUtil.getDefaultBluetoothDeviceName(this);
        return getPrinterName(dName);
    }

    protected String getPrinterName(String dName) {
        if (TextUtils.isEmpty(dName)) {
            dName = "未知设备";
        }
        return dName;
    }

}
