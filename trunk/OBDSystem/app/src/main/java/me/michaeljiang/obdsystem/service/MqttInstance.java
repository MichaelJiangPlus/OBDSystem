package me.michaeljiang.obdsystem.service;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

/**
 * Created by LYQ on 2016/7/5.
 */
public class MqttInstance {
    private static String host="iot.eclipse.org";
    private static String username="";
    private static String password="";
    private static MqttAsyncClient instance=null;
    public static MqttAsyncClient getInstance(){
        return instance;
    }
    public static MqttAsyncClient getInstance(String host, String username, String password){
        if(instance!=null&&host.equals(MqttInstance.host)&&username.equals(MqttInstance.username)&&password.equals(MqttInstance.password)){
            return instance;
        }
        MqttInstance.host=host;
        MqttInstance.username=username;
        MqttInstance.password=password;
        try {
            if(instance!=null&&instance.isConnected()){
                instance.disconnect();
                instance.close();
            }
            instance=new MqttAsyncClient("tcp://"+host, ""+ UUID.randomUUID(),new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
//
        return instance;
    }

    public static MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        if(username!=null&&username.length()>0&&password!=null&&password.length()>0){
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(30);
        }

        return options;
    }
}
