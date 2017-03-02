package com.michael.obdsystem.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.michael.obdsystem.R;

import java.util.List;

/**
 * Created by Michael on 2016/12/25 0025.
 */

public class DataAdapter extends BaseAdapter {
    private int resourceID;
    private List<Data> dataList;
    private Context context;

    public DataAdapter(Context context, int textViewResourcrID , List dataList){
        super();
        this.resourceID = textViewResourcrID;
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Data getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = getItem(position);
        View view = LayoutInflater.from(context).inflate(resourceID,null);
        TextView txt_info = (TextView)view.findViewById(R.id.txt_info);
        TextView txt_data = (TextView)view.findViewById(R.id.txt_data);
        txt_info.setText(data.getInfo());
        txt_data.setText(data.getData());
        return view;
    }
}
