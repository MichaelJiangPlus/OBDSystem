package me.michaeljiang.obdsystem.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import me.michaeljiang.obdsystem.R;
import me.michaeljiang.obdsystem.model.CarData;

/**
 * Created by MichaelJiang on 2017/2/20.
 */

public class CarDataAdapter extends BaseAdapter {
    private Context context;
    private List<CarData> datas;
    public CarDataAdapter(Context context, List<CarData> datas){
        this.context = context;
        this.datas= datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CarData getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarData carData = getItem(position);
        View view = LayoutInflater.from(context).inflate(R.layout.car_data_item,null);
        TextView txt_info = (TextView)view.findViewById(R.id.txt_info);
        TextView txt_describe = (TextView)view.findViewById(R.id.txt_describe);
        txt_info.setText("\t"+carData.getInfo());
        txt_describe.setText("\t  "+carData.getData()+carData.getUnit());
        return view;
    }
}
