package me.michaeljiang.obddevelopmentassistant.activity.fragment;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.michaeljiang.obddevelopmentassistant.R;
import me.michaeljiang.obddevelopmentassistant.activity.MainActivity;
import me.michaeljiang.obddevelopmentassistant.activity.adapter.CarDataAdapter;
import me.michaeljiang.obddevelopmentassistant.model.CarData;
import me.michaeljiang.obddevelopmentassistant.setting.AppSetting;
import me.michaeljiang.obddevelopmentassistant.setting.BluetoothLeServiceSetting;
import me.michaeljiang.obddevelopmentassistant.setting.MqttSeting;
import me.michaeljiang.obddevelopmentassistant.util.DataAnalysed;
import me.michaeljiang.obddevelopmentassistant.util.OBDCProtocol;


public class DashBoardFragment extends ListFragment {
    private View view;
    private Handler uiHangle;
    private MainActivity mainActivity ;
    private List<CarData> carDataList = new ArrayList<>();
    private CarDataAdapter carDataAdapter ;
    private DataAnalysed dataAnalysed = new DataAnalysed();
    private OBDCProtocol obdcProtocol = new OBDCProtocol();
    private CarData sendCarData;
    private Map<String,CarData> carDataMap = new HashMap<>();
    private Map<String,String> dataUnit = OBDCProtocol.getMode01_Unit();
    private Gson gson = new Gson();
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }


    public static DashBoardFragment newInstance(String param1) {
        DashBoardFragment fragment = new DashBoardFragment();
        return fragment;
    }

    public DashBoardFragment(){
        mainActivity = MainActivity.getMainActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initFragment();
        initList();
        return view;
    }

    private void initFragment(){
        mainActivity.linkBluetooth();
        mainActivity.linkMQTT();
    }

    private void initList(){
        carDataAdapter = new CarDataAdapter(getActivity(),carDataList);
        setListAdapter(carDataAdapter);
    }

    public void refeshList(){
        carDataAdapter.notifyDataSetChanged();
    }

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
            if(sendCarData.getCommand() ==null){
                Log.d("OBDSystem","数据非法");
                return;
            }

            Log.d(AppSetting.OBD_DEVELOPMENT_ASSISTANT_ACTION, sendCarData.toString());

            CarData tempCardata = carDataMap.get(command);
            if(tempCardata!=null){
                tempCardata.setData(sendCarData.getData());
            }
            else if(sendCarData !=null){
                carDataMap.put(command,sendCarData);
                carDataList.add(sendCarData);
            }
            refeshList();
        }
    }
        private void MessageReceiver(String json){
            Log.d("Receiver",json);
            try{
                sendCarData = gson.fromJson(json,CarData.class);
                Log.d("Receiver",sendCarData.toString());
                CarData tempCardata = carDataMap.get(sendCarData.getCommand());
                if(tempCardata!=null){
                    tempCardata.setData(sendCarData.getData());
                }
                else if(sendCarData !=null){
                    carDataMap.put(sendCarData.getCommand(),sendCarData);
                    carDataList.add(sendCarData);
                }
                refeshList();
            }
            catch (Exception e){
                Log.d("Json","Error");
            }

        }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeServiceSetting.BLUETOOTH_ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeServiceSetting.BLUETOOTH_ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeServiceSetting.BLUETOOTH_ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(MqttSeting.MQTT_DISCONNECT_ACTION);
        intentFilter.addAction(MqttSeting.MQTT_CONNECT_ACTION);
        intentFilter.addAction(MqttSeting.MQTT_RECEIVER_ACTION);
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
            else if(BluetoothLeServiceSetting.BLUETOOTH_ACTION_GATT_DISCONNECTED.equals(action)){
                Log.d("BlueTooth","DisConnect");
            }
            else if(BluetoothLeServiceSetting.BLUETOOTH_ACTION_GATT_CONNECTED.equals(action)){
                Log.d("BlueTooth","Connect");
            }
            else if(MqttSeting.MQTT_DISCONNECT_ACTION.equals(action)){
                Log.d("MQTT","DisConnect");
            }
            else if(MqttSeting.MQTT_CONNECT_ACTION.equals(action)){
                Log.d("MQTT","Connect");
            }
            else if(MqttSeting.MQTT_RECEIVER_ACTION.equals(action)){
                MessageReceiver(intent.getStringExtra(MqttSeting.MQTT_EXTRA_DATA));
            }
        }
    };

}
