package me.michaeljiang.obddevelopmentassistant.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.michaeljiang.obddevelopmentassistant.activity.adapter.CommandAdapter;
import me.michaeljiang.obddevelopmentassistant.model.Command;

/**
 * Created by wzb on 2014/7/28.
 */
public class OBDCProtocol {

    public static String UUID_OBDC_DEVICE_SERVICE="0000fff0-0000-1000-8000-00805f9b34fb";
    public static String UUID_OBDC_DEVICE_CHA1="0000fff1-0000-1000-8000-00805f9b34fb";
    public static String UUID_OBDC_DEVICE_CHA2="0000fff2-0000-1000-8000-00805f9b34fb";
    public static String UUID_OBDC_DEVICE_CHA3="0000fff3-0000-1000-8000-00805f9b34fb";
    public static String UUID_OBDC_DEVICE_CHA4="0000fff4-0000-1000-8000-00805f9b34fb";
    public static String UUID_OBDC_DEVICE_CHA5="0000fff5-0000-1000-8000-00805f9b34fb";

    private static HashMap<String,HashMap<String,String>> protocolDescribe=new HashMap();
    private static HashMap<String,HashMap<String,Integer>> protocolData=new HashMap();
    public static HashMap<String, String> getMode01() {
        return Mode01;
    }

    private static HashMap<String,String> Mode01=new HashMap();
    private static HashMap<String,String> Mode02=new HashMap();
    private static HashMap<String,String> Mode03=new HashMap();
    private static HashMap<String,String> Mode04=new HashMap();
    private static HashMap<String,String> Mode09=new HashMap();

    private static HashMap<String,Integer> mode01=new HashMap();
    private static HashMap<String,Integer> mode02=new HashMap();
    private static HashMap<String,Integer> mode03=new HashMap();
    private static HashMap<String,Integer> mode04=new HashMap();
    private static HashMap<String,Integer> mode09=new HashMap();

    public static HashMap<String, String> getMode01_Unit() {
        return Mode01_Unit;
    }

    private static HashMap<String,String> Mode01_Unit = new HashMap<>();

    public static List<Command> getCommandList() {
        return commandList;
    }

    private static List<Command> commandList = new ArrayList<>();

