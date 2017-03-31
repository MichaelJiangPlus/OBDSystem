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
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.obdshow.model.CarData;
import com.example.michael.obdshow.service.MyMqtt;
import com.example.michael.obdshow.setting.MqttSetting;
import com.example.michael.obdshow.view.DialChart03View;
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

import java.util.UUID;

public class MainActivity extends AppCompatActivity{
    private Handler uiHandle;
    private DialChart03View myView;

    private Gson gson = new Gson();
    private MyMqtt mqtt;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        init();

        initMqtt();
    }

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

    private void initMqtt(){
        uiHandle = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== MqttSetting.MQTT_STATE_CONNECTED){
//                    Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                    mqtt.setConnect(true);
                    mqtt.subTopic("OBDSystem_MichaelJiang");
                }else if(msg.what==MqttSetting.MQTT_STATE_LOST){
//                    Toast.makeText(MainActivity.this,"连接丢失，进行重连",Toast.LENGTH_SHORT).show();
                    mqtt.setConnect(false);
                }else if(msg.what==MqttSetting.MQTT_STATE_FAIL){
//                    Toast.makeText(MainActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                    mqtt.setConnect(false);
                }else if(msg.what==MqttSetting.MQTT_STATE_RECEIVE){
//                    Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                    Log.d("OBDSystem",(String)msg.obj);
                    receiverMqttData(msg);
                }
                super.handleMessage(msg);
            }
        };
        mqtt = new MyMqtt(uiHandle);
        mqtt.connectMqtt();
    }
    private void receiverMqttData(Message msg){
        String message = (String)msg.obj;
        CarData carData = null;
        try{
            carData = gson.fromJson(message,CarData.class);
            if(carData == null){
                Log.d("OBDSystem","数据非法");
                return;
            }
            switch (carData.getCommand()){
                case "05":{
                    //0105 引擎冷却液温度
                    txt_CoolantTemperature.setText(info_CoolantTemperature+carData.getData()+"°C");
                    break;
                }
                case "0C":{
                    //010C 引擎转速
                    txt_EngineTurn.setText(info_EngineTurn+carData.getData()+"rpm");
                    break;
                }
                case "0D":{
                    //010D 车辆速度
                    try {
                        float pf = Float.valueOf(carData.getData()) / 236.5f;
                        myView.setCurrentStatus(pf);//旋转角度
                        myView.invalidate();//显示值的变化
                    }
                    catch (Exception e){
                        Log.d("OBDSystem","speedChangeError");
                    }
                }
                break;
                case "0F":{
                    //010F 邮箱空气温度
                    txt_TankTemperature.setText(info_TankTemperature+carData.getData()+"°C");
                    break;
                }
                case "2F":{
                    //012F 邮箱液位情况
                    txt_OilSurplus.setText(info_OilSurplus+carData.getData()+"%");
                    break;
                }
                case "5E":{
                    //015E 油量消耗速率
                    txt_OilUse.setText(info_OilUse+carData.getData()+"L/h");
                    break;
                }
                default:break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onResume(){
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }





}
