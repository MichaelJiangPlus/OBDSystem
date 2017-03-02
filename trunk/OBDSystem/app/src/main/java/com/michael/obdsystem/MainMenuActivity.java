package com.michael.obdsystem;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.michael.obdsystem.service.BluetoothLeService;
import com.michael.obdsystem.service.MqttService;

public class MainMenuActivity extends Activity {

    MqttService mqttService ;

    /*****蓝牙部分*****/
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;
    private final static String TAG = MainMenuActivity.class.getSimpleName();
    public MainMenuActivity mainMenuActivity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        mainMenuActivity = this;
        init();
        linkBluetooth();
        linkMqtt();
    }

    private void init(){
        ImageView btn_obd = (ImageView)findViewById(R.id.btn_obd);
        ImageView btn_map = (ImageView)findViewById(R.id.btn_map);
        ImageView btn_obdlist = (ImageView)findViewById(R.id.btn_obdlist);
        btn_obd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenuActivity.this,DeviceControl.class);
                startActivity(myIntent);
            }
        });
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenuActivity.this,NavigationActivity.class);
                startActivity(myIntent);
            }
        });
        btn_obdlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenuActivity.this,DataShowActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void linkBluetooth(){
        /*****获取要连接的蓝牙名称和地址*****/
        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void linkMqtt(){
        /*****连接MQTT信息*****/
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        bindService(mqttServiceIntent, mqttServiceConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                Toast.makeText(MainMenuActivity.this, "无法初始化蓝牙设备", Toast.LENGTH_SHORT).show();
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


    private final ServiceConnection mqttServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mqttService = ((MqttService.LocalBinder)service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}