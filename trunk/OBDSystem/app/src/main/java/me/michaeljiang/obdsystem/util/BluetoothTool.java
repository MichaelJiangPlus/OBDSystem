package me.michaeljiang.obdsystem.util;

import java.util.HashMap;

/**
 * Created by Michael on 2016/12/24 0024.
 */

public class BluetoothTool {
    private static HashMap<String, String> attributes = new HashMap();
    static {
        attributes.put("0000fff2-0000-1000-8000-00805f9b34fb", "Device OBDCYX Service");
        attributes.put("0000fff1-0000-1000-8000-00805f9b34fb", "Device OBDCYX String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

}