    static {
        commandList.add(new Command("00", "PID 支持情况（01-20）",false));
        commandList.add(new Command("01", "DTC 情况指示",false));
        commandList.add(new Command("02", "冻结的 DTC",false));
        commandList.add(new Command("03", "燃油系统状态",false));
        commandList.add(new Command("04", "引擎计算负载",false));
        commandList.add(new Command("05", "引擎冷却液温度",true));
        commandList.add(new Command("06", "短期燃油修正 通道 1",false));
        commandList.add(new Command("07", "长期燃油修正 通道 1",false));
        commandList.add(new Command("08", "短期燃油修正 通道 2",false));
        commandList.add(new Command("09", "长期燃油修正 通道 2",false));
        commandList.add(new Command("0A", "燃油压力",false));
        commandList.add(new Command("0B", "油箱压力绝对值",false));
        commandList.add(new Command("0C", "引擎转速",true));
        commandList.add(new Command("0D", "车辆速度",true));
        commandList.add(new Command("0E", "点火提前值",false));
        commandList.add(new Command("0F", "油箱空气温度",true));
        commandList.add(new Command("10", "MAF 空气流量速率",false));
        commandList.add(new Command("11", "节气门位置",false));
        commandList.add(new Command("12", "二次空气状态",false));
        commandList.add(new Command("13", "氧传感器存在情况",false));
        commandList.add(new Command("14", "通道 1，传感器 1 氧传感器电压 短期燃油修正",false));
        commandList.add(new Command("15", "通道 1，传感器 2 氧传感器电压 短期燃油修正",false));
        commandList.add(new Command("16", "通道 1，传感器 3 氧传感器电压 短期燃油修正",false));
        commandList.add(new Command("17", "通道 1，传感器 4 氧传感器电压 短期燃油修正",false));
        commandList.add(new Command("18", "通道 2，传感器 1 氧传感器电压 短期燃油修正",false));
        commandList.add(new Command("19", "通道 2，传感器 2 氧传感器电压 短期燃油修正",false));
        commandList.add(new Command("1A", "通道 2，传感器 3 氧传感器电压 短期燃油修正",false));
        commandList.add(new Command("1B", "通道 2，传感器 4 氧传感器电压 短期燃油修正",false));
        commandList.add(new Command("1C", "当前车辆 OBD 标准指示",false));
        commandList.add(new Command("1D", "氧传感器存在情况",false));
        commandList.add(new Command("1E", "辅助输入状态",false));
        commandList.add(new Command("1F", "引擎启动后运行时间",false));
        commandList.add(new Command("20", "PID 支持情况（21-40）",false));
        commandList.add(new Command("21", "MIL 灯点亮后车辆行驶里程 ",false));
        commandList.add(new Command("22", "油轨压力 （相对于歧管真空度）",false));
        commandList.add(new Command("23", "油轨压力（柴油或汽油直喷",false));
        commandList.add(new Command("2C", "废气循环命令",false));
        commandList.add(new Command("2D", "废气循环错误",false));
        commandList.add(new Command("2E", "蒸发净化命令",false));
        commandList.add(new Command("2F", "油量液位情况",true));
        commandList.add(new Command("31", "故障码清除后行驶里程",false));
        commandList.add(new Command("32", "系统蒸汽压力",false));
        commandList.add(new Command("33", "大气压",false));
        commandList.add(new Command("3C", "催化剂温度 （通道 1， 传感器 1）",false));
        commandList.add(new Command("3D", "催化剂温度 （通道 2， 传感器 1）",false));
        commandList.add(new Command("3E", "催化剂温度 （通道 1， 传感器 2）",false));
        commandList.add(new Command("3F", "催化剂温度 （通道 2， 传感器 2）",false));
        commandList.add(new Command("41", "本次行程监控状态",false));
        commandList.add(new Command("42", "控制模块电压",false));
        commandList.add(new Command("43", "绝对载荷",false));
        commandList.add(new Command("44", "等效比命令",false));
        commandList.add(new Command("45", "相对节气门位置",false));
        commandList.add(new Command("46", "环境空气温度",false));
        commandList.add(new Command("47", "绝对节气门位置 B ",false));
        commandList.add(new Command("48", "绝对节气门位置 C ",false));
        commandList.add(new Command("49", "加速踏板位置 D",false));
        commandList.add(new Command("4A", "加速踏板位置 E",false));
        commandList.add(new Command("4B", "加速踏板位置 F",false));
        commandList.add(new Command("4C", "油门执行器控制值",false));
        commandList.add(new Command("4D", "MIL 灯亮之后行驶时间",false));
        commandList.add(new Command("4E", "故障码亮之后时间",false));
        commandList.add(new Command("50", "空气流量传感器最大值",false));
        commandList.add(new Command("51", "油料类型",false));
        commandList.add(new Command("52", "乙醇百分比",false));
        commandList.add(new Command("53", "绝对蒸汽系统压力",false));
        commandList.add(new Command("54", "蒸汽系统压力",false));
        commandList.add(new Command("55", "短期二次氧传感器微调组 1 和 组 3",false));
        commandList.add(new Command("56", "长期二次氧传感器微调组 1 和 组 3",false));
        commandList.add(new Command("57", "短期二次氧传感器微调组 2 和 组 4",false));
        commandList.add(new Command("58", "长期二次氧传感器微调组 2 和 组 4",false));
        commandList.add(new Command("59", "绝对油轨压力",false));
        commandList.add(new Command("5A", "相对加速踏板位置",false));
        commandList.add(new Command("5B", "混合动力电池剩余",false));
        commandList.add(new Command("5C", "引擎油温",false));
        commandList.add(new Command("5D", "油料注入时序",false));
        commandList.add(new Command("5E", "引擎油量消耗速率",true));
        commandList.add(new Command("5F", "设计排放要求",false));
        commandList.add(new Command("61", "驾驶者要求引擎的扭矩百分比",false));
        commandList.add(new Command("62", "实际引擎扭矩百分比",false));
        commandList.add(new Command("63", "引擎参考扭矩",false));
        commandList.add(new Command("64", "引擎扭矩百分比数据",false));
        commandList.add(new Command("65", "辅助输入输出支持",false));
        commandList.add(new Command("66", "大容量空气流量传感器",false));
        commandList.add(new Command("67", "引擎冷却液温度",false));
        commandList.add(new Command("68", "邮箱空气温度传感器",false));
        commandList.add(new Command("69", "EGR 命令和 EGR 错误",false));
        commandList.add(new Command("6A", "柴油机的进气流量控制命令和相对进气的流动的位置",false));
        commandList.add(new Command("6B", "排气回流温度",false));
        commandList.add(new Command("6C", "节气门执行器控制命令和相对节气门位置",false));
        commandList.add(new Command("6D", "油压控制系统",false));
        commandList.add(new Command("6E", "注入压力控制系统",false));
        commandList.add(new Command("6F", "涡轮增压器压缩机入口压力",false));
        commandList.add(new Command("70", "增压压力控制",false));
        commandList.add(new Command("71", "可变涡轮（VGT）控制",false));
        commandList.add(new Command("72", "排气阀控制",false));
        commandList.add(new Command("73", "排气压力",false));
        commandList.add(new Command("74", "涡轮增压器转速",false));
        commandList.add(new Command("75", "涡轮增压器温度",false));
        commandList.add(new Command("76", "涡轮增压器温度",false));
        commandList.add(new Command("77", "增压空气冷却器的温度",false));
        commandList.add(new Command("78", "排气温度 通道 1",false));
        commandList.add(new Command("79", "排气温度 通道 2",false));
        commandList.add(new Command("7A", "柴油机微粒过滤器",false));
        commandList.add(new Command("7B", "柴油机微粒过滤器",false));
        commandList.add(new Command("7C", "柴油机微粒过滤器温度",false));
        commandList.add(new Command("7F", "引擎工作时间",false));
        commandList.add(new Command("80", "PID 支持情况（81-A0）",false));
        commandList.add(new Command("84", "歧管表面温度",false));
        commandList.add(new Command("86", "颗粒物（PM）传感器",false));
        commandList.add(new Command("87", "进气歧管绝对压力",false));
    }

