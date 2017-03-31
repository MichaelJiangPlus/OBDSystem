package me.michaeljiang.obddevelopmentassistant.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.michaeljiang.obddevelopmentassistant.R;
import me.michaeljiang.obddevelopmentassistant.model.PropertyData;

/**
 * Created by MichaelJiang on 2017/2/20.
 */

public class PropertyAdapter extends BaseAdapter {
    private Context context;
    private List<PropertyData> datas;
    public PropertyAdapter(Context context, List<PropertyData> datas){
        this.context = context;
        this.datas= datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public PropertyData getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PropertyData propertyData = getItem(position);
        View view = LayoutInflater.from(context).inflate(R.layout.property_item,null);
        TextView txt_property = (TextView)view.findViewById(R.id.txt_property);
        TextView txt_data = (TextView)view.findViewById(R.id.txt_data);
        txt_property.setText("\t\t"+propertyData.getDescribe());
        txt_data.setText("\t\t\t\t\t\t"+String.valueOf(propertyData.getValue()));
        return view;
    }
}
