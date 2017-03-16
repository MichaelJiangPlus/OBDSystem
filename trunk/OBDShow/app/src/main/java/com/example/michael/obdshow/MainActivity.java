package com.example.michael.obdshow;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.obdshow.view.DialChart03View;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{

    private final static int CONNECTED=1;
    private final static int LOST=2;
    private final static int FAIL=3;
    private final static int RECEIVE=4;


    private final static int CoolantTemperature  = 11;
    private final static int EngineTurn  = 12;
    private final static int speed  = 13;
    private final static int TankTemperature  = 14;
    private final static int OilSurplus  = 15;
    private final static int OilUse  = 16;




//    private EditText ipAddress,port,username,password,pubTopic,pubInfo,subTopic;
    private TextView subInfo;
    private Button btnSub;
    private MqttAsyncClient client=null;
    private Handler handler;
    private DialChart03View myView;


    private final static String Host = "10.66.15.150";
    private final static String HostPort = "1883";
    private final static String username = "jiangzhanxiang_OBDSystem";
    private final static String userpwd = "";
    private String pubTopic;
    private String pubInfo;
    private String subTopic = "010D";

    /****控件变量****/
    private TextView txt_EngineTurn;
    private TextView txt_OilSurplus;
    private TextView txt_CoolantTemperature;
    private TextView txt_TankTemperature;
    private TextView txt_OilUse;


    /*****自定义全局变量*****/
    private String info_EngineTurn;
    private String info_OilSurplus;
    private String info_CoolantTemperature;
    private String info_TankTemperature;
    private String info_OilUse;


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


    private MqttCallback callback=new MqttCallback(){

        @Override
        public void connectionLost(Throwable arg0) {
            Message msg=new Message();
            msg.what=LOST;
            handler.sendMessage(msg);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message)
                throws Exception {
            Message msg=new Message();

            switch (topic){
                case "0105":{
                    msg.what = CoolantTemperature;
                    msg.obj=new String(message.getPayload())+"\n";
                    break;
                }
                case "010C":{
                    msg.what = EngineTurn;
                    msg.obj=new String(message.getPayload())+"\n";
                    break;
                }
                case "010D":{
                    msg.what = speed;
                    msg.obj=new String(message.getPayload())+"\n";
                    break;
                }
                case "010F":{
                    msg.what = TankTemperature;
                    msg.obj=new String(message.getPayload())+"\n";
                    break;
                }
                case "012F":{
                    msg.what = OilSurplus;
                    msg.obj=new String(message.getPayload())+"\n";
                    break;
                }
                case "015E":{
                    msg.what = OilUse;
                    msg.obj=new String(message.getPayload())+"\n";
                    break;
                }
                default:{
                    msg.what=RECEIVE;
                    msg.obj=new String(message.getPayload())+"\n";
                    break;
                }
            }
            handler.sendMessage(msg);
        }
    };

    private void init(){
        txt_EngineTurn=(TextView)findViewById(R.id.txt_EngineTurn);
        txt_OilSurplus=(TextView)findViewById(R.id.txt_OilSurplus);
        txt_CoolantTemperature=(TextView)findViewById(R.id.txt_CoolantTemperature);
        txt_TankTemperature=(TextView)findViewById(R.id.txt_TankTemperature);
        txt_OilUse=(TextView)findViewById(R.id.txt_OilUse);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/ttt.ttf");
        txt_EngineTurn.setTypeface(typeface);
        txt_OilSurplus.setTypeface(typeface);
        txt_CoolantTemperature.setTypeface(typeface);
        txt_TankTemperature.setTypeface(typeface);
        txt_OilUse.setTypeface(typeface);


        info_CoolantTemperature =  txt_CoolantTemperature.getText().toString();
        info_EngineTurn  = txt_EngineTurn.getText().toString();
        info_OilSurplus = txt_OilSurplus.getText().toString();
        info_OilUse = txt_OilUse.getText().toString();
        info_TankTemperature = txt_TankTemperature.getText().toString();

        myView = (DialChart03View)findViewById(R.id.frame);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        init();

//        subInfo=(TextView)findViewById(R.id.subInfo);
        btnSub=(Button)findViewById(R.id.btnSub);
        btnSub.setOnClickListener(this);



        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==CONNECTED){
                    Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                }else if(msg.what==LOST){
                    Toast.makeText(MainActivity.this,"连接丢失，进行重连",Toast.LENGTH_SHORT).show();
                    new ConnectThread().start();
                }else if(msg.what==FAIL){
                    Toast.makeText(MainActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                }else {

                    String message = (String)msg.obj;
                    switch (msg.what){
                        case CoolantTemperature:{
                            try{
                                txt_CoolantTemperature.setText(info_CoolantTemperature+Double.valueOf(message)+"°C");
                            }
                            catch (Exception e){
                                Log.d("Message","CoolantTemperatureChange");
                            }
                            break;
                        }
                        case EngineTurn:{
                            try{
                                txt_EngineTurn.setText(info_EngineTurn+Double.valueOf(message)+"rpm");
                            }
                            catch (Exception e){
                                Log.d("Message","EngineTurnChange");
                            }
                            break;
                        }
                        case speed:{
                            try{

                                try {
                                    float pf = Float.valueOf(message) / 236.5f;
                                    myView.setCurrentStatus(pf);//旋转角度
                                    myView.invalidate();//显示值的变化
                                }
                                catch (Exception e){
                                    Log.d("Message","speedChange");
                                }
                            }
                            catch (Exception e){
                                Log.d("Message","EngineTurnChange");
                            }

                            break;
                        }
                        case TankTemperature:{
                            try{
                                txt_TankTemperature.setText(info_TankTemperature+Double.valueOf(message)+"°C");
                            }
                            catch (Exception e){
                                Log.d("Message","TankTemperatureChange");
                            }
                            break;
                        }
                        case OilSurplus:{
                            try{
                                txt_OilSurplus.setText(info_OilSurplus+Double.valueOf(message)+"%");
                            }
                            catch (Exception e){
                                Log.d("Message","OilSurplusChange");
                            }
                            break;
                        }
                        case OilUse:{
                            try{
                                txt_OilUse.setText(info_OilUse+Double.valueOf(message)+"L/h");
                            }
                            catch (Exception e){
                                Log.d("Message","OilUseChange");
                            }

                            break;
                        }
                        default:break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        new ConnectThread().start();
        Sub();
    }

    @Override
    public void onResume(){
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
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

    private void Sub(){
        if(client!=null&&client.isConnected()){
            try {
                //设置订阅的Topic
                client.subscribe("0105",2);
                client.subscribe("010C",2);
                client.subscribe("010D",2);
                client.subscribe("010F",2);
                client.subscribe("012F",2);
                client.subscribe("015C",2);
                client.subscribe("015E",2);

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private void UnSub(){
        if(client!=null&&client.isConnected()){
            try {
                client.unsubscribe("0105");
                client.unsubscribe("010C");
                client.unsubscribe("010D");
                client.unsubscribe("010F");
                client.unsubscribe("012F");
                client.unsubscribe("015C");
                client.unsubscribe("015E");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private void Close(){
        if(client!=null){
            try {
                client.disconnect();
                client.close();
                client=null;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
//        closed();
    }

    private void Pub(){
        if(pubTopic.length()>0&&client!=null&&client.isConnected()){
            try {
                client.publish(pubTopic.toString(),pubInfo.toString().getBytes(),2,false);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,"发送失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btnSub){
            this.Sub();
            btnSub.setVisibility(View.INVISIBLE);
        }
    }




    private class ConnectThread extends Thread{
        @Override
        public void run(){
            if(client==null){
                try {
                    client=new MqttAsyncClient("tcp://"+Host+":"+HostPort, ""+ UUID.randomUUID(),new MemoryPersistence());
                    client.connect(getOptions(),null,mqttActionListener);
                    client.setCallback(callback);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