    static {
        Mode01_Unit.put("00", "");
        Mode01_Unit.put("01", "");
        Mode01_Unit.put("02", "");
        Mode01_Unit.put("03", "");
        Mode01_Unit.put("04", "%");
        Mode01_Unit.put("05", "°C");
        Mode01_Unit.put("06", "%");
        Mode01_Unit.put("07", "%");
        Mode01_Unit.put("08", "%");
        Mode01_Unit.put("09", "%");
        Mode01_Unit.put("0A", "kPa");
        Mode01_Unit.put("0B", "kPa");
        Mode01_Unit.put("0C", "rpm");
        Mode01_Unit.put("0D", "km/h");
        Mode01_Unit.put("0E", "°");
        Mode01_Unit.put("0F", "°C");
        Mode01_Unit.put("10", "grams/s");
        Mode01_Unit.put("11", "%");
        Mode01_Unit.put("12", "");
        Mode01_Unit.put("13", "");
        Mode01_Unit.put("14", "%");
        Mode01_Unit.put("15", "%");
        Mode01_Unit.put("16", "%");
        Mode01_Unit.put("17", "%");
        Mode01_Unit.put("18", "%");
        Mode01_Unit.put("19", "%");
        Mode01_Unit.put("1A", "%");
        Mode01_Unit.put("1B", "%");
        Mode01_Unit.put("1C", "");
        Mode01_Unit.put("1D", "");
        Mode01_Unit.put("1E", "");
        Mode01_Unit.put("1F", "s");
        Mode01_Unit.put("20", "");
        Mode01_Unit.put("21", "km");
        Mode01_Unit.put("22", "kPa");
        Mode01_Unit.put("23", "kPa");
        Mode01_Unit.put("2C", "%");
        Mode01_Unit.put("2D", "%");
        Mode01_Unit.put("2E", "%");
        Mode01_Unit.put("2F", "%");
        Mode01_Unit.put("30", "NA");
        Mode01_Unit.put("31", "km");
        Mode01_Unit.put("32", "Pa");
        Mode01_Unit.put("33", "kPa");
        Mode01_Unit.put("3C", "°C");
        Mode01_Unit.put("3D", "°C");
        Mode01_Unit.put("3E", "°C");
        Mode01_Unit.put("3F", "°C");
        Mode01_Unit.put("40", "");
        Mode01_Unit.put("41", "");
        Mode01_Unit.put("42", "v");
        Mode01_Unit.put("43", "%");
        Mode01_Unit.put("44", "N/A");
        Mode01_Unit.put("45", "%");
        Mode01_Unit.put("46", "°C");
        Mode01_Unit.put("47", "%");
        Mode01_Unit.put("48", "%");
        Mode01_Unit.put("49", "%");
        Mode01_Unit.put("4A", "%");
        Mode01_Unit.put("4B", "%");
        Mode01_Unit.put("4C", "%");
        Mode01_Unit.put("4D", "min");
        Mode01_Unit.put("4E", "min");
//        Mode01_Unit.put("4F", "对于当量比，氧传感器电压，氧传感器电流和进气歧管绝对压力最大值");
        Mode01_Unit.put("50", "g/s");
        Mode01_Unit.put("51", "");
        Mode01_Unit.put("52", "%");
        Mode01_Unit.put("53", "kPa");
        Mode01_Unit.put("54", "Pa");
        Mode01_Unit.put("55", "%");
        Mode01_Unit.put("56", "%");
        Mode01_Unit.put("57", "%");
        Mode01_Unit.put("58", "%");
        Mode01_Unit.put("59", "kPa");
        Mode01_Unit.put("5A", "%");
        Mode01_Unit.put("5B", "%");
        Mode01_Unit.put("5C", "°C");
        Mode01_Unit.put("5D", "°");
        Mode01_Unit.put("5E", "L/h");
        Mode01_Unit.put("5F", "");
        Mode01_Unit.put("60", "");
        Mode01_Unit.put("61", "%");
        Mode01_Unit.put("62", "%");
        Mode01_Unit.put("63", "Nm");
        Mode01_Unit.put("64", "%");
        Mode01_Unit.put("65", "");
        Mode01_Unit.put("66", "");
        Mode01_Unit.put("67", "°C");
        Mode01_Unit.put("68", "");
        Mode01_Unit.put("69", "");
        Mode01_Unit.put("6A", "");
        Mode01_Unit.put("6B", "°C");
        Mode01_Unit.put("6C", "");
        Mode01_Unit.put("6D", "");
        Mode01_Unit.put("6E", "");
        Mode01_Unit.put("6F", "");
        Mode01_Unit.put("70", "");
        Mode01_Unit.put("71", "");
        Mode01_Unit.put("72", "");
        Mode01_Unit.put("73", "");
        Mode01_Unit.put("74", "");
        Mode01_Unit.put("75", "°C");
        Mode01_Unit.put("76", "°C");
        Mode01_Unit.put("77", "°C");
        Mode01_Unit.put("78", "");
        Mode01_Unit.put("79", "");
        Mode01_Unit.put("7A", "");
        Mode01_Unit.put("7B", "");
        Mode01_Unit.put("7C", "°C");
        Mode01_Unit.put("7D", "");
        Mode01_Unit.put("7E", "");
        Mode01_Unit.put("7F", "引擎工作时间");
        Mode01_Unit.put("80", "");
        Mode01_Unit.put("83", "");
        Mode01_Unit.put("84", "");
        Mode01_Unit.put("85", "");
        Mode01_Unit.put("86", "");
        Mode01_Unit.put("87", "");
    }

