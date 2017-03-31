package me.michaeljiang.obddevelopmentassistant.activity.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.michaeljiang.obddevelopmentassistant.R;
import me.michaeljiang.obddevelopmentassistant.activity.MainActivity;
import me.michaeljiang.obddevelopmentassistant.activity.adapter.CommandAdapter;
import me.michaeljiang.obddevelopmentassistant.model.Command;
import me.michaeljiang.obddevelopmentassistant.service.Send;
import me.michaeljiang.obddevelopmentassistant.setting.AppSetting;
import me.michaeljiang.obddevelopmentassistant.util.OBDCProtocol;


public class PubSubFragment extends ListFragment {
    private View view;
    private MainActivity mainActivity ;
    private CommandAdapter commandAdapter;
    private List<Command> commmandList = OBDCProtocol.getCommandList();
    private ToggleButton toggleButton ;

    public static PubSubFragment newInstance(String param1) {
        PubSubFragment fragment = new PubSubFragment();
        return fragment;
    }

    public PubSubFragment(){
        mainActivity = MainActivity.getMainActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pub_suba, container, false);
        initList();
        initToggButton();
        return view;
    }

    private void initToggButton(){
        toggleButton = (ToggleButton)view.findViewById(R.id.tog_select);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton.isChecked()){
                    for(int i = 0;i < commmandList.size();i++){
                        commandAdapter.getItem(i).setCheck(true);
                    }
                }
                else {
                    for(int i = 0;i < commmandList.size();i++){
                        commandAdapter.getItem(i).setCheck(false);
                    }
                }
                AppSetting.isChange = true;
                commandAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initList(){
        commandAdapter = new CommandAdapter(getActivity(),commmandList);
        setListAdapter(commandAdapter);
    }




}
