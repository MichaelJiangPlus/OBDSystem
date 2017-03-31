package com.example.michael.obdshow.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.michael.obdshow.setting.MqttSetting;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;



/**
 * Created by MichaelJiang on 2017/3/16.
 */

public class MyMqtt {
    private String Tag = "MyMqtt";
    public boolean isConnect = false;
    private Handler fatherHandle;
    private MqttAsyncClient mqttClient=null;

    public MyMqtt(Handler uiHandle){
        fatherHandle = uiHandle;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public MyMqtt(){
        fatherHandle =new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== MqttSetting.MQTT_STATE_CONNECTED){
                    isConnect = true;
                    Log.d(Tag,"连接成功");
                }else if(msg.what==MqttSetting.MQTT_STATE_LOST){
                    isConnect = false;
                    Log.d(Tag,"连接丢失，进行重连");
                }else if(msg.what==MqttSetting.MQTT_STATE_FAIL){
                    isConnect = false;
                    Log.d(Tag,"连接成功");
                }else if(msg.what==MqttSetting.MQTT_STATE_RECEIVE){
                    Log.d(Tag,(String)msg.obj);
                }
                super.handleMessage(msg);
            }
        };

    }

    public void connectMqtt(){
        try {
            mqttClient=new MqttAsyncClient("tcp://"+MqttSetting.host,"ClientID"+MqttSetting.clientID,new MemoryPersistence());
            mqttClient.connect(getOptions(),null,mqttActionListener);
            mqttClient.setCallback(callback);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void reStartMqtt(){
        disConnectMqtt();
        connectMqtt();
    }

    public void disConnectMqtt(){
        try {
            mqttClient.disconnect();
            mqttClient = null;
            isConnect = false;
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void pubMsg(String Topic, String Msg){
        if(!isConnect){
            Log.d(Tag,"Mqtt连接未打开");
            return;
        }
        try {
            /** Topic,Msg,Qos,Retained**/
            mqttClient.publish(Topic,Msg.getBytes(),1,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subTopic(String Topic){
        if(!isConnect){
            Log.d(Tag,"Mqtt连接未打开");
            return;
        }
        try {
            mqttClient.subscribe(Topic,2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);//重连不保持状态
        if(MqttSetting.userID!=null&&MqttSetting.userID.length()>0&&MqttSetting.passWord!=null&&MqttSetting.passWord.length()>0){
            options.setUserName(MqttSetting.userID);//设置服务器账号密码
            options.setPassword(MqttSetting.passWord.toCharArray());
        }
        options.setConnectionTimeout(10);//设置连接超时时间
        options.setKeepAliveInterval(30);//设置保持活动时间，超过时间没有消息收发将会触发ping消息确认
        return options;
    }

    private IMqttActionListener mqttActionListener=new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            //连接成功处理
            Message msg=new Message();
            msg.what=MqttSetting.MQTT_STATE_CONNECTED;
            fatherHandle.sendMessage(msg);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            exception.printStackTrace();
            //连接失败处理
            Message msg=new Message();
            msg.what=MqttSetting.MQTT_STATE_FAIL;
            fatherHandle.sendMessage(msg);
            new Thread(){
                @Override
                public void run(){
                    try {
                        sleep(300);
                        connectMqtt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    };

    private MqttCallback callback=new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            //连接断开
            Message msg=new Message();
            msg.what=MqttSetting.MQTT_STATE_LOST;
            fatherHandle.sendMessage(msg);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            //消息到达
            Message msg=new Message();
            msg.what=MqttSetting.MQTT_STATE_RECEIVE;
            msg.obj=new String(message.getPayload())+"\n";
            fatherHandle.sendMessage(msg);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            //消息发送完成
        }
    };

}
