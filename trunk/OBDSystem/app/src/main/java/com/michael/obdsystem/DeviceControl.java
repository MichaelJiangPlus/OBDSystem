package com.michael.obdsystem;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.obdsystem.model.Point;
import com.michael.obdsystem.service.BluetoothLeService;
import com.michael.obdsystem.service.Send;
import com.michael.obdsystem.util.CoordinateConversion;
import com.michael.obdsystem.util.DataAnalysed;
import com.michael.obdsystem.util.OBDCProtocol;
import com.michael.obdsystem.view.DashBoard;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;


public class DeviceControl extends Activity {
    /*****自定义类*****/
    private DataAnalysed dataAnalysed;
    private OBDCProtocol obdcProtocol;
    private static DeviceControl deviceControl = null;
    private String uuid="";

    /*****蓝牙部分*****/
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceName;
    private String mDeviceAddress;
    private final static String TAG = DeviceControl.class.getSimpleName();



    /*****MQTT*****/
    private final static int CONNECTED=1;
    private final static int LOST=2;
    private final static int FAIL=3;
    private final static int RECEIVE=4;

    private MqttAsyncClient client=null;
    private Handler handler;

    private final static String Host = "thingworx.zucc.edu.cn";
    private final static String HostPort = "1883";
    private final static String username = "jiangzhanxiang";
    private final static String userpwd = "KLSFDJW9203";
    private String pubTopic ="";
    private String pubInfo ="";
    private String subTopic = "";



    /*****界面相关控件*****/
    private String info_EngineTurn;
    private String info_OilSurplus;
    private String info_CoolantTemperature;
    private String info_TankTemperature;
    private String info_OilUse;
    private TextView txt_EngineTurn;
    private TextView txt_OilSurplus;
    private TextView txt_CoolantTemperature;
    private TextView txt_TankTemperature;
    private TextView txt_OilUse;
    private Button btn_send;
    private Button button3;
    private DashBoard myview;


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

