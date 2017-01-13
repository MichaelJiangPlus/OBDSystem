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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.michael.obdsystem.service.BluetoothLeService;
import com.michael.obdsystem.service.Send;
import com.michael.obdsystem.util.DataAnalysed;
import com.michael.obdsystem.util.OBDCProtocol;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceControl extends Activity {


    private final static String TAG = DeviceControl.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private static DeviceControl deviceControl = null;
    private SharedPreferences loadmDevice=getSharedPreferences("data",MODE_PRIVATE);
    private int i = 1;
    //控件
    private TextView dev_name;
    private TextView dev_addr;
    private TextView dev_connection;
    private Button Clear;
    private Button send;
    private EditText editText;

    private TextView mDataField;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private BluetoothGattCharacteristic mWriteCharacteristic;
    private String mDeviceName;
    private String mDeviceAddress;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    byte[] WriteBytes = new byte[20];
    private ToggleButton connectBtn;

    public static DeviceControl getDeviceControl() {
        return deviceControl;
    }

    private TextView txt_speed;
    private TextView txt_EngineTurn;
    private TextView txt_OilSurplus;
    private TextView txt_EnginTemperature;
    private TextView txt_OilUse;
    private TextView txt_TankTemperature;
    private TextView txt_CoolantTemperature;
    private DataAnalysed dataAnalysed;
    private OBDCProtocol obdcProtocol;
//    private Timestamp start;
//    private Timestamp now;
    private void init() {
        this.send = (Button) findViewById(R.id.button);
        this.editText = (EditText) findViewById(R.id.editText);
        this.dev_name = (TextView) findViewById(R.id.ctrl_txt2);
        this.dev_addr = (TextView) findViewById(R.id.ctrl_txt21);
        this.dev_connection = (TextView) findViewById(R.id.ctrl_txt4);
        this.mDataField = (TextView) findViewById(R.id.data);
        this.Clear = (Button) findViewById(R.id.btnClear);
        this.connectBtn = (ToggleButton) findViewById(R.id.connectBtn);
        this.txt_speed = (TextView) findViewById(R.id.txt_speed);
        this.txt_EngineTurn = (TextView) findViewById(R.id.txt_EngineTurn);
        this.txt_OilSurplus = (TextView) findViewById(R.id.txt_OilSurplus);
        this.txt_EnginTemperature = (TextView) findViewById(R.id.txt_EnginTemperature);
        this.txt_OilUse = (TextView) findViewById(R.id.txt_OilUse);
        this.txt_TankTemperature = (TextView) findViewById(R.id.txt_TankTemperature);
        this.txt_CoolantTemperature = (TextView) findViewById(R.id.txt_CoolantTemperature);
        dataAnalysed=new DataAnalysed();
        obdcProtocol=new OBDCProtocol();
        deviceControl=this;
    }

    private void btnSetting(){
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton toggleButton = (ToggleButton) view;
                if (!toggleButton.isChecked())//连接开启时check为false
                {
                    //连接目标设备
                    try {
                        mBluetoothLeService.connect(mDeviceAddress);
                        dev_connection.setText("Connected");
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                } else {
                    //关闭设备连接
                    mBluetoothLeService.disconnect();
                    dev_connection.setText("Disconnected");
                }
            }
        });


        this.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = editText.getText().toString();
                result = result + '\r';
                sendString(result);
                Send send=new Send(deviceControl);
//                start=new Timestamp(currentTimeMillis());
//                now=new Timestamp(currentTimeMillis());
                send.execute();
            }
        });

        this.Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 主页面初始化
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        init();
        btnSetting();
//        final Intent intent = getIntent();
//        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
//        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        mDeviceName=loadmDevice.getString(EXTRAS_DEVICE_NAME,"");
        mDeviceAddress=loadmDevice.getString(EXTRAS_DEVICE_ADDRESS,"");
        if(mDeviceName.equals("")){

        }

        // Sets up UI references.
        dev_name.setText(mDeviceName);
        dev_addr.setText(mDeviceAddress);


        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        this.dev_connection.setText("自动连接设备中");





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
        mBluetoothLeService.disconnect();
        mBluetoothLeService = null;
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    //蓝牙连接
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                //Log.e(TAG, "Unable to initialize Bluetooth");
                Toast.makeText(DeviceControl.this, "无法初始化蓝牙设备", Toast.LENGTH_SHORT).show();
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
            //自动连接到目标设备
