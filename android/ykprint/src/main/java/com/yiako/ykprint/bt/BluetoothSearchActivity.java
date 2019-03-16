package com.yiako.ykprint.bt;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dascom.print.ESCPOS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yiako.ykprint.R;
import com.yiako.ykprint.entity.Data;
import com.yiako.ykprint.entity.PrintData1;
import com.yiako.ykprint.template.PrintTemplate1;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class BluetoothSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String SP_DEVICE_NAME = "SP_DEVICE_NAME";  //设备名
    public static final String SP_DEVICE_ADDRESS = "SP_DEVICE_ADDRESS"; //设备地址

    ListView mRv;
    TextView mTvSelected;
    Button mBtnSearch;
    private ProgressDialog progressDialog;
    private String devicesAddress;
    private String devicesName;

    BluetoothUtils bluetoothUtils;
    private SearchBleAdapter searchBleAdapter;

    private BtListener btListener = new BtListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_search);

        initView();

        initBluetooth();

        initData();
    }

    private void initData() {
        devicesName = SPUtils.getInstance().getString(SP_DEVICE_NAME);
        devicesAddress = SPUtils.getInstance().getString(SP_DEVICE_ADDRESS);
        if (!TextUtils.isEmpty(devicesName)) {
            mTvSelected.setText("已选择：" + devicesName);
        }
        bluetoothUtils.startDiscovery(btListener);
    }


    private void initBluetooth() {
        bluetoothUtils = new BluetoothUtils(this);
        bluetoothUtils.registerReceiver();

        bluetoothUtils.openBluetooth();

    }

    private void initView() {
        mRv = findViewById(R.id.rv);
        mTvSelected = findViewById(R.id.tv_selected);
        mBtnSearch = findViewById(R.id.btn_search);
        searchBleAdapter = new SearchBleAdapter(this, null);

        // 初始化进度条对话框
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mRv.setAdapter(searchBleAdapter);
        mRv.setOnItemClickListener(this);

        mBtnSearch.setOnClickListener(v -> {
            searchBleAdapter.getDevices().clear();
            searchBleAdapter.notifyDataSetChanged();
            bluetoothUtils.startDiscovery(btListener);
        });

        findViewById(R.id.print).setOnClickListener(v -> {
            if (TextUtils.isEmpty(devicesAddress)) {
                ToastUtils.showShort("请连接打印机！");
                return;
            }

            if (BluetoothUtils.bluetoothSocket == null) {
                BluetoothUtils.bluetoothSocket = bluetoothUtils.connectRemoteDevice(devicesAddress);
            }

            if (BluetoothUtils.bluetoothSocket == null) {
                ToastUtils.showShort("连接失败,请检查打印机！");
            } else {
                ToastUtils.showShort("打印机连接成功！");
                if (BluetoothUtils.escpos == null) {
                    BluetoothUtils.escpos = new ESCPOS(BluetoothUtils.bluetoothSocket);
                }

//                Type type2 = new TypeToken<List<PrintData2>>() {}.getType();
//                List<PrintData2> list2 = new Gson().fromJson(Data.data2, type2);
//                new PrintTemplate2(escpos).doPrint( list2);

                Type type1 = new TypeToken<List<PrintData1>>() {
                }.getType();
                List<PrintData1> list1 = new Gson().fromJson(Data.data1, type1);
                new PrintTemplate1(BluetoothUtils.escpos).doPrint(list1);

//                new PrintTemplate3(escpos).doPrint(new Gson().fromJson(Data.data3, PrintData3.class));


            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        progressDialog.show();

        if (null == searchBleAdapter) {
            return;
        }
        final BluetoothDevice device = searchBleAdapter.getItem(position);
        if (null == device) {
            return;
        }
        devicesAddress = device.getAddress();
        devicesName = device.getName();


        if (device.getBondState() == BluetoothDevice.BOND_NONE) {// 未绑定
            try {
                // 开始配对 这段代码打开输入配对密码的对话框
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                createBondMethod.invoke(device);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException e) {
                e.printStackTrace();
                ToastUtils.showShort("选择配对失败！");
                closeDilog();
            }
        } else {
            //本地缓存已选设备
            SPUtils.getInstance().put(SP_DEVICE_NAME, devicesName);
            SPUtils.getInstance().put(SP_DEVICE_ADDRESS, devicesAddress);
            mTvSelected.setText("已选择：" + devicesName);
            ToastUtils.showShort("选择配对成功！");

            try {
                if (BluetoothUtils.bluetoothSocket != null) {
                    BluetoothUtils.bluetoothSocket.close();
                }
                if (BluetoothUtils.escpos != null) {
                    BluetoothUtils.escpos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            closeDilog();
            finish();
        }

    }

    class BtListener implements BluetoothUtils.DiscoveryListener {

        @Override
        public void onStart() {
            mBtnSearch.setText("搜索中…");
        }

        @Override
        public void onDiscovery(BluetoothDevice device) {
            if (null != bluetoothUtils.adapter && device != null) {
                searchBleAdapter.addDevices(device);
                String dName = device.getName() == null ? "未知设备" : device.getName();
                Log.d("未知设备", dName);
            }
        }

        @Override
        public void onFinish() {
            mBtnSearch.setText("点击搜索");
        }

        @Override
        public void onBtStatusChanged(Intent intent) {

        }

        @Override
        public void onBtBondStatusChange(Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING://正在配对
                    Log.d("BlueToothTestActivity", "正在配对......");
                    break;
                case BluetoothDevice.BOND_BONDED://配对结束
                    Log.d("BlueToothTestActivity", "完成配对");
                    //本地缓存已选设备
                    SPUtils.getInstance().put(SP_DEVICE_NAME,devicesName );
                    SPUtils.getInstance().put(SP_DEVICE_ADDRESS, devicesAddress);
                    mTvSelected.setText("已选择：" + devicesName);
                    ToastUtils.showShort("选择配对成功！");
                    try {
                        if (BluetoothUtils.bluetoothSocket != null) {
                            BluetoothUtils.bluetoothSocket.close();
                        }
                        if (BluetoothUtils.escpos != null) {
                            BluetoothUtils.escpos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    closeDilog();
                    finish();
                    break;
                case BluetoothDevice.BOND_NONE://取消配对/未配对
                    Log.d("BlueToothTestActivity", "取消配对");
                    closeDilog();
                default:
                    break;
            }
        }

        @Override
        public void onPairingRequest(Intent intent) {

        }
    }

    private void closeDilog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void finish() {
        super.finish();
        bluetoothUtils.unRegisterReceiver();
    }
}
