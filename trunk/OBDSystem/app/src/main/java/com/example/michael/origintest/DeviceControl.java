package com.example.michael.origintest;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceControl extends Activity {


    private final static String TAG = DeviceControl.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";


    //控件
    private TextView dev_name;
    private TextView dev_addr;
    private TextView dev_connection;
    private ToggleButton connectBtn;
    private Button sendBtn;
    private Button sendBtn1;
    private Button sendBtn2;
    private Button sendBtn3;
    private Button sendBtn4;
    private Button sendBtn5;
    private Button Clear;
    private Button send;
    private EditText editText;

    private TextView mDataField;
    private ExpandableListView mGattServicesList;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private BluetoothGattCharacteristic mWriteCharacteristic;
    private String mDeviceName;
    private String mDeviceAddress;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    byte[] WriteBytes = new byte[20];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);


        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        this.dev_name=(TextView)findViewById(R.id.ctrl_txt2);
        this.dev_addr=(TextView)findViewById(R.id.ctrl_txt21);
        this.dev_connection=(TextView)findViewById(R.id.ctrl_txt4);
        this.mDataField = (TextView) findViewById(R.id.ctrl_txtData);
        this.connectBtn=(ToggleButton)findViewById(R.id.ctrl_btn1);
        this.sendBtn=(Button)findViewById(R.id.ctrl_btn2);
        this.sendBtn1=(Button)findViewById(R.id.ctrl_btn3);
        this.sendBtn2=(Button)findViewById(R.id.ctrl_btn4);
        this.sendBtn3=(Button)findViewById(R.id.ctrl_btn5);
        this.sendBtn4=(Button)findViewById(R.id.ctrl_btn6);
        this.sendBtn5=(Button)findViewById(R.id.ctrl_btn7);
        this.Clear=(Button)findViewById(R.id.ctrl_btn_clear);
        this.Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataField.setText("");
            }
        });
        this.send=(Button)findViewById(R.id.button);
        this.editText=(EditText)findViewById(R.id.editText);


        // Sets up UI references.
        dev_name.setText(mDeviceName);
        dev_addr.setText(mDeviceAddress);
        mGattServicesList = (ExpandableListView) findViewById(R.id.ctrl_lv1);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton toggleButton=(ToggleButton)view;
                if(!toggleButton.isChecked())//连接开启时check为false
                {
                    //连接目标设备
                    try{
                        mBluetoothLeService.connect(mDeviceAddress);
                        dev_connection.setText("Connected");
                    }catch (Exception e)
                    {
                        Log.d(TAG,e.getMessage());
                    }
                }else
                {
                    //关闭设备连接
                    mBluetoothLeService.disconnect();
                    dev_connection.setText("Disconnected");
                }
            }
        });

        this.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendString("0100\r");
            }
        });

        this.sendBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendString("ATE0\r");
            }
        });

        this.sendBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendString("ATDPN\r");
            }
        });
        this.sendBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendString("ATSP00\r");
            }
        });

        this.sendBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendString("0101\r");
            }
        });
        this.sendBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendString("03\r");
            }
        });

        this.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result=editText.getText().toString();
                result=result+'\r';
                sendString(result);
            }
        });

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        this.dev_connection.setText("自动连接设备中");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService.disconnect();
        mBluetoothLeService = null;
    }



