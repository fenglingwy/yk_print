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
import com.google.gson.reflect.TypeToken;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.exception.PrinterPortNullException;
import com.printer.sdk.exception.ReadException;
import com.printer.sdk.exception.WriteException;
import com.yiako.bluetoothprinter.bt.BluetoothSearchActivity;
import com.yiako.bluetoothprinter.bt.PrintUtil;
import com.yiako.bluetoothprinter.entity.PrintData1;
import com.yiako.bluetoothprinter.entity.PrintData2;
import com.yiako.bluetoothprinter.entity.PrintData3;
import com.yiako.bluetoothprinter.template.PrintTemplate1;
import com.yiako.bluetoothprinter.template.PrintTemplate2;
import com.yiako.bluetoothprinter.template.PrintTemplate3;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintModule extends ReactContextBaseJavaModule {

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
        if (TextUtils.isEmpty(data)) {
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

        int status = -4;
        try {
            status = mPritner.getPrinterStatusTSPL();
        } catch (WriteException | PrinterPortNullException | ReadException e) {
            e.printStackTrace();
        }

        //                0 状态正常;-1 缺纸;-2 开盖;-3 其他错误
        if(status==-1){
            ToastUtils.showShort("打印机缺纸");return;
        }else if(status==-2){
            ToastUtils.showShort("打印机开盖");return;
        } else if(status==-3){
            ToastUtils.showShort("打印机连接错误");return;
        }else if(status==-4){
            ToastUtils.showShort("打印机断开，请重新连接！");
            mPritner.closeConnection();
            Intent intent = new Intent(getReactApplicationContext(), BluetoothSearchActivity.class);
            getCurrentActivity().startActivity(intent);
            return;
        }


        if (mPritner != null) {
            new Thread(() -> {
                try {
                    switch (templateType) {
                        case 1:
                            Type type1 = new TypeToken<List<PrintData1>>() {}.getType();
                            List<PrintData1> list1 = new Gson().fromJson(data, type1);
                            new PrintTemplate1().doPrint(mPritner, list1);
                            break;
                        case 2:
                            Type type2 = new TypeToken<List<PrintData2>>() {}.getType();
                            List<PrintData2> list2 = new Gson().fromJson(data, type2);
                            new PrintTemplate2().doPrint(mPritner, list2);
                            break;
                        case 3:
                            new PrintTemplate3().doPrint(mPritner,new Gson().fromJson(data, PrintData3.class));
                            break;
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                    ToastUtils.showShort("打印数据解析异常!");
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
}
