package com.yiako.bluetoothprinter.bt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.utils.XLog;
import com.yiako.bluetoothprinter.R;
import com.yiako.bluetoothprinter.template.PrintLabel80;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BluetoothSearchActivity extends BluetoothActivity implements AdapterView.OnItemClickListener {

    ListView mRv;
    TextView mBtnSearch;
    TextView mBtnStatus;
    private BluetoothAdapter mBluetoothAdapter;
    private SearchBleAdapter searchBleAdapter;
    private boolean isConnected;
    private ProgressDialog progressDialog;
    private String devicesAddress;
    private PrinterInstance myPrinter;
    private BluetoothDevice mDevice;
    private String devicesName;
    private IntentFilter bluDisconnectFilter;
    private boolean hasRegDisconnectReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_search);

        initView();

        //初始化蓝牙
        initBluetooth();
    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!BtUtil.isOpen(mBluetoothAdapter)) {
            mBluetoothAdapter.enable();
        } else {
            searchDeviceOrOpenBluetooth();
            if (!PrintUtil.isBondPrinter(this, mBluetoothAdapter)) {
                //未绑定蓝牙打印机器
                mBtnStatus.setText("未连接蓝牙打印机");
            } else {
                //已绑定蓝牙设备
                mBtnStatus.setText(getPrinterName() + "已连接");
            }
        }
    }

    private void initView() {
        mRv = findViewById(R.id.rv);
        mBtnSearch = findViewById(R.id.tv_title);
        mBtnStatus = findViewById(R.id.tv_summary);
        searchBleAdapter = new SearchBleAdapter(this, null);

        // 初始化进度条对话框
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mRv.setAdapter(searchBleAdapter);
        mRv.setOnItemClickListener(this);

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBleAdapter.getDevices().clear();
                searchBleAdapter.notifyDataSetChanged();
                searchDeviceOrOpenBluetooth();
            }
        });
        findViewById(R.id.print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PrinterInstance mPritner = PrinterInstance.mPrinter;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new PrintLabel80().doPrint(mPritner);
                    }
                }).start();
            }
        });

    }


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
    public void btStatusChanged(Intent intent) {

        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {//蓝牙被关闭时强制打开
            mBluetoothAdapter.enable();
        }
        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {//蓝牙打开时搜索蓝牙
            searchDeviceOrOpenBluetooth();
        }
    }

    /**
     * 开始搜索
     * search device
     */
    private void searchDeviceOrOpenBluetooth() {
        if (BtUtil.isOpen(mBluetoothAdapter)) {
            BtUtil.searchDevices(mBluetoothAdapter);
        }
    }

    @Override
    public void btStartDiscovery(Intent intent) {
        mBtnSearch.setText("搜索中…");
    }

    @Override
    public void btFinishDiscovery(Intent intent) {
        mBtnSearch.setText("重新搜索");
    }

    @Override
    public void btFoundDevice(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (null != mBluetoothAdapter && device != null) {
            searchBleAdapter.addDevices(device);
            String dName = device.getName() == null ? "未知设备" : device.getName();
            Log.d("未知设备", dName);
        }
    }

    @Override
    public void btBondStatusChange(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_BONDING://正在配对
                Log.d("BlueToothTestActivity", "正在配对......");
                break;
            case BluetoothDevice.BOND_BONDED://配对结束
                Log.d("BlueToothTestActivity", "完成配对");
                new connectThread().start(); //连接
                break;
            case BluetoothDevice.BOND_NONE://取消配对/未配对
                Log.d("BlueToothTestActivity", "取消配对");
                PrinterInstance.mPrinter=null;
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            default:
                break;
        }
    }


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
                //开始搜索
                btStartDiscovery(intent);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //结束搜索
                btFinishDiscovery(intent);
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                //蓝牙状态改变
                btStatusChanged(intent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //发现设备
                btFoundDevice(intent);
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                //配对状态改变
                btBondStatusChange(intent);
            } else if ("android.bluetooth.device.action.PAIRING_REQUEST".equals(action)) {
                btPairingRequest(intent);
            }
        }
    };

    private String getPrinterName() {
        String dName = PrintUtil.getDefaultBluetoothDeviceName(this);
        return getPrinterName(dName);
    }

    private String getPrinterName(String dName) {
        if (TextUtils.isEmpty(dName)) {
            dName = "未知设备";
        }
        return dName;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        BtUtil.cancelDiscovery(mBluetoothAdapter);

        if (null == searchBleAdapter) {
            return;
        }
        final BluetoothDevice bluetoothDevice = searchBleAdapter.getItem(position);
        if (null == bluetoothDevice) {
            return;
        }

        devicesAddress = bluetoothDevice.getAddress();
        connect2BlueToothdevice();
    }


    private void connect2BlueToothdevice() {
        mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(devicesAddress);
        devicesName = mDevice.getName();
        myPrinter = PrinterInstance.getPrinterInstance(mDevice, mHandler);
        if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {// 未绑定
            PairOrConnect(true);
        } else {
            PairOrConnect(false);
        }
        progressDialog.show();
    }

    private class connectThread extends Thread {
        @Override
        public void run() {
            if (myPrinter != null) {
                isConnected = myPrinter.openConnection();
            }
        }
    }


    private void PairOrConnect(boolean pair) {
        if (pair) {
            boolean success = false;
            try {
                // 开始配对 这段代码打开输入配对密码的对话框
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                success = (Boolean) createBondMethod.invoke(mDevice);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            Log.i("1111111111111", "createBond is success? : " + success);
        } else {
            new connectThread().start();
        }
    }

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {

                if (device != null && myPrinter != null && isConnected && device.equals(mDevice)) {
                    myPrinter.closeConnection();
                    mHandler.obtainMessage(PrinterConstants.Connect.CLOSED).sendToTarget();
                }
            }

        }
    };


    // 用于接受连接状态消息的 Handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    isConnected = true;
                    GlobalContants.ISCONNECTED = isConnected;
                    GlobalContants.DEVICENAME = devicesName;
                    PrefUtils.setString(BluetoothSearchActivity.this, GlobalContants.DEVICEADDRESS, devicesAddress);
                    bluDisconnectFilter = new IntentFilter();
                    bluDisconnectFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                    registerReceiver(myReceiver, bluDisconnectFilter);
                    hasRegDisconnectReceiver = true;
                    if (null != searchBleAdapter) {
                        searchBleAdapter.setConnectedDeviceAddress(mDevice.getAddress());
                        searchBleAdapter.notifyDataSetChanged();
                    }
                    ToastUtils.showShort("蓝牙连接成功！");
                    finish();
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    ToastUtils.showShort("蓝牙连接失败！");
                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    GlobalContants.ISCONNECTED = isConnected;
                    GlobalContants.DEVICENAME = devicesName;
                    ToastUtils.showShort("蓝牙连接关闭！");
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    ToastUtils.showShort("没有可连接的设备！");
                    break;
                // case 10:
                // if (setPrinterTSPL(myPrinter)) {
                // Toast.makeText(mContext, "蓝牙连接设置TSPL指令成功", 0).show();
                // }
                default:
                    break;
            }

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

    };

    @Override
    protected void onDestroy() {
        if (BtUtil.isOpen(mBluetoothAdapter)) {
            BtUtil.cancelDiscovery(mBluetoothAdapter);
        }
        super.onDestroy();
    }
}
