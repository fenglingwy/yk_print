package com.yiako.ykprint;

import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dascom.print.ESCPOS;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.yiako.ykprint.bt.BluetoothSearchActivity;
import com.yiako.ykprint.bt.BluetoothUtils;
import com.yiako.ykprint.entity.PrintData1;
import com.yiako.ykprint.entity.PrintData2;
import com.yiako.ykprint.entity.PrintData3;
import com.yiako.ykprint.template.PrintTemplate1;
import com.yiako.ykprint.template.PrintTemplate2;
import com.yiako.ykprint.template.PrintTemplate3;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yiako.ykprint.bt.BluetoothSearchActivity.SP_DEVICE_ADDRESS;

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


    @ReactMethod
    public void print(final int templateType, final String data) {
        if (TextUtils.isEmpty(data)) {
            ToastUtils.showShort("打印数据不能为空!");
            return;
        }
        String devicesAddress = SPUtils.getInstance().getString(SP_DEVICE_ADDRESS);
        if (TextUtils.isEmpty(devicesAddress)) {
            ToastUtils.showShort("请选择打印机！");
            selectBt();
            return;
        }

        if (BluetoothUtils.bluetoothSocket == null) {
            BluetoothUtils.bluetoothSocket = new BluetoothUtils(getCurrentActivity()).connectRemoteDevice(devicesAddress);
        }

        if (BluetoothUtils.bluetoothSocket == null) {
            ToastUtils.showShort("连接失败,请检查打印机！");
            selectBt();
        } else {
//            ToastUtils.showShort("打印机连接成功！");
            if (BluetoothUtils.escpos == null) {
                BluetoothUtils.escpos = new ESCPOS(BluetoothUtils.bluetoothSocket);
            }

            new Thread(){
                @Override
                public void run() {
                    try {
                        switch (templateType) {
                            case 1:
                                Type type1 = new TypeToken<List<PrintData1>>() {
                                }.getType();
                                List<PrintData1> list1 = new Gson().fromJson(data, type1);
                                new PrintTemplate1(BluetoothUtils.escpos).doPrint(list1);
                                break;
                            case 2:
                                Type type2 = new TypeToken<List<PrintData2>>() {
                                }.getType();
                                List<PrintData2> list2 = new Gson().fromJson(data, type2);
                                new PrintTemplate2(BluetoothUtils.escpos).doPrint(list2);
                                break;
                            case 3:
                                new PrintTemplate3(BluetoothUtils.escpos).doPrint(new Gson().fromJson(data, PrintData3.class));
                                break;
                        }
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        ToastUtils.showShort("打印数据解析异常!");
                    }
                }
            }.start();


//                Type type2 = new TypeToken<List<PrintData2>>() {}.getType();
//                List<PrintData2> list2 = new Gson().fromJson(Data.data2, type2);
//                new PrintTemplate2(escpos).doPrint( list2);

//                Type type1 = new TypeToken<List<PrintData1>>() {
//                }.getType();
//                List<PrintData1> list1 = new Gson().fromJson(Data.data1, type1);
//                new PrintTemplate1(BluetoothUtils.escpos).doPrint(list1);

//                new PrintTemplate3(escpos).doPrint(new Gson().fromJson(Data.data3, PrintData3.class));
        }
    }

    @ReactMethod
    public void selectBt() {
        Intent intent = new Intent(getReactApplicationContext(), BluetoothSearchActivity.class);
        getCurrentActivity().startActivity(intent);
    }
}
