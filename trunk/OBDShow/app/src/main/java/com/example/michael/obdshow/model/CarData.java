package com.example.michael.obdshow.model;

import java.io.Serializable;

/**
 * Created by Michael on 2016/12/25 0025.
 */

public class CarData implements Serializable {
    private String data;
    private String info;
    private String command;
    private String unit;

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
