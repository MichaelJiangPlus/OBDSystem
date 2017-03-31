package me.michaeljiang.obddevelopmentassistant.service;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import me.michaeljiang.obddevelopmentassistant.model.Command;
import me.michaeljiang.obddevelopmentassistant.setting.AppSetting;
import me.michaeljiang.obddevelopmentassistant.setting.BluetoothLeServiceSetting;
import me.michaeljiang.obddevelopmentassistant.util.OBDCProtocol;

/**
 * Created by Michael on 2016/7/22 0022.
 */
public class Send extends AsyncTask<String, Integer, String> {
    private BluetoothLeService bluetoothLeService;
    private List<Command> commandList = OBDCProtocol.getCommandList();
    private Command sendCommand;
    private String[] commands = null;


    public Send(BluetoothLeService bluetoothLeService) {
        this.bluetoothLeService=bluetoothLeService;
    }

    private void initCommand(){
        AppSetting.isChange = false;
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < commandList.size(); i++) {
            sendCommand = commandList.get(i);
            if (sendCommand.isCheck())
                temp.add("01"+sendCommand.getCommandNumber());
        }

        commands = new String[temp.size()];
        for(int i = 0; i < temp.size(); i++){
            commands[i] = new String(temp.get(i));
        }

    }

    /**
     * 这里的String参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    @Override
    protected String doInBackground(String... params) {
        while(true){
            if(AppSetting.isChange)
                initCommand();
            if(BluetoothLeServiceSetting.BlueState)
            for(int i = 0 ;i < commands.length ; i++){
                bluetoothLeService.sendString(commands[i]);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }




    /**
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    @Override
    protected void onPostExecute(String result) {

    }

    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
    @Override
    protected void onPreExecute() {

    }

    /**
     * 这里的Intege参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
     */
    @Override
    protected void onProgressUpdate(Integer... values) {

    }


}