    static {
        Mode01.put("00", "PID 支持情况（01-20）");
        Mode01.put("01", "DTC 情况指示");
        Mode01.put("02", "冻结的 DTC");
        Mode01.put("03", "燃油系统状态");
        Mode01.put("04", "引擎计算负载");
        Mode01.put("05", "引擎冷却液温度");//
        Mode01.put("06", "短期燃油修正 通道 1");
        Mode01.put("07", "长期燃油修正 通道 1");
        Mode01.put("08", "短期燃油修正 通道 2");
        Mode01.put("09", "长期燃油修正 通道 2");
        Mode01.put("0A", "燃油压力");
        Mode01.put("0B", "油箱压力绝对值");
        Mode01.put("0C", "引擎转速");//
        Mode01.put("0D", "车辆速度");//
        Mode01.put("0E", "点火提前值");
        Mode01.put("0F", "油箱空气温度");//
        Mode01.put("10", "MAF 空气流量速率");//
        Mode01.put("11", "节气门位置");//
        Mode01.put("12", "二次空气状态");
        Mode01.put("13", "氧传感器存在情况");
        Mode01.put("14", "通道 1，传感器 1 氧传感器电压 短期燃油修正");
        Mode01.put("15", "通道 1，传感器 2 氧传感器电压 短期燃油修正");
        Mode01.put("16", "通道 1，传感器 3 氧传感器电压 短期燃油修正");
        Mode01.put("17", "通道 1，传感器 4 氧传感器电压 短期燃油修正");
        Mode01.put("18", "通道 2，传感器 1 氧传感器电压 短期燃油修正");
        Mode01.put("19", "通道 2，传感器 2 氧传感器电压 短期燃油修正");
        Mode01.put("1A", "通道 2，传感器 3 氧传感器电压 短期燃油修正");
        Mode01.put("1B", "通道 2，传感器 4 氧传感器电压 短期燃油修正");
        Mode01.put("1C", "当前车辆 OBD 标准指示");
        Mode01.put("1D", "氧传感器存在情况");
        Mode01.put("1E", "辅助输入状态");
        Mode01.put("1F", "引擎启动后运行时间");
        Mode01.put("20", "PID 支持情况（21-40）");
        Mode01.put("21", "MIL 灯点亮后车辆行驶里程 ");
        Mode01.put("22", "油轨压力 （相对于歧管真空度）");
        Mode01.put("23", "油轨压力（柴油或汽油直喷");
        Mode01.put("2C", "废气循环命令");
        Mode01.put("2D", "废气循环错误");
        Mode01.put("2E", "蒸发净化命令");
        Mode01.put("2F", "油量液位情况");
        Mode01.put("30", "# of warm-ups since codes cleared");
        Mode01.put("31", "故障码清除后行驶里程");
        Mode01.put("32", "系统蒸汽压力");
        Mode01.put("33", "大气压");
        Mode01.put("3C", "催化剂温度 （通道 1， 传感器 1）");
        Mode01.put("3D", "催化剂温度 （通道 2， 传感器 1）");
        Mode01.put("3E", "催化剂温度 （通道 1， 传感器 2）");
        Mode01.put("3F", "催化剂温度 （通道 2， 传感器 2）");
        Mode01.put("40", "PID 支持情况（41-60）");
        Mode01.put("41", "本次行程监控状态");
        Mode01.put("42", "控制模块电压");
        Mode01.put("43", "绝对载荷");
        Mode01.put("44", "等效比命令");//
        Mode01.put("45", "相对节气门位置");
        Mode01.put("46", "环境空气温度");
        Mode01.put("47", "绝对节气门位置 B ");
        Mode01.put("48", "绝对节气门位置 C ");
        Mode01.put("49", "加速踏板位置 D");
        Mode01.put("4A", "加速踏板位置 E");
        Mode01.put("4B", "加速踏板位置 F");
        Mode01.put("4C", "油门执行器控制值");
        Mode01.put("4D", "MIL 灯亮之后行驶时间");
        Mode01.put("4E", "故障码亮之后时间");
        Mode01.put("50", "空气流量传感器最大值");
        Mode01.put("51", "油料类型");
        Mode01.put("52", "乙醇百分比");
        Mode01.put("53", "绝对蒸汽系统压力");
        Mode01.put("54", "蒸汽系统压力");
        Mode01.put("55", "短期二次氧传感器微调组 1 和 组 3");
        Mode01.put("56", "长期二次氧传感器微调组 1 和 组 3");
        Mode01.put("57", "短期二次氧传感器微调组 2 和 组 4");
        Mode01.put("58", "长期二次氧传感器微调组 2 和 组 4");
        Mode01.put("59", "绝对油轨压力");
        Mode01.put("5A", "相对加速踏板位置");
        Mode01.put("5B", "混合动力电池剩余");
        Mode01.put("5C", "引擎油温");
        Mode01.put("5D", "油料注入时序");
        Mode01.put("5E", "引擎油量消耗速率");
        Mode01.put("5F", "设计排放要求");
        Mode01.put("61", "驾驶者要求引擎的扭矩百分比");
        Mode01.put("62", "实际引擎扭矩百分比");
        Mode01.put("63", "引擎参考扭矩");
        Mode01.put("64", "引擎扭矩百分比数据");
        Mode01.put("65", "辅助输入输出支持");
        Mode01.put("66", "大容量空气流量传感器");
        Mode01.put("67", "引擎冷却液温度");
        Mode01.put("68", "邮箱空气温度传感器");
        Mode01.put("69", "EGR 命令和 EGR 错误");
        Mode01.put("6A", "柴油机的进气流量控制命令和相对进气的流动的位置");
        Mode01.put("6B", "排气回流温度");
        Mode01.put("6C", "节气门执行器控制命令和相对节气门位置");
        Mode01.put("6D", "油压控制系统");
        Mode01.put("6E", "注入压力控制系统");
        Mode01.put("6F", "涡轮增压器压缩机入口压力");
        Mode01.put("70", "增压压力控制");
        Mode01.put("71", "可变涡轮（VGT）控制");
        Mode01.put("72", "排气阀控制");
        Mode01.put("73", "排气压力");
        Mode01.put("74", "涡轮增压器转速");
        Mode01.put("75", "涡轮增压器温度");
        Mode01.put("76", "涡轮增压器温度");
        Mode01.put("77", "增压空气冷却器的温度");
        Mode01.put("78", "排气温度 通道 1");
        Mode01.put("79", "排气温度 通道 2");
        Mode01.put("7A", "柴油机微粒过滤器");
        Mode01.put("7B", "柴油机微粒过滤器");
        Mode01.put("7C", "柴油机微粒过滤器温度");
        Mode01.put("7F", "引擎工作时间");
        Mode01.put("80", "PID 支持情况（81-A0）");
        Mode01.put("83", "NULL");
        Mode01.put("84", "歧管表面温度");
        Mode01.put("85", "NULL");
        Mode01.put("86", "颗粒物（PM）传感器");
        Mode01.put("87", "进气歧管绝对压力");
    }

