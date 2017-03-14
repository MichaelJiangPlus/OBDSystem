package me.michaeljiang.obdsystem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.text.DecimalFormat;

import me.michaeljiang.obdsystem.fragment.CarFragment;
import me.michaeljiang.obdsystem.fragment.DashBoardFragment;
import me.michaeljiang.obdsystem.fragment.HomeFragment;
import me.michaeljiang.obdsystem.fragment.NavigationFragment;
import me.michaeljiang.obdsystem.fragment.SettingFragment;
import me.michaeljiang.obdsystem.service.BluetoothLeService;
import me.michaeljiang.obdsystem.service.MqttService;
import me.michaeljiang.obdsystem.util.DataAnalysed;
import me.michaeljiang.obdsystem.util.OBDCProtocol;

public class MainActivity extends AppCompatActivity {
    /*****常用变量*****/
    private final String TAG = "BottomNavigationBarDemo";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public MainActivity mainActivity;
    public MainActivity getMainActivity() {
        return mainActivity;
    }

    /*****界面相关控件*****/
    private BottomNavigationBar bottomNavigationBar ;


    /*****碎片布局*****/
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment = null;
    private DashBoardFragment dashBoardFragment = null;
    private NavigationFragment navigationFragment = null;
    private CarFragment carFragment = null ;
    private SettingFragment settingFragment = null;

    /*****蓝牙部分*****/
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;

    /*****MQTT部分*****/
    MqttService mqttService ;

    /*****数据解析*****/
    DataAnalysed dataAnalysed = new DataAnalysed();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        linkBluetooth();
        linkMqtt();
        initActivity();
        initfragment();
        initBottomNavigationBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
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

    }

    /**
     * 底部菜单栏
     */
    private void initBottomNavigationBar(){
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setAutoHideEnabled(true);
//        默认模式，只显示图标，点击后显示该图标的文字
//        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);

        //直接显示所有内容，然后将内容显示在屏幕上
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_white_32, "导航"))
                .addItem(new BottomNavigationItem(R.drawable.ic_dashboard_white_32, "仪表"))
                .addItem(new BottomNavigationItem(R.drawable.ic_car_white_32, "爱车"))
                .addItem(new BottomNavigationItem(R.drawable.ic_setting_white_32, "设置"))
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (position){
                    case 0:{
                        if (homeFragment == null) {
                            homeFragment = HomeFragment.newInstance("Home");
                        }
                        fragmentTransaction.replace(R.id.tb, homeFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case 1:{
                        if (navigationFragment == null) {
                            navigationFragment = NavigationFragment.newInstance("Location");
                        }
                        fragmentTransaction.replace(R.id.tb, navigationFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case 2:{
                        if (dashBoardFragment == null) {
                            dashBoardFragment = DashBoardFragment.newInstance("DashBoard");
                        }
                        fragmentTransaction.replace(R.id.tb, dashBoardFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case 3:{
                        if (carFragment == null) {
                            carFragment = CarFragment.newInstance("MyCar");
                        }
                        fragmentTransaction.replace(R.id.tb, carFragment);
                        fragmentTransaction.commit();
                        break;
                    }
                    case 4:{
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
                Log.d(TAG, "onTabUnselected() called with: " + "position = [" + position + "]");

            }
            @Override
            public void onTabReselected(int position) {
                Log.d(TAG, "onTabReselected() called with: " + "position = [" + position + "]");

            }
        });

    }

    /**
     * 初始化fragment管理
     */
    private void initfragment(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        homeFragment = HomeFragment.newInstance("Home");
        fragmentTransaction.add(R.id.tb, homeFragment);
        fragmentTransaction.commit();
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

        }
    };


    private void displayData(String data) {
        if (data != null) {
            byte[][] result;
            result = dataAnalysed.analysisDate(data);
            Log.d(TAG,data);
            String command=new String(result[1]);//命令位
            int a=dataAnalysed.hexTodec(result[2]);//A
            int b=dataAnalysed.hexTodec(result[3]);//B
            DecimalFormat df   = new DecimalFormat("######0.00");

            double temp= OBDCProtocol.Mode01_calculate(command,a,b,0);

            String sum=df.format(temp);
            switch (command){
                case "05":{
                    Log.d(TAG,sum+"°C");
                    break;
                }
                case "0C":{
                    Log.d(TAG,sum+"rpm");
                    break;
                }
                case "0D":{
                    Log.d(TAG,sum+"km/h");
                    break;
                }
                case "0F":{
                    Log.d(TAG,sum+"°C");
                    break;
                }
                case "2F":{
                    Log.d(TAG,sum+"%");
                    break;
                }
                case "5C":{
                    Log.d(TAG,sum+"°C");
                    break;
                }
                case "5E":{
                    Log.d(TAG,sum+"L/h\"");
                    break;
                }
                default:break;
            }
        }
    }

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
                displayData(result);
            }
        }
    };
}
