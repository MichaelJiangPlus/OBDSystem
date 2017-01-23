package com.michael.obdsystem.service;

import android.os.AsyncTask;

import com.michael.obdsystem.DataShowActivity;

/**
 * Created by Michael on 2016/12/24 0024.
 */

public class DataGet extends AsyncTask<String, Integer, String> {
    private DataShowActivity dataShowActivity;
    private String[] command =new String[]{"0105","010C","010D","010F","012F","015C","015E"};
    public DataGet(DataShowActivity dataShowActivity) {
        this.dataShowActivity=dataShowActivity;
    }


    /**
     * 这里的String参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */

    @Override
    protected String doInBackground(String... params) {
        int i=0;
        while (true){
            dataShowActivity.sendString(command[i++]);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i==7)
                i=0;
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