    static {
        //mode02
        Mode02.put("02", "冻结的 DTC");
    }

    static {
        //mode03
        Mode03.put("N/A", "请求的故障码");
    }

    static {

        //mode04
        Mode04.put("N/A", "清除故障码/MIL/检查引擎灯");
    }

    static {
        //mode09
        Mode09.put("00", "模式 9 支持的 PID 列表");
        Mode09.put("01", "VIN 消息");
        Mode09.put("02", "车辆唯一标识 VIN");
        Mode09.put("03", "Calibration ID message count for PID 04. Only for ISO 9141-2, ISO 14230-4 and SAE J1850.");
        Mode09.put("04", "Calibration ID");
        Mode09.put("05", "Calibration verification numbers (CVN) message count for PID 06. Only for ISO 9141-2, ISO 14230-4 and SAE J1850.");
        Mode09.put("06", "Calibration Verification Numbers (CVN)");
        Mode09.put("07", "In-use performance tracking message count for PID 08. Only for ISO 9141-2, ISO 14230-4 and SAE J1850.");
        Mode09.put("08", "In-use performance tracking");
        Mode09.put("09", "ECU name message count for PID 0A");
        Mode09.put("0A", "ECU name");
        Mode09.put("0B", "In-use performance tracking");
    }

    static {
        protocolDescribe.put("01", Mode01);
        protocolDescribe.put("02", Mode02);
        protocolDescribe.put("03", Mode03);
        protocolDescribe.put("04", Mode04);
        protocolDescribe.put("09", Mode09);
    }

