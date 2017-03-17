package me.michaeljiang.obdsystem;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.michaeljiang.obdsystem.fragment.DashBoardFragment;
import me.michaeljiang.obdsystem.fragment.CarFragment;
import me.michaeljiang.obdsystem.fragment.NavigationFragment;
import me.michaeljiang.obdsystem.model.CarData;
import me.michaeljiang.obdsystem.service.BluetoothLeService;
import me.michaeljiang.obdsystem.service.MqttService;
import me.michaeljiang.obdsystem.util.AppSetting;
import me.michaeljiang.obdsystem.util.DataAnalysed;
import me.michaeljiang.obdsystem.util.OBDCProtocol;

/**
 * UIHandle更新是个问题  需要重新刷新
 */

public class MainActivity extends AppCompatActivity {
    /*****常用变量*****/
    public static MainActivity mainActivity;
    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    /*****界面相关控件*****/
    private BottomNavigationBar bottomNavigationBar ;
    private Handler uiHandle;
    public Handler getUiHandle() {
        return uiHandle;
    }

    /*****碎片布局*****/
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DashBoardFragment dashBoardFragment = null;
    private NavigationFragment navigationFragment = null;
    private CarFragment carFragment = null ;
    private  int postion = 0;
    private List<Fragment> fragmentList = new ArrayList<>();


    /*****蓝牙部分*****/
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;

    /*****MQTT部分*****/
    MqttService mqttService ;
    private Gson gson = new Gson();

