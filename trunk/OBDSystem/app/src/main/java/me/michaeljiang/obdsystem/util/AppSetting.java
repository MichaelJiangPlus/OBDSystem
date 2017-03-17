package me.michaeljiang.obdsystem.util;

/**
 * Created by MichaelJiang on 2017/2/23.
 */

public class AppSetting {
    /*****MQTT常量*****/
    public final static String MQTT_HOST = "192.168.1.249";
    public final static String MQTT_PORT = "1883";
    public final static String MQTT_USERID = "";
    public final static String MQTT_PASSWORD = "";


    /*****TAG常量*****/
    public static final String LOG_CAR_FRAGMENT_TAG = "CarFragment";
    public static final String LOG_DASHBOARD_FRAGMENT_TAG = "DashBoardFragment";
    public static final String LOG_NAVIGATION_FRAGMENT_TAG = "NavigationFragment";
    public static final String LOG_HOME_FRAGMENT_TAG = "HomeFragment";
    public static final String LOG_SETTING_FRAGMENT_TAG = "SettingFragment";
    public static final String BLUETOOTH_SERVICE_TAG = "BluetoothLeService";
    public static final String OBD_SYSTEM_TAG = "OBDSystem";


    /*****蓝牙状态常量*****/
    public static final int BLUETOOTH_STATE_DISCONNECTED = 0;
    public static final int BLUETOOTH_STATE_CONNECTING = 1;
    public static final int BLUETOOTH_STATE_CONNECTED = 2;

    /*****MQTT常量*****/
    public final static int MQTT_CONNECTED=3;
    public final static int MQTT_LOST=4;
    public final static int MQTT_FAIL=5;
    public final static int MQTT_RECEIVE=6;

    /*****Message常量*****/
    public static final int MESSAGE_CAR_FRAGMENT_KEY = 7;
    public static final int MESSAGE_DASHBOARD_FRAGMENT_KEY = 8;
    public static final int MESSAGE_NAVIGATION_FRAGMENT_KEY = 9;
    public static final int MESSAGE_HOME_FRAGMENT_KEY = 10;
    public static final int MESSAGE_SETTING_FRAGMENT_KEY = 11;
    public static final int OBD_SYSTEM_KEY = 12;

    /*****蓝牙广播常量*****/
    public final static String BLUETOOTH_ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String BLUETOOTH_ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String BLUETOOTH_ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String BLUETOOTH_ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String BLUETOOTH_EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    /*****蓝牙部分*****/
    public static final String BLUETOOTH_DEVICE_NAME = "DEVICE_NAME";
    public static final String BLUETOOTH_DEVICE_ADDRESS = "DEVICE_ADDRESS";


}