    static {
        mode01.put("00", 4);
        mode01.put("01", 4);
        mode01.put("02", 2);
        mode01.put("03", 2);
        mode01.put("04", 1);
        mode01.put("05", 1);
        mode01.put("06", 1);
        mode01.put("07", 1);
        mode01.put("08", 1);
        mode01.put("09", 1);
        mode01.put("0A", 1);
        mode01.put("0B", 1);
        mode01.put("0C", 2);
        mode01.put("0D", 1);
        mode01.put("0E", 1);
        mode01.put("0F", 1);
        //
        //
        mode01.put("10", 2);
        mode01.put("11", 1);
        mode01.put("12", 1);
        mode01.put("13", 1);
        mode01.put("14", 2);
        mode01.put("15", 2);
        mode01.put("16", 2);
        mode01.put("17", 2);
        mode01.put("18", 2);
        mode01.put("19", 2);
        mode01.put("1A", 2);
        mode01.put("1B", 2);
        mode01.put("1C", 1);
        mode01.put("1D", 1);
        mode01.put("1E", 1);
        mode01.put("1F", 2);
        //
        //
        mode01.put("20", 4);
        mode01.put("21", 2);
        mode01.put("22", 2);
        mode01.put("23", 2);
        mode01.put("24", 4);
        mode01.put("25", 4);
        mode01.put("26", 4);
        mode01.put("27", 4);
        mode01.put("28", 4);
        mode01.put("29", 4);
        mode01.put("2A", 4);
        mode01.put("2B", 4);
        mode01.put("2C", 1);
        mode01.put("2D", 1);
        mode01.put("2E", 1);
        mode01.put("2F", 1);
        //
        //
        mode01.put("30", 1);
        mode01.put("31", 2);
        mode01.put("32", 2);
        mode01.put("33", 1);
        mode01.put("34", 4);
        mode01.put("35", 4);
        mode01.put("36", 4);
        mode01.put("37", 4);
        mode01.put("38", 4);
        mode01.put("39", 4);
        mode01.put("3A", 4);
        mode01.put("3B", 4);
        mode01.put("3C", 2);
        mode01.put("3D", 2);
        mode01.put("3E", 2);
        mode01.put("3F", 2);
        //
        //
        mode01.put("40", 4);
        mode01.put("41", 4);
        mode01.put("42", 2);
        mode01.put("43", 2);
        mode01.put("44", 2);
        mode01.put("45", 1);
        mode01.put("46", 1);
        mode01.put("47", 1);
        mode01.put("48", 1);
        mode01.put("49", 1);
        mode01.put("4A", 1);
        mode01.put("4B", 1);
        mode01.put("4C", 1);
        mode01.put("4D", 2);
        mode01.put("4E", 2);
        mode01.put("4F", 4);
        //
        //
        mode01.put("50", 4);
        mode01.put("51", 1);
        mode01.put("52", 1);
        mode01.put("53", 2);
        mode01.put("54", 2);
        mode01.put("55", 2);
        mode01.put("56", 2);
        mode01.put("57", 2);
        mode01.put("58", 2);
        mode01.put("59", 2);
        mode01.put("5A", 1);
        mode01.put("5B", 1);
        mode01.put("5C", 1);
        mode01.put("5D", 2);
        mode01.put("5E", 2);
        mode01.put("5F", 1);
        //
        //
        mode01.put("60", 4);
        mode01.put("61", 1);
        mode01.put("62", 1);
        mode01.put("63", 2);
        mode01.put("64", 5);
        mode01.put("65", 2);
        mode01.put("66", 5);
        mode01.put("67", 3);
        mode01.put("68", 7);
        mode01.put("69", 7);
        mode01.put("6A", 5);
        mode01.put("6B", 5);
        mode01.put("6C", 5);
        mode01.put("6D", 6);
        mode01.put("6E", 5);
        mode01.put("6F", 3);
        //
        //
        mode01.put("70", 9);
        mode01.put("71", 5);
        mode01.put("72", 5);
        mode01.put("73", 5);
        mode01.put("74", 5);
        mode01.put("75", 7);
        mode01.put("76", 7);
        mode01.put("77", 5);
        mode01.put("78", 9);
        mode01.put("79", 9);
        mode01.put("7A", 7);
        mode01.put("7B", 7);
        mode01.put("7C", 9);
        mode01.put("7D", 1);
        mode01.put("7E", 1);
        mode01.put("7F", 13);
        //
        //
        mode01.put("80", 4);
        mode01.put("81", 21);
        mode01.put("82", 21);
        mode01.put("83", 5);
        mode01.put("84", -1);//-1代表为null
        mode01.put("85", -1);
        mode01.put("86", -1);
        mode01.put("87", -1);

        //特殊
        mode01.put("A0", -1);
        mode01.put("C0", -1);
    }

