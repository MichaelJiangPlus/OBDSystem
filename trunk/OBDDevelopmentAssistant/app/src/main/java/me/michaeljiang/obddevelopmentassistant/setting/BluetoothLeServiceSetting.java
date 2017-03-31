package me.michaeljiang.obddevelopmentassistant.setting;

/**
 * Created by MichaelJiang on 2017/2/25.
 */

public class BluetoothLeServiceSetting {
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
    public static final String BLUETOOTH_DEVICE_NAME_TAG = "DEVICE_NAME";
    public static final String BLUETOOTH_DEVICE_ADDRESS_TAG = "DEVICE_ADDRESS";

    public static String BLUETOOTH_DEVICE_NAME = "";
    public static String BLUETOOTH_DEVICE_ADDRESS = "";

    /*****蓝牙状态常量*****/
    public static final int BLUETOOTH_STATE_DISCONNECTED = 0;
    public static final int BLUETOOTH_STATE_CONNECTING = 1;
    public static final int BLUETOOTH_STATE_CONNECTED = 2;

    public static final String BLUETOOTH_SERVICE_TAG = "BluetoothLeService";
    public static boolean BlueState = false;

}
