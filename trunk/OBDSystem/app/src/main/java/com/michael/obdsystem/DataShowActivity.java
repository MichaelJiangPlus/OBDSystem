package com.michael.obdsystem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.obdsystem.model.Data;
import com.michael.obdsystem.model.DataAdapter;
import com.michael.obdsystem.service.BluetoothLeService;
import com.michael.obdsystem.util.DataAnalysed;
import com.michael.obdsystem.util.OBDCProtocol;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataShowActivity extends Activity {
    /***** Android 控件部分*****/
    private TextView textView ;
    private ListView listView;
    private List datalist = new ArrayList();
    private HashMap<String,Data>  myMap= new HashMap();
    String[] command = {"0105","010C","010D","010F","012F","015C","015E"};
    String[] info = {"引擎冷却液温度","引擎转速","车辆速度","油箱空气温度","油量液位情况","引擎油温","引擎油量消耗速率"};
    String[] data = {"-40","0","0","-40","0","-40","0"};

    /***** Android Util*****/
    DataAnalysed dataAnalysed = new DataAnalysed();
    DataAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_data_show);
        textView = (TextView)findViewById(R.id.txt_temp);
        listView = (ListView)findViewById(R.id.listview_data);
        getData();
        adapter = new DataAdapter(DataShowActivity.this,R.layout.listitem_data,datalist);
        listView.setAdapter(adapter);

    }

    private void getData(){
        //数据种类
        //{"0105","010C","010D","010F","012F","015C","015E"};
        Data temp ;
        for (int i = 0 ;i < command.length ;i++){
            temp = new Data();
            temp.setCommand(command[i]);
            temp.setInfo("  "+info[i]);
            temp.setData(data[i]);
            datalist.add(temp);
            myMap.put(command[i],temp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }


    private void displayData(String data) {
        if (data != null) {
            textView.setText(data);
            byte[][] result;
            result = dataAnalysed.analysisDate(data);
            String command=new String(result[1]);//命令位
            int a=dataAnalysed.hexTodec(result[2]);//A
            int b=dataAnalysed.hexTodec(result[3]);//B
            DecimalFormat df   = new DecimalFormat("######0.00");

            double temp= OBDCProtocol.Mode01_calculate(command,a,b,0);

            String sum=df.format(temp);
            switch (command){
                case "05":{
                    myMap.get("0105").setData(sum+"°C");
                    break;
                }
                case "0C":{
                    myMap.get("010C").setData(sum+"rpm");
                    break;
                }
                case "0D":{
                    myMap.get("010D").setData(sum+"km/h");
                    break;
                }
                case "0F":{
                    myMap.get("010F").setData(sum+"°C");
                    break;
                }
                case "2F":{
                    myMap.get("012F").setData(sum+"%");
                    break;
                }
                case "5C":{
                    myMap.get("015C").setData(sum+"°C");
                    break;
                }
                case "5E":{
                    myMap.get("015E").setData(sum+"L/h");
                    break;
                }
                default:break;
            }
            adapter.notifyDataSetChanged();
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
}