    static {
        //mode02
        mode02.put("02", 2);
    }

    static {
        //mode03
        mode03.put("N/A", 6);//注长度字节为n*6,在外需设置
    }

    static {
        //mode04
        mode04.put("N/A", 0);
    }

    static {
        //mode09
        mode09.put("00", 4);
        mode09.put("01", 1);
        mode09.put("02", 20);//注：17-20，取20
        mode09.put("03", 1);
        mode09.put("04", 16);
        mode09.put("05", 1);
        mode09.put("06", 4);
        mode09.put("07", 1);
        mode09.put("08", -1);
        mode09.put("09", 1);
        mode09.put("0A", 20);
        mode09.put("0B", -1);
    }

    static {
        //
        protocolData.put("01",mode01);
        protocolData.put("02",mode02);
        protocolData.put("03",mode03);
        protocolData.put("04",mode04);
        protocolData.put("09",mode09);
    }

    public static String findDescrip(String Mode, String PID, String Default) {
        String des=protocolDescribe.get(Mode).get(PID);
        return des==null?Default:des;

    }

    public static Integer findData(String Mode, String PID, Integer Default) {
        Integer des=protocolData.get(Mode).get(PID);
        return des==null?Default:des;

    }

    public static double Mode01_calculate(String PID, int data_A, int data_B, double extra) {
        switch (PID){
            case "0D":case "0B":case "30":case "33":
                return data_A;

            case "04":case "11":case "2C":case "2E":case "2F":case "45":
            case "47":case "48":case "49":case "4A":case "4B":case "4C":
            case "52":case "5A": case "5B":
                return data_A*100/255;

            case "0F":case "46": return data_A-40;

            case "06":case "07":case "08":case "09":case "2D":
                return (data_A-128)*100/128;

            case "0A":return data_A*3;

            case "0C":case "32":
                return ((data_A*256)+data_B)/4;

            case "0E":return (data_A-128)/2;
            case "10":return ((data_A*256)+data_B)/100;

            case "14":case "15":case "16":case "17":
            case "18":case "19":case "1A":case "1B":
                return data_A/200;

            case "1F":case "21":case "31":case "4D":
            case "4E":
                return data_A*256+data_B;

            case "22" : return (data_A*256+data_B)*0.079;
            case "23" :return (data_A*256+data_B)*10;

            case "3C" :case "3D" :
            case "3E" :case "3F" :
                return (data_A*256+data_B)/10-40;

            case "42":return (data_A*256+data_B)/1000;
            case "43":return ((data_A*256+data_B)*100)/255;
            case "44":return (data_A*256+data_B)/32768;
            case "53":return (data_A*256+data_B)/200;
            case "54":return (data_A*256+data_B)-32767;
            case "59":return ((data_A*256)+data_B)*10;
            case "5E":return ((data_A*256)+data_B)*0.05;
            case "61":return data_A-125;
            case "62":return data_A-125;
            case "63":return data_A*256+data_B;
            default:return  data_A;
        }

    }

    public static HashMap<String,String> getMode1(){
        return Mode01;
    }





}
