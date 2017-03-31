package me.michaeljiang.obddevelopmentassistant.model;

import java.io.Serializable;

import me.michaeljiang.obddevelopmentassistant.util.OBDCProtocol;

/**
 * Created by Michael on 2016/12/25 0025.
 */

public class CarData implements Serializable {
    private String data;
    private String info;
    private String command;
    private String unit;

    public CarData(String command, String data, String unit){
        this.command = command;
        this.info = OBDCProtocol.getMode1().get(command);
        this.data = data;
        this.unit = unit;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return command+"  :  "+info+"  :  "+data+" "+unit;
    }
}
