package me.michaeljiang.obdsystem.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.michaeljiang.obdsystem.R;


public class CarFragment extends Fragment {
    private static final String TAG = "CarFragment";
    private static final int CAR_FRAGMENT_WHAT = 14;
    private Handler uiHandle = null;
    public static CarFragment newInstance(String param1) {
        CarFragment fragment = new CarFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    private CarFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
        TextView tv = (TextView)view.findViewById(R.id.txt_info);
        tv.setText(agrs1);
        return view;
    }

    private class UiHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"getMessage");
            if(msg.what== CAR_FRAGMENT_WHAT){
                //ui更新
            }
            super.handleMessage(msg);
        }
    }
}
