package com.michael.obdsystem;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.obdsystem.model.Point;
import com.michael.obdsystem.service.BluetoothLeService;
import com.michael.obdsystem.service.Send;
import com.michael.obdsystem.util.CoordinateConversion;
import com.michael.obdsystem.util.DataAnalysed;
import com.michael.obdsystem.util.OBDCProtocol;
import com.michael.obdsystem.view.DashBoard;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;


public class DeviceControl extends Activity {
    /*****自定义类*****/
    private DataAnalysed dataAnalysed;
    private OBDCProtocol obdcProtocol;
    private static DeviceControl deviceControl = null;
    private final static String TAG = DeviceControl.class.getSimpleName();

    /*****蓝牙部分*****/
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceName;
    private String mDeviceAddress;
    private String uuid="";



    /*****界面相关控件*****/
    private String info_EngineTurn;
    private String info_OilSurplus;
    private String info_CoolantTemperature;
    private String info_TankTemperature;
    private String info_OilUse;
    private TextView txt_EngineTurn;
    private TextView txt_OilSurplus;
    private TextView txt_CoolantTemperature;
    private TextView txt_TankTemperature;
    private TextView txt_OilUse;
    private DashBoard myview;

    public static DeviceControl getDeviceControl() {
        return deviceControl;
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

        myview=(DashBoard)findViewById(R.id.frame);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 主页面初始化
         */
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_design);

        dataAnalysed = new DataAnalysed();
        obdcProtocol = new OBDCProtocol();
        deviceControl = this;

        TelephonyManager tm = (TelephonyManager) deviceControl.getSystemService(TELEPHONY_SERVICE);
        uuid=tm.getSubscriberId();
        init();

    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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
//            Toast.makeText(DeviceControl.this,command,Toast.LENGTH_LONG).show();
            switch (command){
                case "05":{
                    txt_CoolantTemperature.setText(info_CoolantTemperature+sum+"°C");
                    break;
                }
                case "0C":{
                    txt_EngineTurn.setText(info_EngineTurn+sum+"rpm");
                    break;
                }
                case "0D":{
//                    txt_speed.setText(sum+"Km/h");
                    float pf = Float.valueOf(sum) / 236.5f;
                    myview.setCurrentStatus(pf);//旋转角度
                    myview.invalidate();//显示值的变化
                    break;
                }
                case "0F":{
                    txt_TankTemperature.setText(info_TankTemperature+sum+"°C");
                    break;
                }
                case "2F":{
                    txt_OilSurplus.setText(info_OilSurplus+sum+"%");
                    break;
                }
                case "5C":{
//                    txt_EnginTemperature.setText(sum+"°C");
                    break;
                }
                case "5E":{
                    txt_OilUse.setText(info_OilUse+sum+"L/h");
                    break;
                }
                default:break;
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

                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
}


