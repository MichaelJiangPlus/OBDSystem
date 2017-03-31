package me.michaeljiang.obddevelopmentassistant.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.michaeljiang.obddevelopmentassistant.R;
import me.michaeljiang.obddevelopmentassistant.activity.fragment.DashBoardFragment;
import me.michaeljiang.obddevelopmentassistant.activity.fragment.PubSubFragment;
import me.michaeljiang.obddevelopmentassistant.activity.fragment.SettingFragment;
import me.michaeljiang.obddevelopmentassistant.model.CarData;
import me.michaeljiang.obddevelopmentassistant.service.BluetoothLeService;
import me.michaeljiang.obddevelopmentassistant.service.MqttService;
import me.michaeljiang.obddevelopmentassistant.setting.AppSetting;
import me.michaeljiang.obddevelopmentassistant.setting.BluetoothLeServiceSetting;
import me.michaeljiang.obddevelopmentassistant.setting.MqttSeting;
import me.michaeljiang.obddevelopmentassistant.util.DataAnalysed;
import me.michaeljiang.obddevelopmentassistant.util.OBDCProtocol;

public class MainActivity extends AppCompatActivity {
    /*****常用变量*****/
    public static MainActivity mainActivity;
    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    private SharedPreferences preferences ;

    /*****界面相关控件*****/
    private BottomNavigationBar bottomNavigationBar ;

    /*****碎片布局*****/
    private  int postion = 0;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SettingFragment settingFragment;
    private DashBoardFragment dashBoardFragment;
    private PubSubFragment pubSubFragment;

    /*****蓝牙部分*****/
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;

    /*****MQTT部分*****/
    private Gson gson = new Gson();
    private Handler mqtt_handle;
    private MqttAsyncClient client=null;

    private MqttService mqttService;

    /*****数据解析*****/
    DataAnalysed dataAnalysed = new DataAnalysed();
    private List<CarData> carDataList = new ArrayList<>();
    private Map<String,CarData> carDataMap = new HashMap<>();
    private CarData sendCarData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mainActivity = this;
        initBottomNavigationBar();
        initMqttData();
        initActivity();
        initFragment();
    }

    private void initMqttData(){
        try {
            preferences = getSharedPreferences("data" ,MODE_PRIVATE);
            if(preferences!=null){
                String mqttHost = preferences.getString(MqttSeting.MQTT_HOST_ACTION,MqttSeting.Host);
                String mqttPort = preferences.getString(MqttSeting.MQTT_PORT_ACTION,MqttSeting.Port);
                String mqttUserName = preferences.getString(MqttSeting.MQTT_USER_NAME_ACTION,MqttSeting.UserID);
                String mqttPassword = preferences.getString(MqttSeting.MQTT_PASSWORD_ACTION,MqttSeting.PassWord);
                String mqttDeviceID = preferences.getString(MqttSeting.MQTT_DEVICE_ID_ACTION,MqttSeting.DeviceID);

                MqttSeting.Host = mqttHost;
                MqttSeting.Port = mqttPort;
                MqttSeting.DeviceID = mqttDeviceID;
                MqttSeting.UserID = mqttUserName;
                MqttSeting.PassWord = mqttPassword;
            }
        }
        catch (Exception e){
            Log.d("LoadData","LoadData : ERROR");
        }
    }

    private void initBottomNavigationBar(){
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setAutoHideEnabled(true);
//        默认模式，只显示图标，点击后显示该图标的文字
//        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);

        //直接显示所有内容，然后将内容显示在屏幕上
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_dashboard_white_32, "DashBoard "))
                .addItem(new BottomNavigationItem(R.drawable.ic_subpub_white_32, "Pub/Sub"))
                .addItem(new BottomNavigationItem(R.drawable.ic_setting_white_32, "Setting"))
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                Log.d(AppSetting.OBD_DEVELOPMENT_ASSISTANT_ACTION, "onTabSelected() called with: " + "position = [" + position + "]");
                getMainActivity().postion = position;
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (position){
                    case 0:{
                        if (dashBoardFragment == null) {
                            dashBoardFragment = DashBoardFragment.newInstance("Location");
                        }
                        fragmentTransaction.replace(R.id.tb, dashBoardFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case 1:{
                        //待定
                        if (pubSubFragment == null) {
                            pubSubFragment = PubSubFragment.newInstance("PubSub");
                        }
                        fragmentTransaction.replace(R.id.tb, pubSubFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case 2:{
                        if (settingFragment == null) {
                            settingFragment = SettingFragment.newInstance("Setting");
                        }
                        fragmentTransaction.replace(R.id.tb, settingFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                }

            }
            @Override
            public void onTabUnselected(int position) {
                Log.d(AppSetting.OBD_DEVELOPMENT_ASSISTANT_ACTION, "onTabUnselected() called with: " + "position = [" + position + "]");

            }
            @Override
            public void onTabReselected(int position) {
                Log.d(AppSetting.OBD_DEVELOPMENT_ASSISTANT_ACTION, "onTabReselected() called with: " + "position = [" + position + "]");
            }
        });

    }

    private void initFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        dashBoardFragment = DashBoardFragment.newInstance("Location");
        fragmentTransaction.replace(R.id.tb, dashBoardFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(AppSetting.OBD_DEVELOPMENT_ASSISTANT_ACTION, "Connect request result=" + result);
        }
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 初始化界面相关UI
     * 在本Demo中并不存在
     */
    private void initActivity(){
        fragmentManager = getFragmentManager();
    }

    public void linkMQTT(){
        /*****连接MQTT信息*****/
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        bindService(mqttServiceIntent, mqttServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public void linkBluetooth(){
        /*****获取要连接的蓝牙名称和地址*****/
        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(BluetoothLeServiceSetting.BLUETOOTH_DEVICE_ADDRESS_TAG);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void ReceiverData(Message msg){
        try {
            Gson gson = new Gson();

        }
        catch (Exception e){
            Log.d("MainActivity","Json格式有误");
        }
    }

    public void unLinkBluetooth(){
        mBluetoothLeService.disconnect();
    }

    public void unLinkMqtt(){
        mqttService.UnLinkMqtt();
    }

    public void reLinkMQTT(){
        mqttService.UnLinkMqtt();
        mqttService.mqttstart();
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
                Toast.makeText(MainActivity.this, "无法初始化蓝牙设备", Toast.LENGTH_SHORT).show();
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
            mqttService = null;
        }
    };

}
