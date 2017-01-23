package com.michael.obdsystem;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.obdsystem.service.BluetoothLeService;
import com.michael.obdsystem.service.DataGet;
import com.michael.obdsystem.util.BluetoothTool;
import com.michael.obdsystem.util.DataAnalysed;
import com.michael.obdsystem.util.OBDCProtocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataShowActivity extends Activity {
    /*****自定义类*****/
    private DataAnalysed dataAnalysed;
    private OBDCProtocol obdcProtocol;
    private static DataShowActivity dataShowActivity = null;
    private String uuid="";

    /*****蓝牙部分*****/
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;

    private int i = 1;
    byte[] WriteBytes = new byte[20];
    private String mDeviceName;
    private String mDeviceAddress;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private final static String TAG = DeviceControl.class.getSimpleName();
    public static DataShowActivity getDataShowActivity() {
        return dataShowActivity;
    }

    /***** Android 控件部分*****/
    private TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_data_show);
        dataAnalysed = new DataAnalysed();
        obdcProtocol = new OBDCProtocol();
        dataShowActivity = this;
        //获取电话信息
        TelephonyManager tm = (TelephonyManager) dataShowActivity.getSystemService(TELEPHONY_SERVICE);
        uuid=tm.getSubscriberId();

        textView = (TextView)findViewById(R.id.txt);
        /*****获取要连接的蓝牙名称和地址*****/
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


        Button btn_send = (Button) findViewById(R.id.button);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataGet dataGet=new DataGet(dataShowActivity);
                dataGet.execute();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        if(mBluetoothLeService != null)
            mBluetoothLeService.disconnect();
        mBluetoothLeService = null;
    }

    private void displayData(String data) {
        if (data != null) {
            //显示在界面即可
            textView.setText(data);
        }
    }

    /*****蓝牙部分*****/

    /*蓝牙连接*/
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                //Log.e(TAG, "Unable to initialize Bluetooth");
                Toast.makeText(DataShowActivity.this, "无法初始化蓝牙设备", Toast.LENGTH_SHORT).show();
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
            //自动连接到目标设备
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }

    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    //通过BroadcastReceiver获取的信息
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
//                updateConnectionState(R.string.connected);
                //invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
//                updateConnectionState(R.string.disconnected);
                //invalidateOptionsMenu();
//                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //System.out.println(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                String result = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.i("ajx", result);
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            }
        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = "未知服务";
        String unknownCharaString = "未知特征";
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, BluetoothTool.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, BluetoothTool.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);//写接口
                if(uuid.equals("0000fff1-0000-1000-8000-00805f9b34fb"))
                {
                    mWriteCharacteristic=gattCharacteristic;
                }else if(uuid.equals("0000fff4-0000-1000-8000-00805f9b34fb"))//读回调接口
                {
                    mNotifyCharacteristic=gattCharacteristic;
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, true);
                }

            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        // mGattServicesList.setAdapter(gattServiceAdapter);
    }

    public void sendString(String command) {
        try {
            if (mWriteCharacteristic != null) {
                if (!command.matches("\\r$"))//如果末尾没有回车，自动补全
                    command += "\r";
                if (command.length() > 0) {
                    byte[] WriteBytes;
                    WriteBytes = command.getBytes();
                    System.out.println("command:" + command);
                    //WriteBytes = hex2byte(sendStr.getBytes());
                    mWriteCharacteristic.setValue((byte) 0x00, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                    mWriteCharacteristic.setValue(WriteBytes);
                    mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
                }
            } else {
                System.out.println("写特性未实例化");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
