package com.michael.obdsystem.service;

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

import com.michael.obdsystem.util.DataAnalysed;
import com.michael.obdsystem.util.OBDCProtocol;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DecimalFormat;
import java.util.UUID;

public class MqttService extends Service {
    /*****自定义类*****/
    private DataAnalysed dataAnalysed = new DataAnalysed();
    private OBDCProtocol obdcProtocol = new OBDCProtocol();


    /*****MQTT*****/
    private final static int CONNECTED=1;
    private final static int LOST=2;
    private final static int FAIL=3;
    private final static int RECEIVE=4;

    private MqttAsyncClient client=null;
    private Handler handler;

    private final static String Host = "thingworx.zucc.edu.cn";
    private final static String HostPort = "1883";
    private final static String username = "jiangzhanxiang";
    private final static String userpwd = "KLSFDJW9203";
    private String pubTopic ="";
    private String pubInfo ="";
    private String subTopic = "";

    public class LocalBinder extends Binder {
        public MqttService getService() {
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
            msg.what=CONNECTED;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Message msg=new Message();
            msg.what=FAIL;
            handler.sendMessage(msg);
        }
    };

    private void mqttstart() {
        handler  =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==CONNECTED){
                    Log.d("Mqtt","Mqtt连接成功");
                    registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                }else if(msg.what==LOST){
                    Log.d("Mqtt","连接丢失，进行重连");
                    unregisterReceiver(mGattUpdateReceiver);
                    new ConnectThread().start();
                }else if(msg.what==FAIL){
                    Log.d("Mqtt","连接失败");
                }
                super.handleMessage(msg);
            }
        };
        new ConnectThread().start();
    }

    public MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        String strUserName=username;
        String strPassword=userpwd;
        if(strUserName!=null&&strUserName.length()>0&&strPassword!=null&&strPassword.length()>0){
            options.setUserName(strUserName);
            options.setPassword(strPassword.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(30);
        }
        return options;
    }

    private class ConnectThread extends Thread{
        @Override
        public void run(){
            if(client==null){
                try {
                    client=new MqttAsyncClient("tcp://"+Host+":"+HostPort, ""+ UUID.randomUUID(),new MemoryPersistence());
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


    /**
     * 数据处理,在这里处理我们刚获得的数据
     * @param data
     */
    private void displayData(String data) {
        if (data != null) {
            byte[][] result;
            result = dataAnalysed.analysisDate(data);
            String command=new String (result[1]);//命令位
            int a=dataAnalysed.hexTodec(result[2]);//A
            int b=dataAnalysed.hexTodec(result[3]);//B
            DecimalFormat df   = new DecimalFormat("######0.00");
            double temp=obdcProtocol.Mode01_calculate(command,a,b,0);
            String sum=df.format(temp);
            switch (command){
                case "05":{
                    this.sendOrder("0105",String.valueOf(sum));
                    break;
                }
                case "0C":{
                    this.sendOrder("010C",String.valueOf(sum));
                    break;
                }
                case "0D":{
                    this.sendOrder("010D",String.valueOf(sum));
                    break;
                }
                case "0F":{
                    this.sendOrder("010F",String.valueOf(sum));
                    break;
                }
                case "2F":{
                    this.sendOrder("012F",String.valueOf(sum));
                    break;
                }
                case "5C":{
                    this.sendOrder("015C",String.valueOf(sum));
                    break;
                }
                case "5E":{
                    this.sendOrder("015E",String.valueOf(sum));
                    break;
                }
                default:break;
            }
        }
    }
}