    private IMqttActionListener mqttActionListener=new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Message msg=new Message();
            msg.what=CONNECTED;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Message msg=new Message();
            msg.what=FAIL;
            handler.sendMessage(msg);
        }
    };

    private void mqttstart() {
        handler  =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==CONNECTED){
                    Toast.makeText(DeviceControl.this,"连接成功", Toast.LENGTH_SHORT).show();
                    deviceControl.GPSReceiver();
                }else if(msg.what==LOST){
                    Toast.makeText(DeviceControl.this,"连接丢失，进行重连", Toast.LENGTH_SHORT).show();
                    new ConnectThread().start();
                }else if(msg.what==FAIL){
                    Toast.makeText(DeviceControl.this,"连接失败", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
        new ConnectThread().start();
    }

    public MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        String strUserName=username;
        String strPassword=userpwd;
        if(strUserName!=null&&strUserName.length()>0&&strPassword!=null&&strPassword.length()>0){
            options.setUserName(strUserName);
            options.setPassword(strPassword.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(30);
        }
        return options;
    }

    private class ConnectThread extends Thread {
        @Override
        public void run(){
            if(client==null){
                try {
                    client=new MqttAsyncClient("tcp://"+Host+":"+HostPort, ""+ UUID.randomUUID(),new MemoryPersistence());
                    client.connect(getOptions(),null,mqttActionListener);
                    //如果有Sub的话只需要在写一个callback就好
//                    client.setCallback(callback);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void sendOrder(String Topic, String order) {
        if(Topic.length()>0&&client!=null&&client.isConnected()){
            try {
                client.publish(Topic,order.getBytes(),2,false);
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
        Point myPoint= CoordinateConversion.wgs_gcj_encrypts(lat, lon);
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

    private void init(){
        txt_EngineTurn=(TextView)findViewById(R.id.txt_EngineTurn);
        txt_OilSurplus=(TextView)findViewById(R.id.txt_OilSurplus);
        txt_CoolantTemperature=(TextView)findViewById(R.id.txt_CoolantTemperature);
        txt_TankTemperature=(TextView)findViewById(R.id.txt_TankTemperature);
        txt_OilUse=(TextView)findViewById(R.id.txt_OilUse);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/ttt.ttf");
        txt_EngineTurn.setTypeface(typeface);
        txt_OilSurplus.setTypeface(typeface);
        txt_CoolantTemperature.setTypeface(typeface);
        txt_TankTemperature.setTypeface(typeface);
        txt_OilUse.setTypeface(typeface);

        info_CoolantTemperature =  txt_CoolantTemperature.getText().toString();
        info_EngineTurn  = txt_EngineTurn.getText().toString();
        info_OilSurplus = txt_OilSurplus.getText().toString();
        info_OilUse = txt_OilUse.getText().toString();
        info_TankTemperature = txt_TankTemperature.getText().toString();

        myview=(DashBoard)findViewById(R.id.frame);
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


        TelephonyManager tm = (TelephonyManager) deviceControl.getSystemService(TELEPHONY_SERVICE);
        uuid=tm.getSubscriberId();

        init();

        btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send send=new Send(mBluetoothLeService);
                send.execute();
                btn_send.setVisibility(View.INVISIBLE);
            }
        });


        /*****获取要连接的蓝牙名称和地址*****/
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceControl.this,DataShowActivity.class);
                intent.putExtra(DeviceControl.EXTRAS_DEVICE_NAME, mDeviceName);
                intent.putExtra(DeviceControl.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
                startActivity(intent);
                DeviceControl.getDeviceControl().onDestroy();
            }
        });

        mqttstart();

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

    /**
     * 数据处理,在这里处理我们刚获得的数据
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
//            Toast.makeText(DeviceControl.this,command,Toast.LENGTH_LONG).show();
            switch (command){
                case "05":{
                    txt_CoolantTemperature.setText(info_CoolantTemperature+sum+"°C");
                    deviceControl.sendOrder("0105", String.valueOf(sum));
                    break;
                }
                case "0C":{
                    txt_EngineTurn.setText(info_EngineTurn+sum+"rpm");
                    deviceControl.sendOrder("010C", String.valueOf(sum));
                    break;
                }
                case "0D":{
//                    txt_speed.setText(sum+"Km/h");
                    float pf = Float.valueOf(sum) / 236.5f;
                    myview.setCurrentStatus(pf);//旋转角度
                    myview.invalidate();//显示值的变化
                    deviceControl.sendOrder("010D", String.valueOf(sum));
                    break;
                }
                case "0F":{
                    txt_TankTemperature.setText(info_TankTemperature+sum+"°C");
                    deviceControl.sendOrder("010F", String.valueOf(sum));
                    break;
                }
                case "2F":{
                    txt_OilSurplus.setText(info_OilSurplus+sum+"%");
                    deviceControl.sendOrder("012F", String.valueOf(sum));
                    break;
                }
                case "5C":{
//                    txt_EnginTemperature.setText(sum+"°C");
                    deviceControl.sendOrder("015C", String.valueOf(sum));
                    break;
                }
                case "5E":{
                    txt_OilUse.setText(info_OilUse+sum+"L/h");
                    deviceControl.sendOrder("015E", String.valueOf(sum));
                    break;
                }
                default:break;
            }
        }
    }


    /*****蓝牙部分*****/

    /*蓝牙连接*/
    //android.content.ServiceConnection是一个接口，实现（implementate）这个接口有2个方法需要重写（Override）。
    // 一个是当Service成功绑定后会被回调的onServiceConnected()方法，
    // 另一个是当Service解绑定或者Service被关闭时被回调的onServiceDisconnected()。
    //前者（onServiceConnected()方法）会传入一个IBinder对象参数，
    // 这个IBinder对象就是在Service的生命周期回调方法的onBind()方法中的返回值
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
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    //通过BroadcastReceiver获取的信息
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String result = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);

                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

}