//            connectBtn.setChecked(false);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    /////////////////////////////////////////////////////////////////////////////////////
    //他是通过BroadcastReceiver获取的信息
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                //invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                //invalidateOptionsMenu();
                clearUI();
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

    /**
     * 数据处理,在这里处理我们刚获得的数据
     *
     * @param data
     */
    private void displayData(String data) {
        if (data != null) {
            byte[][] result;
            result = dataAnalysed.analysisDate(data);
            String command=new String(result[1]);//命令位
            int a=dataAnalysed.hexTodec(result[2]);//A
            int b=dataAnalysed.hexTodec(result[3]);//B
            DecimalFormat df   = new DecimalFormat("######0.00");
            double temp=obdcProtocol.Mode01_calculate(command,a,b,0);
            String sum=df.format(temp);
            switch (command){
                case "05":{
//                    now.setTime(currentTimeMillis());
//                    long time=now.getTime()-start.getTime();
//                    mDataField.setText(String.valueOf(time));
//                    start.setTime(currentTimeMillis());
                    txt_CoolantTemperature.setText(sum+"°C");
                    break;
                }
                case "0C":{
                    txt_EngineTurn.setText(sum+"rpm");
                    break;
                }
                case "0D":{
                    txt_speed.setText(sum+"Km/h");
                    break;
                }
                case "0F":{
                    txt_TankTemperature.setText(sum+"°C");
                    break;
                }
                case "2F":{
                    txt_OilSurplus.setText(sum+"%");
                    break;
                }
                case "5C":{
                    txt_EnginTemperature.setText(sum+"°C");
                    break;
                }
                case "5E":{
                    txt_OilUse.setText(sum+"L/h");
                    break;
                }
                default:break;
            }

            mDataField.setText(data);

        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dev_connection.setText(resourceId);
            }
        });
    }


    private void clearUI() {
        //mDataField.setText("No Data...");
    }

    /////////////////////////////////////////////////////////////////////////
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


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
                    LIST_NAME, lookup(uuid, unknownServiceString));
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
                        LIST_NAME, lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);//写接口
                if (uuid.equals("0000fff1-0000-1000-8000-00805f9b34fb")) {

                    mWriteCharacteristic = gattCharacteristic;

                } else if (uuid.equals("0000fff4-0000-1000-8000-00805f9b34fb"))//读回调接口
                {

                    mNotifyCharacteristic = gattCharacteristic;
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
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

    }

    ////////////////////////////////////////////////////////
    private static HashMap<String, String> attributes = new HashMap();

    static {
        attributes.put("0000fff2-0000-1000-8000-00805f9b34fb", "Device OBDCYX Service");
        attributes.put("0000fff1-0000-1000-8000-00805f9b34fb", "Device OBDCYX String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
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


    //-----------------------------------------------------------------------
    //进制转换，没用上
    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            //throw new IllegalArgumentException("长度不是偶数");
            //edit by me 由原来的抛出异常改为用回车补全
            byte[] b2 = new byte[(b.length - 1) / 2 + 1];
            for (int i = 0; i < b.length - 1; i += 2) {
                String item = new String(b, i, 2);
                b2[i / 2] = (byte) Integer.parseInt(item, 16);
            }
            b = null;
            return b2;
        } else {
            byte[] b2 = new byte[b.length / 2];
            for (int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2);
                // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }
            b = null;
            return b2;
        }
    }

    //------------------------------------------------------------------------
}


//android.content.ServiceConnection是一个接口，实现（implementate）这个接口有2个方法需要重写（Override）。
// 一个是当Service成功绑定后会被回调的onServiceConnected()方法，
// 另一个是当Service解绑定或者Service被关闭时被回调的onServiceDisconnected()。
//前者（onServiceConnected()方法）会传入一个IBinder对象参数，
// 这个IBinder对象就是在Service的生命周期回调方法的onBind()方法中的返回值