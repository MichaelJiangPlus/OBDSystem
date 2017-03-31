package me.michaeljiang.obddevelopmentassistant.setting;

/**
 * Created by MichaelJiang on 2017/2/18.
 */

public class MqttSeting {
    public static String Host = "10.66.15.150";
    public static String Port = "1883";
    public static String DeviceID = "OBDSystem_MichaelJiang_DevelopmentAssistant";//每一个机自都应该有自己的唯一ID号 应该通过系统获取
    public static String UserID = "";
    public static String PassWord = "";

    /*****MQTT常量*****/
    public final static int MQTT_CONNECTED=3;
    public final static int MQTT_LOST=4;
    public final static int MQTT_FAIL=5;
    public final static int MQTT_RECEIVE=6;

    public final static String MQTT_HOST_ACTION = "MQTT_HOST";
    public final static String MQTT_PORT_ACTION = "MQTT_PORT";
    public final static String MQTT_USER_NAME_ACTION = "MQTT_USERID";
    public final static String MQTT_PASSWORD_ACTION = "MQTT_PASSWORD";
    public final static String MQTT_DEVICE_ID_ACTION = "MQTT_DEVICEID";

    public final static String MQTT_CONNECT_ACTION = "MQTT_CONNECT_ACTION";
    public final static String MQTT_DISCONNECT_ACTION = "MQTT_DISCONNECT_ACTION";
    public final static String MQTT_RECEIVER_ACTION = "MQTT_RECEIVER_ACTION";
    public final static String MQTT_EXTRA_DATA = "MQTT_EXTRA_DATA";


    public static final String MQTT_PUB = "MQTT_PUB";

    public static boolean MqttState = false;
}
