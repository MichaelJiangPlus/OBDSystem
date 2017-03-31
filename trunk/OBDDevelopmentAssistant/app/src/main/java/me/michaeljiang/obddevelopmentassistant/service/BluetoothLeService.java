package me.michaeljiang.obddevelopmentassistant.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.michaeljiang.obddevelopmentassistant.R;
import me.michaeljiang.obddevelopmentassistant.activity.MainActivity;
import me.michaeljiang.obddevelopmentassistant.setting.BluetoothLeServiceSetting;
import me.michaeljiang.obddevelopmentassistant.util.OBDCProtocol;


public class BluetoothLeService extends Service {
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = BluetoothLeServiceSetting.BLUETOOTH_STATE_CONNECTED;
//    private Handler uiHandle;
//    private String last_str="";

    //当前状态标志
    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_OBDC_DEVICE = UUID.fromString(OBDCProtocol.UUID_OBDC_DEVICE_CHA1);


    public static BluetoothLeService getmBluetoothLeService() {
        return mBluetoothLeService;
    }

    public void  setmBluetoothLeService(BluetoothLeService mBluetoothLeService) {
        this.mBluetoothLeService = mBluetoothLeService;
    }

    private static BluetoothLeService mBluetoothLeService;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private final IBinder mBinder = new LocalBinder();


    public boolean initialize() {
        //获取蓝牙管理
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Toast.makeText(this, "BluetoothManager获取失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //获取适配器
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "BLE Adapter获取失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public BluetoothLeService() {
        mBluetoothLeService = this;
//        uiHandle = MainActivity.getMainActivity().getUiHandle();
    }
    String ns = Context.NOTIFICATION_SERVICE;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification.Builder builder1 = new Notification.Builder(BluetoothLeService.this);
        builder1.setSmallIcon(R.drawable.ic_launcher); //设置图标
        builder1.setTicker("显示第二个通知");
        builder1.setContentTitle("通知"); //设置标题
        builder1.setContentText("点击查看详细内容"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent myIntent =new Intent (BluetoothLeService.this,MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(BluetoothLeService.this, 0, myIntent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        ((NotificationManager)getSystemService(ns)).notify(124, notification1); // 通过通知管理器发送通知

        return super.onStartCommand(intent,flags,startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //结束使用设备后保证使用BluetoothGatt.close()使设备和Gatt断开来保证资源完全释放
        //在本例中close会在UI（DeviceControl）和服务断开时调用
        //close（）->mBluetoothGatt.close()
        close();
        return super.onUnbind(intent);
    }

    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
        //释放Gatt
    }

    public boolean connect(final String address) {
        //确认已经获取到适配器并且地址不为空
        if (mBluetoothAdapter == null || address == null) {
            Log.w(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = BluetoothLeServiceSetting.BLUETOOTH_STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        //通过设备地址获取设备
        if (device == null) {
            Log.w(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        //连接Gatt
        Log.d(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = BluetoothLeServiceSetting.BLUETOOTH_STATE_CONNECTING;
        return true;
    }

    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        //断开连接
    }

    //////////////////////////////////////////////////////////////////////////////////////

    private void broadcastUpdate(final String action){
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private String last_str="";
    //从特征中分析返回的数据并表示为action后进行广播
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        //else if (UUID_OBDC_DEVICE.equals(characteristic.getUuid())){
        //获取到目标设备OBDC发送的特征
        //}
        // For all other profiles, writes the data formatted in HEX.
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
                /*
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                System.out.println("格式化后接受的数据"+stringBuilder.toString());
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
                */
            try {
                String get_str = new String(data, "ASCII");
                String get_str_trim=get_str.trim();
                last_str+=get_str;
                if(get_str_trim.contains(">")){
                    //System.out.println("last_str"+last_str);
                    intent.putExtra(BluetoothLeServiceSetting.BLUETOOTH_EXTRA_DATA,last_str);
                    sendBroadcast(intent);
                    last_str="";
                }

                //intent.setAction(ACTION_DATA_AVAILABLE);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            //连接状态发生变更
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState = BluetoothLeServiceSetting.BLUETOOTH_STATE_CONNECTED;
                broadcastUpdate(BluetoothLeServiceSetting.BLUETOOTH_ACTION_GATT_CONNECTED);
                Log.i(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "Connected to GATT server.");
                Log.i(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());
                BluetoothLeServiceSetting.BlueState = true;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = BluetoothLeServiceSetting.BLUETOOTH_STATE_DISCONNECTED;
                broadcastUpdate(BluetoothLeServiceSetting.BLUETOOTH_ACTION_GATT_DISCONNECTED);
                Log.i(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "Disconnected from GATT server.");
                BluetoothLeServiceSetting.BlueState = false;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            //发现服务的广播
            if (status == BluetoothGatt.GATT_SUCCESS) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else {
                Log.w(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(BluetoothLeServiceSetting.BLUETOOTH_ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(BluetoothLeServiceSetting.BLUETOOTH_ACTION_DATA_AVAILABLE, characteristic);

        }
        @Override
        public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){

        }
    };

    public int getmConnectionState() {
        return mConnectionState;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    ////////////////////////////////////////////////////////////////////

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(BluetoothLeServiceSetting.BLUETOOTH_SERVICE_TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid()))
        {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;
        displayGattServices(mBluetoothGatt.getServices());
        return mBluetoothGatt.getServices();
    }


    //获取发现的蓝牙模块
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = "未知服务";
        String unknownCharaString = "未知特征";
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
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
        Send send=new Send(BluetoothLeService.getmBluetoothLeService());
        send.execute();
    }


    ////////////////////////////////////////////////////////
    private static HashMap<String, String> attributes = new HashMap();

    static {
        attributes.put("0000fff2-0000-1000-8000-00805f9b34fb", "Device OBDCYX Service");
        attributes.put("0000fff1-0000-1000-8000-00805f9b34fb", "Device OBDCYX String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

    public void sendString(String command) {
        try {
            if (mWriteCharacteristic != null) {
                if (!command.matches("\\r$"))//如果末尾没有回车，自动补全
                    command += "\r";
                if (command.length() > 0) {
                    byte[] WriteBytes;
                    WriteBytes = command.getBytes();
                    System.out.println("command:" + command);
                    //WriteBytes = hex2byte(sendStr.getBytes());
                    mWriteCharacteristic.setValue((byte) 0x00, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                    mWriteCharacteristic.setValue(WriteBytes);
                    mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
                }
            } else {
                System.out.println("写特性未实例化");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
