package com.michael.obdsystem;

import android.Manifest;
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
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.michael.obdsystem.model.Point;
import com.michael.obdsystem.service.BluetoothLeService;
import com.michael.obdsystem.service.MqttInstance;
import com.michael.obdsystem.service.Send;
import com.michael.obdsystem.util.CoordinateConversion;
import com.michael.obdsystem.util.DataAnalysed;
import com.michael.obdsystem.util.OBDCProtocol;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceControl extends Activity {
    /*****自定义类*****/
    private DataAnalysed dataAnalysed;
    private OBDCProtocol obdcProtocol;
    private CoordinateConversion coordinateConversion;
    /*****蓝牙部分*****/
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

    /*****MQTT*****/
    private MqttAsyncClient instance = null;
    private Handler handler;
    private Thread mqttlink;

    /*****常用变量*****/
    private final static int CONNECT = 1;
    private final static int FAIL = 0;
    private final static int SUCCESS = 1;
    private final static String TAG = DeviceControl.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private static DeviceControl deviceControl = null;

    /*****自定义全局变量*****/
    private int i = 1;
    byte[] WriteBytes = new byte[20];

    /*****GPS全局变量*****/
    LocationManager locationManager = null;
    Location myPoint = null;
    String provider;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(myPoint);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public static DeviceControl getDeviceControl() {
        return deviceControl;
    }

    private IMqttActionListener mqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Message msg = new Message();
            msg.what = CONNECT;
            msg.arg1 = SUCCESS;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Message msg = new Message();
            msg.what = CONNECT;
            msg.arg1 = FAIL;
            handler.sendMessage(msg);
        }
    };


    private void mqttstart() {
        mqttlink = new Thread() {
            public void run() {
                instance = MqttInstance.getInstance("thingworx.zucc.edu.cn", "", "");
                try {
                    instance.connect(MqttInstance.getOptions(), null, mqttActionListener);
                } catch (MqttException e) {
                    e.printStackTrace();
                    Toast.makeText(deviceControl, "Fail...", Toast.LENGTH_LONG);
                }
            }
        };
        mqttlink.start();
    }


    public void sendOrder(String Topic, String order) {
        if (MqttInstance.getInstance().isConnected()) {
            try {
                MqttInstance.getInstance().publish(Topic, order.getBytes(), 2, false);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void showLocation(Location point) {
        String result = "lat: " + point.getLatitude() + "  " + "lon: " + point.getLongitude();
        double lat = 30.3287750;//纬度
        double lon = 120.149800;//经度
        //原版  接受GPS并且转换称百度坐标
//        Point myPoint=CoordinateConversion.wgs_gcj_encrypts(point.getLatitude(), point.getLongitude());
//        myPoint = CoordinateConversion.google_bd_encrypt(myPoint.getLat(), myPoint.getLng());
        Point myPoint=CoordinateConversion.wgs_gcj_encrypts(lat, lon);
        myPoint = CoordinateConversion.google_bd_encrypt(myPoint.getLat(), myPoint.getLng());

        deviceControl.sendOrder("location777",myPoint.getLat()+","+myPoint.getLng()+","+"20");
    }


    public void GPSReceiver() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //大致意思就是在这里写没有权限的话会怎么样
            Toast.makeText(DeviceControl.this, "没有权限", Toast.LENGTH_LONG);
            return;
        } else {
            List<String> providerList = locationManager.getProviders(true);
            provider = LocationManager.NETWORK_PROVIDER;
            //
//            if(providerList.contains(LocationManager.GPS_PROVIDER)){
//                provider=LocationManager.GPS_PROVIDER;
//            }
//            else
            //
            if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                provider = LocationManager.NETWORK_PROVIDER;
            } else {
                Toast.makeText(DeviceControl.this, "没有定位服务", Toast.LENGTH_LONG).show();
                return;
            }
            myPoint = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

            if (myPoint != null) {
                this.showLocation(myPoint);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 主页面初始化
         */
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_design);

        dataAnalysed = new DataAnalysed();
        obdcProtocol = new OBDCProtocol();
        deviceControl = this;

        mqttstart();

        /*****获取要连接的蓝牙名称和地址*****/
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == CONNECT) {
                    if (msg.arg1 == SUCCESS) {
                        Toast.makeText(DeviceControl.this, "Success", Toast.LENGTH_SHORT).show();
                        Send send=new Send(deviceControl);
                        send.execute();
                        deviceControl.GPSReceiver();
                    } else {
                        Toast.makeText(DeviceControl.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                }
                super.handleMessage(msg);
            }
        };

    }


    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else {
            if(locationManager!=null){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            }
        }

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

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            else{
                locationManager.removeUpdates(locationListener);
            }

        }

    }



    //通过BroadcastReceiver获取的信息
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
            String command=new String (result[1]);//命令位
            int a=dataAnalysed.hexTodec(result[2]);//A
            int b=dataAnalysed.hexTodec(result[3]);//B
            DecimalFormat    df   = new DecimalFormat("######0.00");
            double temp=obdcProtocol.Mode01_calculate(command,a,b,0);
            String sum=df.format(temp);
            switch (command){
                case "05":{
//                    txt_CoolantTemperature.setText(sum+"°C");
                    deviceControl.sendOrder("0105",String.valueOf(sum));
                    break;
                }
                case "0C":{
//                    txt_EngineTurn.setText(sum+"rpm");
                    deviceControl.sendOrder("010C",String.valueOf(sum));
                    break;
                }
                case "0D":{
//                    txt_speed.setText(sum+"Km/h");
                    deviceControl.sendOrder("010D",String.valueOf(sum));
                    break;
                }
                case "0F":{
//                    txt_TankTemperature.setText(sum+"°C");
                    deviceControl.sendOrder("010F",String.valueOf(sum));
                    break;
                }
                case "2F":{
//                    txt_OilSurplus.setText(sum+"%");
                    deviceControl.sendOrder("012F",String.valueOf(sum));
                    break;
                }
                case "5C":{
//                    txt_EnginTemperature.setText(sum+"°C");
                    deviceControl.sendOrder("015C",String.valueOf(sum));
                    break;
                }
                case "5E":{
//                    txt_OilUse.setText(sum+"L/h");
                    deviceControl.sendOrder("015E",String.valueOf(sum));
                    break;
                }
                default:break;
            }

//            mDataField.setText(data);

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



    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                dev_connection.setText(resourceId);
            }
        });
    }

    private void clearUI() {
        //mDataField.setText("No Data...");
    }

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


    /*****没用上的进制转换*****/
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
}


//android.content.ServiceConnection是一个接口，实现（implementate）这个接口有2个方法需要重写（Override）。
// 一个是当Service成功绑定后会被回调的onServiceConnected()方法，
// 另一个是当Service解绑定或者Service被关闭时被回调的onServiceDisconnected()。
//前者（onServiceConnected()方法）会传入一个IBinder对象参数，
// 这个IBinder对象就是在Service的生命周期回调方法的onBind()方法中的返回值