    /*****数据解析*****/
    DataAnalysed dataAnalysed = new DataAnalysed();
    private List<CarData> carDataList = new ArrayList<>();
    private Map<String,CarData> carDataMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mainActivity = this;
        initActivity();
        initFragment();
        initBottomNavigationBar();
        linkBluetooth();
        linkMqtt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(AppSetting.OBD_SYSTEM_TAG, "Connect request result=" + result);
        }
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
        uiHandle = new UiHandle();
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
//                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_white_32, "导航"))
                .addItem(new BottomNavigationItem(R.drawable.ic_dashboard_white_32, "检测"))
                .addItem(new BottomNavigationItem(R.drawable.ic_car_white_32, "爱车"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_setting_white_32, "设置"))
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                Log.d(AppSetting.OBD_SYSTEM_TAG, "onTabSelected() called with: " + "position = [" + position + "]");
                getMainActivity().postion = position;
                clearFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (position){
//                    case 0:{
//                        if (homeFragment == null) {
//                            homeFragment = HomeFragment.newInstance("Home");
//                            fragmentTransaction.add(R.id.tb, homeFragment);
//                        }
//                        else {
//                            fragmentTransaction.show(homeFragment);
//                        }
//                        fragmentTransaction.commit();
//                        break;
//                    }
                    case 0:{
                        if (navigationFragment == null) {
                            navigationFragment = NavigationFragment.newInstance("Location");
                            fragmentTransaction.add(R.id.tb, navigationFragment);
                        }
                        else {
                            fragmentTransaction.show(navigationFragment);
                        }
                        fragmentTransaction.commit();
                        break;
                    }
                    case 1:{
                        if (dashBoardFragment == null) {
                            dashBoardFragment = DashBoardFragment.newInstance("DashBoard");
                            fragmentTransaction.add(R.id.tb, dashBoardFragment);
                        }
                        else {
                            fragmentTransaction.show(dashBoardFragment);
                        }
                        fragmentTransaction.commit();
                        break;
                    }
                    case 2:{
                        if (carFragment == null) {
                            carFragment = CarFragment.newInstance("MyCar");
                            fragmentTransaction.add(R.id.tb, carFragment);
                        }
                        else
                            fragmentTransaction.show(carFragment);
                        fragmentTransaction.commit();
                        break;
                    }
//                    case 4:{
//                        if (settingFragment == null) {
//                            settingFragment = SettingFragment.newInstance("Setting");
//                            fragmentTransaction.add(R.id.tb, settingFragment);
//                        }
//                        else {
//                            fragmentTransaction.show(settingFragment);
//                        }
//                        fragmentTransaction.commit();
//                        break;
//                    }
                }

            }
            @Override
            public void onTabUnselected(int position) {
                Log.d(AppSetting.OBD_SYSTEM_TAG, "onTabUnselected() called with: " + "position = [" + position + "]");

            }
            @Override
            public void onTabReselected(int position) {
                Log.d(AppSetting.OBD_SYSTEM_TAG, "onTabReselected() called with: " + "position = [" + position + "]");

            }
        });

    }

    /**
     * 初始化fragment管理
     */
    private void initFragment(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
//        homeFragment = HomeFragment.newInstance("Home");
//        fragmentTransaction.add(R.id.tb, homeFragment);
        navigationFragment = NavigationFragment.newInstance("Location");
        fragmentTransaction.add(R.id.tb, navigationFragment);
        fragmentTransaction.commit();
        clearFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(navigationFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private void clearFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        if(carFragment!=null)
        fragmentTransaction.hide(carFragment);
        if(navigationFragment!=null)
        fragmentTransaction.hide(navigationFragment);
        if(dashBoardFragment!=null)
        fragmentTransaction.hide(dashBoardFragment);
        fragmentTransaction.commit();

    }

    private void linkBluetooth(){
        /*****获取要连接的蓝牙名称和地址*****/
        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(AppSetting.BLUETOOTH_DEVICE_ADDRESS);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void linkMqtt(){
        /*****连接MQTT信息*****/
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        bindService(mqttServiceIntent, mqttServiceConnection, BIND_AUTO_CREATE);
        mqttService = MqttService.getMqttService();
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

    private CarData sendCarData;

    private void displayData(String data) {
        if (data != null) {
            byte[][] result;
            result = dataAnalysed.analysisDate(data);
            Log.d(AppSetting.OBD_SYSTEM_TAG,data);
            String command=new String(result[1]);//命令位
            int a=dataAnalysed.hexTodec(result[2]);//A
            int b=dataAnalysed.hexTodec(result[3]);//B
            DecimalFormat df   = new DecimalFormat("######0.00");

            double temp= OBDCProtocol.Mode01_calculate(command,a,b,0);
            String sum=df.format(temp);

            switch (command){
                case "05":{
                    sendCarData = new CarData(command,sum,"°C");
                    Log.d(AppSetting.OBD_SYSTEM_TAG, sendCarData.toString());
                    break;
                }
                case "0C":{
                    sendCarData = new CarData(command,sum,"rpm");
                    Log.d(AppSetting.OBD_SYSTEM_TAG, sendCarData.toString());
                    break;
                }
                case "0D":{
                    sendCarData = new CarData(command,sum,"km/h");
                    Log.d(AppSetting.OBD_SYSTEM_TAG, sendCarData.toString());
                    break;
                }
                case "0F":{
                    sendCarData = new CarData(command,sum,"°C");
                    Log.d(AppSetting.OBD_SYSTEM_TAG, sendCarData.toString());
                    break;
                }
                case "2F":{
                    sendCarData = new CarData(command,sum,"%");
                    Log.d(AppSetting.OBD_SYSTEM_TAG, sendCarData.toString());
                    break;
                }
                case "5C":{
                    sendCarData = new CarData(command,sum,"°C");
                    Log.d(AppSetting.OBD_SYSTEM_TAG, sendCarData.toString());
                    break;
                }
                case "5E":{
                    sendCarData = new CarData(command,sum,"L/h");
                    Log.d(AppSetting.OBD_SYSTEM_TAG, sendCarData.toString());
                    break;
                }
                default:break;
            }

            String json = gson.toJson(sendCarData);
            if(mqttService!=null)
                mqttService.sendOrder("Test",json);
            else
                mqttService = MqttService.getMqttService();

            CarData tempCarData = carDataMap.get(sendCarData.getCommand());
            if(tempCarData!=null)
                tempCarData.setData(sendCarData.getData());
            else {
                carDataList.add(sendCarData);
                carDataMap.put(sendCarData.getCommand(),sendCarData);
            }

            if(postion ==2){
                dashBoardFragment.refeshList();
            }

        }
    }

    public List<CarData> getCarDataList() {
        return carDataList;
    }


    private class UiHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(AppSetting.OBD_SYSTEM_TAG,"getMessage");
            if(msg.what== AppSetting.OBD_SYSTEM_KEY){
                //ui更新
                displayData((String)msg.obj);
            }
            super.handleMessage(msg);
        }
    }

}