/////////////////////////////////////////////////////////////////////////////////////////
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                //Log.e(TAG, "Unable to initialize Bluetooth");
                Toast.makeText(DeviceControl.this,"无法初始化蓝牙设备",Toast.LENGTH_SHORT).show();
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
            //自动连接到目标设备
            connectBtn.setChecked(false);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    ////////////////////////////////////////////////////////////////////////////////////
    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener(){
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    final EditText etHex;
                    final EditText etStr;

                    etHex=new EditText(parent.getContext());
                    etStr=new EditText(parent.getContext());

                    etHex.setSingleLine();
                    etStr.setSingleLine();
                    if (mGattCharacteristics != null) {
                        //final BluetoothGattCharacteristic
                                mWriteCharacteristic =mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = mWriteCharacteristic.getProperties();
                        //如果该特性可写
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0)
                        {
                            LayoutInflater factory = LayoutInflater.from(parent.getContext());
                            final View textEntryView = factory.inflate(R.layout.dialog, null);
                            final EditText editTextName = (EditText) textEntryView.findViewById(R.id.editTextName);
                            final EditText editTextNumEditText = (EditText)textEntryView.findViewById(R.id.editTextNum);
                            AlertDialog.Builder ad1 = new AlertDialog.Builder(parent.getContext());
                            ad1.setTitle("写特性");
                            ad1.setView(textEntryView);
                            ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
                                    byte[] value = new byte[20];
                                    value[0] = (byte) 0x00;
                                    if(editTextName.getText().length() > 0){
                                        //write string

                                        WriteBytes= editTextName.getText().toString().getBytes();
                                    }else if(editTextNumEditText.getText().length() > 0){
                                        try {
                                            WriteBytes = hex2byte(editTextNumEditText.getText().toString().getBytes());
                                        }catch (Exception e)
                                        {Toast.makeText(DeviceControl.this,"长度不为偶数",Toast.LENGTH_SHORT).show();}
                                    }
                                    mWriteCharacteristic.setValue(value[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                    mWriteCharacteristic.setValue(WriteBytes);

                                    mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
                                }
                            });
                            ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {

                                }
                            });
                            ad1.show();

                            }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = mWriteCharacteristic;
                            mBluetoothLeService.setCharacteristicNotification(
                                    mWriteCharacteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
            };


    public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            //throw new IllegalArgumentException("长度不是偶数");
            //edit by me 由原来的抛出异常改为用回车补全
            byte[] b2=new byte[(b.length-1)/2+1];
            for (int i=0;i<b.length-1;i+=2)
            {
                String item=new String(b,i,2);
                b2[i/2]=(byte)Integer.parseInt(item,16);
            }
            b=null;
            return b2;
        }
        else {
            byte[] b2 = new byte[b.length / 2];
            for (int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2);
                // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }
            b = null;
            return b2;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //他是通过BroadcastReceiver获取的信息
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                //invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                //invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //System.out.println(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                String result=intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.i("ajx",result);
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            }
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dev_connection.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
           if (data != null) {
            Log.i("Text",data);
            mDataField.setText(data);
        }
    }

    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        //mDataField.setText("No Data...");
    }

    /////////////////////////////////////////////////////////////////////////
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = "未知服务";
        String unknownCharaString = "未知特征";
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);//写接口
                if(uuid.equals("0000fff1-0000-1000-8000-00805f9b34fb"))
                {

                    mWriteCharacteristic=gattCharacteristic;

                }else if(uuid.equals("0000fff4-0000-1000-8000-00805f9b34fb"))//读回调接口
                {

                    mNotifyCharacteristic=gattCharacteristic;
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, true);

                }

            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }

    ////////////////////////////////////////////////////////
    private static HashMap<String, String> attributes = new HashMap();

    static {
        attributes.put("0000fff2-0000-1000-8000-00805f9b34fb","Device OBDCYX Service");
        attributes.put("0000fff1-0000-1000-8000-00805f9b34fb","Device OBDCYX String");
    }
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

    public void sendString(String command)
    {
        try {
            if (mWriteCharacteristic != null) {
                if (!command.matches("\\r$"))//如果末尾没有回车，自动补全
                    command += "\r";
                if (command.length() > 0) {
                    byte[] WriteBytes;
                    WriteBytes = command.getBytes();
                    System.out.println("command:"+command);
                    //WriteBytes = hex2byte(sendStr.getBytes());
                    mWriteCharacteristic.setValue((byte) 0x00, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                    mWriteCharacteristic.setValue(WriteBytes);
                    mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
                }
            }else
            {
                System.out.println("写特性未实例化");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}



//android.content.ServiceConnection是一个接口，实现（implementate）这个接口有2个方法需要重写（Override）。
// 一个是当Service成功绑定后会被回调的onServiceConnected()方法，
// 另一个是当Service解绑定或者Service被关闭时被回调的onServiceDisconnected()。
//前者（onServiceConnected()方法）会传入一个IBinder对象参数，
// 这个IBinder对象就是在Service的生命周期回调方法的onBind()方法中的返回值