package com.michael.obdsystem;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.obdsystem.model.LeDeviceListAdapter;

import java.util.ArrayList;

public class DeviceState extends Activity {
    private ListView listView;
    private Button buttonScan;
    private Button buttonStop;
    private Handler mHandler;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 20000;//一次扫描时间设置

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_device_state);

        //控件初始化
        this.listView=(ListView)findViewById(R.id.dev_lv1);
        this.buttonScan=(Button)findViewById(R.id.dev_btn1);
        this.buttonStop=(Button)findViewById(R.id.dev_btn2);


        //扫描时间控制
        mHandler = new Handler();

        /**
         * 绑定监听，是否开始扫描
         */
        this.buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLeDevice(true);
            }
        });

        this.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLeDevice(false);
            }
        });

        /**
         * 将扫描到的设备放到列表中，如果点击列表中的某个设备则跳转到活动页面
         */
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(i);
                if (device == null) return;
                final Intent intent = new Intent(DeviceState.this, MainMenuActivity.class);
                intent.putExtra(DeviceControl.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(DeviceControl.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                startActivity(intent);
            }
        });

        //初始化蓝牙部分，检测蓝牙是否支持
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "你的设备不支持BLE功能", Toast.LENGTH_SHORT).show();
            finish();
        }

        //获取Adapeter
        final BluetoothManager bluetoothManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //检测是否获取到蓝牙Adapter
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "获取蓝牙功能失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED) {
            //如果请求开启蓝牙被拒绝则关闭活动
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        // 如果蓝牙未开启，启动蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }

        // 将数据源绑定到UI
        mLeDeviceListAdapter = new LeDeviceListAdapter(DeviceState.this,R.layout.listitem_device);
        listView.setAdapter(mLeDeviceListAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }
}
