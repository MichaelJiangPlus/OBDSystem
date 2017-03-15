package me.michaeljiang.obdsystem.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.michaeljiang.obdsystem.R;
import me.michaeljiang.obdsystem.database.CarDataAdapter;
import me.michaeljiang.obdsystem.model.CarData;
import me.michaeljiang.obdsystem.util.AppSetting;


public class DashBoardFragment extends ListFragment {
    private Handler uiHandle = null;
    public Handler getUiHandle() {
        return uiHandle;
    }

    private List<CarData> carDataList = new ArrayList<>();
    private Map<String,CarData> carDataMap = new HashMap<>();
    private View view;
    private CarDataAdapter carDataAdapter;

    public static DashBoardFragment newInstance(String param1) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public DashBoardFragment() {
        uiHandle = new UiHandle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        initList();
        refeshList();
        return view;
    }

    private void initList(){
        carDataAdapter = new CarDataAdapter(getActivity(),carDataList);
        setListAdapter(carDataAdapter);
    }

    private void refeshList(){
        carDataAdapter.notifyDataSetChanged();
    }


    private void displayData(CarData data) {
        if (data != null) {
            CarData temp = carDataMap.get(data.getCommand());
            if(temp!=null)
                temp.setData(data.getData());
            else {
                carDataList.add(data);
                carDataMap.put(data.getCommand(),data);
            }
            refeshList();
        }
    }

    private class UiHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== AppSetting.MESSAGE_DASHBOARD_FRAGMENT_KEY){
                //ui更新
                displayData((CarData) msg.getData().getSerializable(AppSetting.LOG_DASHBOARD_FRAGMENT_TAG));
                Log.d(AppSetting.LOG_DASHBOARD_FRAGMENT_TAG,"getMessage");
            }
            super.handleMessage(msg);
        }
    }
}
