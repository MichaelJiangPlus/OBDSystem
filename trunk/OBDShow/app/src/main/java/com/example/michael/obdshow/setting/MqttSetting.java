package com.example.michael.obdshow.setting;

/**
 * Created by MichaelJiang on 2017/3/16.
 */

public class MqttSetting {
    public static String host = "iot.eclipse.org";
    public static String port = "1883";
    public static String userID = "";
    public static String passWord = "";
    public static String clientID = "OBDSystem_JiangZhanXiang";

    public final static int MQTT_STATE_CONNECTED=1;
    public final static int MQTT_STATE_LOST=2;
    public final static int MQTT_STATE_FAIL=3;
    public final static int MQTT_STATE_RECEIVE=4;
}
