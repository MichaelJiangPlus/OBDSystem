package me.michaeljiang.obdsystem.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.michaeljiang.obdsystem.MainActivity;
import me.michaeljiang.obdsystem.R;
import me.michaeljiang.obdsystem.adapter.CarDataAdapter;


public class DashBoardFragment extends ListFragment {
    private View view;
    private Handler uiHangle;
    private CarDataAdapter carDataAdapter;
    private MainActivity mainActivity;
    public static DashBoardFragment newInstance(String param1) {
        DashBoardFragment fragment = new DashBoardFragment();
        return fragment;
    }

    public DashBoardFragment(){
        mainActivity = MainActivity.getMainActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        initList();
        return view;
    }

    private void initList(){
        carDataAdapter = new CarDataAdapter(mainActivity, mainActivity.getCarDataList());
        setListAdapter(carDataAdapter);
    }

    public void refeshList(){
        carDataAdapter.notifyDataSetChanged();
    }

}
