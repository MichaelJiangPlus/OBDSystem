package me.michaeljiang.obdsystem.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DecimalFormat;
import java.util.UUID;

import me.michaeljiang.obdsystem.util.AppSetting;
import me.michaeljiang.obdsystem.util.DataAnalysed;
import me.michaeljiang.obdsystem.util.OBDCProtocol;

/**
 * 2017/02/23  需要补全MQTT订阅与发送，现在已经取消了广播
 * 2017/02/23  目前已经解决了MQTT发送
 */
public class MqttService extends Service {

    private Handler handler;
    private MqttAsyncClient client=null;
    private static MqttService mqttService = null;
    public static MqttService getMqttService() {
        return mqttService;
    }


    public class LocalBinder extends Binder {
        public MqttService getService() {
            mqttService = MqttService.this;
            return MqttService.this;
        }
    }

    private final IBinder mBinder =  new LocalBinder();

    public MqttService() {

    }

    @Override
    public void onCreate(){
        this.mqttstart();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    private IMqttActionListener mqttActionListener=new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Message msg=new Message();
            msg.what= AppSetting.MQTT_CONNECTED;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Message msg=new Message();
            msg.what=AppSetting.MQTT_FAIL;
            handler.sendMessage(msg);
        }
    };

    private void mqttstart() {
        handler  =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==AppSetting.MQTT_CONNECTED){
                    Log.d("Mqtt","Mqtt连接成功");
                }else if(msg.what==AppSetting.MQTT_LOST){
                    Log.d("Mqtt","连接丢失，进行重连");
                    new ConnectThread().start();
                }else if(msg.what==AppSetting.MQTT_FAIL){
                    Log.d("Mqtt","连接失败");
                }
                super.handleMessage(msg);
            }
        };
        new ConnectThread().start();
    }

    private MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        String strUserName=AppSetting.MQTT_USERID;
        String strPassword=AppSetting.MQTT_PASSWORD;
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
                    client=new MqttAsyncClient("tcp://"+AppSetting.MQTT_HOST+":"+AppSetting.MQTT_PORT, ""+ UUID.randomUUID(),new MemoryPersistence());
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

}
