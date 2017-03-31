package me.michaeljiang.obddevelopmentassistant.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import me.michaeljiang.obddevelopmentassistant.R;
import me.michaeljiang.obddevelopmentassistant.model.CarData;
import me.michaeljiang.obddevelopmentassistant.model.Command;
import me.michaeljiang.obddevelopmentassistant.setting.AppSetting;

/**
 * Created by MichaelJiang on 2017/2/27.
 */

public class CommandAdapter extends BaseAdapter {
    private Context context;
    private List<Command> datas;
    private CheckBox check_command;
    public CommandAdapter(Context context, List<Command> datas){
        this.context = context;
        this.datas= datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Command getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Command command = getItem(position);
        View view = LayoutInflater.from(context).inflate(R.layout.command_item,null);
        check_command = (CheckBox) view.findViewById(R.id.check_command);
        check_command.setText(command.getCommandNumber()+"  :  "+command.getCommandInfo());
        check_command.setChecked(command.isCheck());
        check_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(command.isCheck()){
                    command.setCheck(false);
                }
                else {
                    command.setCheck(true);
                }
                AppSetting.isChange = true;
            }
        });
        return view;
    }

}
