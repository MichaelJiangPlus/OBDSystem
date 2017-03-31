package me.michaeljiang.obddevelopmentassistant.activity.fragment;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import me.michaeljiang.obddevelopmentassistant.R;
import me.michaeljiang.obddevelopmentassistant.activity.DeviceState;
import me.michaeljiang.obddevelopmentassistant.activity.MainActivity;
import me.michaeljiang.obddevelopmentassistant.setting.BluetoothLeServiceSetting;
import me.michaeljiang.obddevelopmentassistant.setting.MqttSeting;

public class SettingFragment extends Fragment {
    private View view;
    private Handler uiHangle;
    private MainActivity mainActivity;
    private Button btn_Search;
    private Button btn_Save;

    private EditText edt_bluetoothName;
    private EditText edt_bluetoothAddress;
    private EditText edt_mqttAddress;
    private EditText edt_mqttPort;
    private EditText edt_UserID;
    private EditText edt_PassWord;
    private EditText edt_DeviceID;

    public static SettingFragment newInstance(String param1) {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    public SettingFragment(){
        mainActivity = MainActivity.getMainActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initFragment();
        btn_Search = (Button)view.findViewById(R.id.btn_resetting);
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeviceState.class);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE).edit();
                editor.putString(BluetoothLeServiceSetting.BLUETOOTH_DEVICE_ADDRESS_TAG,"");
                editor.putString(BluetoothLeServiceSetting.BLUETOOTH_DEVICE_NAME_TAG,"");
                editor.commit();
                BluetoothLeServiceSetting.BLUETOOTH_DEVICE_ADDRESS = "";
                BluetoothLeServiceSetting.BLUETOOTH_DEVICE_NAME = "";
                getActivity().finish();
                startActivity(intent);
            }
        });
        btn_Save = (Button)view.findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在这里修改设置信息
                String mqttHost = edt_mqttAddress.getText().toString();
                String mqttPort = edt_mqttPort.getText().toString();
                String mqttUserName = edt_UserID.getText().toString();
                String mqttPassword = edt_PassWord.getText().toString();
                String mqttDeviceID = edt_DeviceID.getText().toString();

                MqttSeting.Host = mqttHost;
                MqttSeting.Port = mqttPort;
                MqttSeting.DeviceID = mqttDeviceID;
                MqttSeting.UserID = mqttUserName;
                MqttSeting.PassWord = mqttPassword;

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE).edit();
                editor.putString(MqttSeting.MQTT_HOST_ACTION,mqttHost);
                editor.putString(MqttSeting.MQTT_PORT_ACTION,mqttPort);
                editor.putString(MqttSeting.MQTT_USER_NAME_ACTION,mqttUserName);
                editor.putString(MqttSeting.MQTT_PASSWORD_ACTION,mqttPassword);
                editor.putString(MqttSeting.MQTT_DEVICE_ID_ACTION,mqttDeviceID);
                editor.commit();

                mainActivity.reLinkMQTT();
            }
        });
        return view;
    }

    private void initFragment(){
        edt_bluetoothName = (EditText)view.findViewById(R.id.edt_bluetoothName);
        edt_bluetoothAddress = (EditText)view.findViewById(R.id.edt_bluetoothAddress);
        edt_bluetoothName.setKeyListener(null);
        edt_bluetoothAddress.setKeyListener(null);

        edt_mqttAddress = (EditText)view.findViewById(R.id.edt_mqttAddress);
        edt_mqttPort = (EditText)view.findViewById(R.id.edt_mqttPort);
        edt_UserID = (EditText)view.findViewById(R.id.edt_UserID);
        edt_PassWord = (EditText)view.findViewById(R.id.edt_PassWord);
        edt_DeviceID = (EditText)view.findViewById(R.id.edt_DeviceID);

        edt_bluetoothName.setText(BluetoothLeServiceSetting.BLUETOOTH_DEVICE_NAME);
        edt_bluetoothAddress.setText(BluetoothLeServiceSetting.BLUETOOTH_DEVICE_ADDRESS);

        edt_mqttAddress.setText(MqttSeting.Host);
        edt_mqttPort.setText(MqttSeting.Port);
        edt_UserID.setText(MqttSeting.UserID);
        edt_PassWord.setText(MqttSeting.PassWord);
        edt_DeviceID.setText(MqttSeting.DeviceID);

    }


}
