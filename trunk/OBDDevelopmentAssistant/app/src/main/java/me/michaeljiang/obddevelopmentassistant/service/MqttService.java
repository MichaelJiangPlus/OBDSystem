package me.michaeljiang.obddevelopmentassistant.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;

import me.michaeljiang.obddevelopmentassistant.R;
import me.michaeljiang.obddevelopmentassistant.activity.MainActivity;
import me.michaeljiang.obddevelopmentassistant.model.CarData;
import me.michaeljiang.obddevelopmentassistant.setting.AppSetting;
import me.michaeljiang.obddevelopmentassistant.setting.BluetoothLeServiceSetting;
import me.michaeljiang.obddevelopmentassistant.setting.MqttSeting;
import me.michaeljiang.obddevelopmentassistant.util.DataAnalysed;
import me.michaeljiang.obddevelopmentassistant.util.OBDCProtocol;

public class MqttService extends Service {
    /*****自定义类*****/
    private DataAnalysed dataAnalysed = new DataAnalysed();
    private OBDCProtocol obdcProtocol = new OBDCProtocol();
    private MqttAsyncClient client=null;
    private Handler handler;
    private Gson gson = new Gson();
    private  CarData sendCarData = null;
    private boolean isConnect = false;
    String ns = Context.NOTIFICATION_SERVICE;
    private Map<String,String> dataUnit = OBDCProtocol.getMode01_Unit();

    public class LocalBinder extends Binder {
        public MqttService getService() {
            return MqttService.this;
        }
    }

    private final IBinder mBinder =  new LocalBinder();

    public MqttService() {

    }
    private MqttCallback callback=new MqttCallback(){
        @Override
        public void connectionLost(Throwable arg0) {
            Message msg=new Message();
            msg.what=MqttSeting.MQTT_LOST;
            handler.sendMessage(msg);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void messageArrived(String arg0, MqttMessage arg1)
                throws Exception {
            Message msg=new Message();
            msg.what=MqttSeting.MQTT_RECEIVE;
            msg.obj=new String(arg1.getPayload())+"\n";
            handler.sendMessage(msg);
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification.Builder builder1 = new Notification.Builder(MqttService.this);
        builder1.setSmallIcon(R.drawable.ic_launcher); //设置图标
        builder1.setTicker("显示第二个通知");
        builder1.setContentTitle("通知"); //设置标题
        builder1.setContentText("点击查看详细内容"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent myIntent =new Intent (MqttService.this,MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(MqttService.this, 0, myIntent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        ((NotificationManager)getSystemService(ns)).notify(124, notification1); // 通过通知管理器发送通知

        return super.onStartCommand(intent,flags,startId);
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
            msg.what=MqttSeting.MQTT_CONNECTED;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Message msg=new Message();
            msg.what=MqttSeting.MQTT_FAIL;
            handler.sendMessage(msg);
        }
    };

    public void mqttstart() {
        handler  =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==MqttSeting.MQTT_CONNECTED){
                    Log.d("Mqtt","Mqtt连接成功");
                    boardcastUpdate(MqttSeting.MQTT_CONNECT_ACTION);
                    isConnect = true;
                    MqttSeting.MqttState =true;
                    if(AppSetting.APP_MODE.equals(AppSetting.Sub_MODE)){
                        SubMqtt(MqttSeting.DeviceID);
                    }
                    registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                }else if(msg.what==MqttSeting.MQTT_LOST){
                    Log.d("Mqtt","连接丢失，进行重连");
                    isConnect = false;
                    MqttSeting.MqttState =false;
                    unregisterReceiver(mGattUpdateReceiver);
                    boardcastUpdate(MqttSeting.MQTT_DISCONNECT_ACTION);
                    new ConnectThread().start();
                }else if(msg.what==MqttSeting.MQTT_FAIL){
                    isConnect = false;
                    MqttSeting.MqttState =false;
                    boardcastUpdate(MqttSeting.MQTT_DISCONNECT_ACTION);
                    Log.d("Mqtt","连接失败");
                }
                else if(msg.what ==MqttSeting.MQTT_RECEIVE){
                    boardcastUpdate(MqttSeting.MQTT_RECEIVER_ACTION,(String)msg.obj);
                }
                super.handleMessage(msg);
            }
        };
        new ConnectThread().start();
    }
    private void boardcastUpdate(String acton){
        Intent intent = new Intent();  //Itent就是我们要发送的内容
        intent.setAction(acton);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
        sendBroadcast(intent);   //发送广播
    }

    private void boardcastUpdate(String action,String json){
        Intent intent = new Intent();  //Itent就是我们要发送的内容
        intent.setAction(action);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
        intent.putExtra(MqttSeting.MQTT_EXTRA_DATA,json);
        sendBroadcast(intent);   //发送广播
    }

    public MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        String strUserName=MqttSeting.UserID;
        String strPassword=MqttSeting.PassWord;
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
                    client=new MqttAsyncClient("tcp://"+ MqttSeting.Host+":"+MqttSeting.Port, ""+ UUID.randomUUID(),new MemoryPersistence());
                    client.connect(getOptions(),null,mqttActionListener);
                    //如果有Sub的话只需要在写一个callback就好
                    client.setCallback(callback);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean SubMqtt(String inTopic){
        if(client!=null&&client.isConnected()){
            try {
                client.subscribe(MqttSeting.DeviceID,2);
                return true;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean UnSubMqtt(){
        if(client!=null&&client.isConnected()){
            try {
                client.unsubscribe(MqttSeting.DeviceID);
                return true;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean PubMqtt(String topic,String info){
        if(topic.length()>0&&client!=null&&client.isConnected()){
            try {
                client.publish(topic,info.getBytes(),2,false);
                return true;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }else{
            this.UnLinkMqtt();
        }
        return false;
    }

    public void UnLinkMqtt(){
        if(client!=null){
            try {
                client.disconnect();
                client.close();
                client=null;
                isConnect = false;
                MqttSeting.MqttState = false;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        boardcastUpdate(MqttSeting.MQTT_DISCONNECT_ACTION);
        client=null;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeServiceSetting.BLUETOOTH_ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    //通过BroadcastReceiver获取的信息
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeServiceSetting.BLUETOOTH_ACTION_DATA_AVAILABLE.equals(action)) {
                String result = intent.getStringExtra(BluetoothLeServiceSetting.BLUETOOTH_EXTRA_DATA);
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
            String command=new String(result[1]);//命令位
            int a=dataAnalysed.hexTodec(result[2]);//A
            int b=dataAnalysed.hexTodec(result[3]);//B
            DecimalFormat df   = new DecimalFormat("######0.00");
            double temp=obdcProtocol.Mode01_calculate(command,a,b,0);
            String sum=df.format(temp);

            sendCarData = new CarData(command,sum,dataUnit.get(command));
            Log.d(AppSetting.OBD_DEVELOPMENT_ASSISTANT_ACTION, sendCarData.toString());


            Log.d(MqttSeting.MQTT_PUB, "MQTT_PUB : "+sendCarData.toString());
            String json = "null";
            if(sendCarData!=null)
                json = gson.toJson(sendCarData);
            if(MqttSeting.MqttState)
                PubMqtt("OBDSystem_MichaelJiang",json);
        }
    }

}
