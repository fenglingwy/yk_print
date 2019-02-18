package com.yiako.bluetoothprinter;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.printer.sdk.PrinterInstance;
import com.yiako.bluetoothprinter.bt.BluetoothSearchActivity;
import com.yiako.bluetoothprinter.bt.PrintUtil;
import com.yiako.bluetoothprinter.entity.PrintData1;
import com.yiako.bluetoothprinter.entity.PrintData2;
import com.yiako.bluetoothprinter.template.PrintTemplate1;
import com.yiako.bluetoothprinter.template.PrintTemplate2;

import java.util.HashMap;
import java.util.Map;

public class PrintModule extends ReactContextBaseJavaModule {
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    public PrintModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "PrintModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    PrinterInstance mPritner;

    @ReactMethod
    public void print(final int templateType, final String data) {
        if(TextUtils.isEmpty(data)){
            ToastUtils.showShort("打印数据不能为空!");
            return;
        }


        if (mPritner == null) mPritner = PrinterInstance.mPrinter;

        if (mPritner == null) {
            ToastUtils.showShort("请连接打印机！");
            Intent intent = new Intent(getReactApplicationContext(), BluetoothSearchActivity.class);
            getCurrentActivity().startActivity(intent);
            return;
        }


        if (mPritner != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        switch (templateType){
                            case 1:

                                new PrintTemplate1().doPrint(mPritner,new Gson().fromJson(data,PrintData1.class));
                                break;
                            case 2:
                                new PrintTemplate2().doPrint(mPritner,   new Gson().fromJson(data,PrintData2.class));
                                break;
                        }
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("打印数据解析异常!");
                    }
                }
            }).start();
        }

    }

    @ReactMethod
    public void connectBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (PrintUtil.isBondPrinter(getCurrentActivity(), mBluetoothAdapter)) {

        } else {
            Intent intent = new Intent(getReactApplicationContext(), BluetoothSearchActivity.class);
            getCurrentActivity().startActivity(intent);
        }
    }

//    private void connect2BlueToothdevice() {
//        mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(devicesAddress);
//        devicesName = mDevice.getName();
//        myPrinter = PrinterInstance.getPrinterInstance(mDevice, mHandler);
//        if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {// 未绑定
//            // IntentFilter boundFilter = new IntentFilter();
//            // boundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//            // mContext.registerReceiver(boundDeviceReceiver, boundFilter);
//            PairOrConnect(true);
//        } else {
//            PairOrConnect(false);
//        }
//    }
}
