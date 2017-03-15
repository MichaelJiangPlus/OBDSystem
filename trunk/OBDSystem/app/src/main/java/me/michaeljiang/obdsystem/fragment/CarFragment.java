package me.michaeljiang.obdsystem.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.michaeljiang.obdsystem.R;
import me.michaeljiang.obdsystem.util.AppSetting;


public class CarFragment extends Fragment {
    private Handler uiHandle = null;
    public Handler getUiHandle() {
        return uiHandle;
    }
    private View view;

    public static CarFragment newInstance(String param1) {
        CarFragment fragment = new CarFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car, container, false);
        uiHandle = new UiHandle();
        return view;
    }


    private class UiHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(AppSetting.LOG_CAR_FRAGMENT_TAG,"getMessage");
            if(msg.what== AppSetting.MESSAGE_CAR_FRAGMENT_KEY){
                //ui更新

            }
            super.handleMessage(msg);
        }
    }

}
