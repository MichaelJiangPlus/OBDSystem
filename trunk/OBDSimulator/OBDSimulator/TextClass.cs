using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace OBDSimulator
{
   
    public class TextClass
    {
        public struct dsvalueDf
        {
            public int total;
            public string[] dsv;
        }
        
        public  dsvalueDf dsvalue = new dsvalueDf();
        public void dsvalueTo(int i)
        {
            
            dsvalue.total = 3;
            switch (i)
            {
                case 1:
                    dsvstr1();
                    break;
                case 2:
                    dsvstr2();
                    break;
                case 3:
                    dsvstr2();
                    break;
                case 4:
                    dsvstr2();
                    break;
                case 5:
                    dsvstr5();
                    break;
                case 6:
                    dsvstr5();
                    break;
                case 7:
                    dsvstr5();
                    break;
                case 8:
                    dsvstr2();
                    break;
                case 9:
                    dsvstr2();
                    break;
                case 10:
                    dsvstr2();
                    break;
                case 11:
                    dsvstr2();
                    break;
                case 12:
                    dsvstr2();
                    break;
                case 13:
                    dsvstr2();
                    break;
                case 14:
                    dsvstr5();
                    break;
                case 15:
                    dsvstr5();
                    break;
                case 16:
                    dsvstr5();
                    break;
                case 17:
                    dsvstr5();
                    break;
                case 18:
                    dsvstr5();
                    break;
                case 19:
                    dsvstr5();
                    break;
                case 20:
                    dsvstr2();
                    break;
                case 21:
                    dsvstr2();
                    break;
                case 22:
                    dsvstr2();
                    break;
                case 23:
                    dsvstr5();
                    break;
                case 24:
                    dsvstr5();
                    break;
                case 25:
                    dsvstr2();
                    break;
                case 26:
                    dsvstr2();
                    break;
                case 27:
                    dsvstr2();
                    break;
                case 28:
                    dsvstr2();
                    break;
                case 29:
                    dsvstr2();
                    break;
                case 30:
                    dsvstr2();
                    break;
                case 31:
                    dsvstr5();
                    break;
                case 32:
                    dsvstr5();
                    break;
                case 33:
                    dsvstr5();
                    break;
                case 34:
                    dsvstr5();
                    break;
                case 35:
                    dsvstr5();
                    break;
                case 36:
                    dsvstr5();
                    break;
                case 37:
                    dsvstr37();
                    break;
                case 38:
                    dsvstr38();
                    break;
                case 39:
                    dsvstr38();
                    break;
                case 54:
                    dsvstr54();
                    break;
                case 55:
                    dsvstr55();
                    break;
                case 72:
                    dsvstr72();
                    break;
                case 73:
                    dsvstr73();
                    break;
                case 74:
                    dsvstr1();
                    break;
                case 136:
                    dsvstr136();
                    break;
                default:
                    break;
            }

 
        }
        private void dsvstr1()
        {
            dsvalue.total = 2;
            dsvalue.dsv = new string[2];
            dsvalue.dsv[0] = "ON";
            dsvalue.dsv[1] = "OFF";
        }
        private void dsvstr2()
        {
            dsvalue.total = 2;
            dsvalue.dsv = new string[2];
            dsvalue.dsv[0] = "支持";
            dsvalue.dsv[1] = "不支持";
        }
        private void dsvstr5()
        {
            dsvalue.total = 2;
            dsvalue.dsv = new string[2];
            dsvalue.dsv[0] = "OK";
            dsvalue.dsv[1] = "未完成";
        }
        private void dsvstr37()
        {
            dsvalue.total = 5;
            dsvalue.dsv = new string[5];
            dsvalue.dsv[0] = "P1012";
            dsvalue.dsv[1] = "P1013";
            dsvalue.dsv[2] = "P2013";
            dsvalue.dsv[3] = "P2012";
            dsvalue.dsv[4] = "P105A";
        }
        private void dsvstr38()
        {
            dsvalue.total = 6;
            dsvalue.dsv = new string[6];
            dsvalue.dsv[0] = "OL";
            dsvalue.dsv[1] = "CL";
            dsvalue.dsv[2] = "OL-Drive";
            dsvalue.dsv[3] = "OL-Fault";
            dsvalue.dsv[4] = "CL-Fault";
            dsvalue.dsv[5] = "--";
        }
        private void dsvstr54()
        {
            dsvalue.total = 5;
            dsvalue.dsv = new string[5];
            dsvalue.dsv[0] = "UPS";
            dsvalue.dsv[1] = "DNS";
            dsvalue.dsv[2] = "OFF";
            dsvalue.dsv[3] = "DIAG";
            dsvalue.dsv[4] = "--";
        }
        private void dsvstr55()
        {
            dsvalue.total = 8;
            dsvalue.dsv = new string[8];
            dsvalue.dsv[0] = "B1-S1";
            dsvalue.dsv[1] = "B1-S2";
            dsvalue.dsv[2] = "B1-S3";
            dsvalue.dsv[3] = "B1-S4";
            dsvalue.dsv[4] = "B2-S1";
            dsvalue.dsv[5] = "B2-S2";
            dsvalue.dsv[6] = "B2-S3";
            dsvalue.dsv[7] = "B2-S4";
        }
        private void dsvstr72()
        {
            dsvalue.total = 11;
            dsvalue.dsv = new string[11];
            dsvalue.dsv[0] = "OBD";
            dsvalue.dsv[1] = "OBD and OBD II";
            dsvalue.dsv[2] = "OBDI";
            dsvalue.dsv[3] = "NO OBD";
            dsvalue.dsv[4] = "EOBD";
            dsvalue.dsv[5] = "EOBD AND OBDII";
            dsvalue.dsv[6] = "EOBD AND OBD";
            dsvalue.dsv[7] = "EOBD OBD AND OBDII";
            dsvalue.dsv[8] = "JOBD";
            dsvalue.dsv[9] = "JOBD AND OBDII";
            dsvalue.dsv[10] = "JOBD AND EOBD";
        }
        private void dsvstr73()
        {
            dsvalue.total = 8;
            dsvalue.dsv = new string[8];
            dsvalue.dsv[0] = "B1-S1";
            dsvalue.dsv[1] = "B1-S2";
            dsvalue.dsv[2] = "B2-S1";
            dsvalue.dsv[3] = "B2-S2";
            dsvalue.dsv[4] = "B3-S1";
            dsvalue.dsv[5] = "B3-S2";
            dsvalue.dsv[6] = "B4-S1";
            dsvalue.dsv[7] = "B4-S2";
        }
        private void dsvstr136()
        {
            dsvalue.total = 16;
            dsvalue.dsv = new string[16];
            dsvalue.dsv[0] = "GAS";
            dsvalue.dsv[1] = "METH";
            dsvalue.dsv[2] = "ETH";
            dsvalue.dsv[3] = "DSL";
            dsvalue.dsv[4] = "LPG";
            dsvalue.dsv[5] = "CNG";
            dsvalue.dsv[6] = "PROP";
            dsvalue.dsv[7] = "ELEC";
            dsvalue.dsv[8] = "BI_GAS";
            dsvalue.dsv[9] = "BI_METH";
            dsvalue.dsv[10] = "BI_ETH";
            dsvalue.dsv[11] = "BI_LPG";
            dsvalue.dsv[12] = "BI_CNG";
            dsvalue.dsv[13] = "BI_PROP";
            dsvalue.dsv[14] = "BI_ELEC";
            dsvalue.dsv[15] = "ISO/SAE";
        }
        public string[,] DSString = new string[151,2]{
{"000.ECU中存储的故障码数量",                                                            "--        "},                  
{"001.MIL(故障指示灯)状态",                                                              "--        "},                  
{"002.支持失火监测",                                                                     "--        "},                  
{"003.支持燃油系统监测",                                                                 "--        "},                  
{"004.支持综合部件监测",                                                                 "--        "},                  
{"005.失火监测准备就绪",                                                                 "--        "},                  
{"006.燃油系统监测准备就绪",                                                             "--        "},                  
{"007.综合部件监测准备就绪",                                                             "--        "},                  
{"008.支持NMHC催化剂监测(清码后)",                                                       "--        "},                  
{"009.支持氮氧化合物后处理监测(清码后)",                                                 "--        "},                  
{"010.支持增压压力系统监测(清码后)",                                                     "--        "},                  
{"011.支持废气传感器监测(清码后)",                                                       "--        "},                  
{"012.支持PM(颗粒物)过滤器监测(清码后)",                                                 "--        "},                  
{"013.支持EGR(废气再循环)系统和/或VVT(可变阀正时)系统监测(清码后)",                      "--        "},                  
{"014.NMHC催化剂监测准备就绪(清码后)",                                                   "--        "},                  
{"015.氮氧化合物后处理监测准备就绪(清码后)",                                             "--        "},                  
{"016.增压压力系统监测准备就绪(清码后)",                                                 "--        "},                  
{"017.废气传感器监测准备就绪(清码后)",                                                   "--        "},                  
{"018.PM(颗粒物)监测准备就绪(清码后)",                                                   "--        "},                  
{"019.支持EGR(废气再循环)系统和/或VVT(可变阀正时)系统监测准备就绪(清码后)",              "--        "},                  
{"020.失火检测激活",                                                                     "--        "},                  
{"021.燃油系统检测激活",                                                                 "--        "},                  
{"022.综合部件检测启用",                                                                 "--        "},                  
{"023.失火检测完成",                                                                     "--        "},                  
{"024.燃油系统检测完成",                                                                 "--        "},                  
{"025.支持NMHC催化剂监测(驾驶循环)",                                                     "--        "},                  
{"026.支持氮氧化合物后处理监测(驾驶循环)",                                               "--        "},                  
{"027.支持增压压力系统监测(驾驶循环)",                                                   "--        "},                  
{"028.支持废气传感器监测(驾驶循环)",                                                     "--        "},                  
{"029.支持PM(颗粒物)过滤器监测(驾驶循环)",                                               "--        "},                  
{"030.支持EGR(废气再循环)系统和/或VVT(可变阀正时)系统监测(驾驶循环)",                    "--        "},                  
{"031.NMHC催化剂监测准备就绪(驾驶循环)",                                                 "--        "},                  
{"032.氮氧化合物后处理监测准备就绪(驾驶循环)",                                           "--        "},                  
{"033.增压压力系统监测准备就绪(驾驶循环)",                                               "--        "},                  
{"034.废气传感器监测准备就绪(驾驶循环)",                                                 "--        "},                  
{"035.PM(颗粒物)监测准备就绪(驾驶循环)",                                                 "--        "},                  
{"036.支持EGR(废气再循环)系统和/或VVT(可变阀正时)系统监测准备就绪(驾驶循环)",            "--        "},                  
{"037.导致冻结帧数据存储的故障码",                                                       "--        "},                  
{"038.燃油系统1状态",                                                                    "--        "},                  
{"039.燃油系统2状态",                                                                    "--        "},                  
{"040.负荷计算值",                                                                       "--        "},                  
{"041.发动机冷却液温度",                                                                 "--        "},                  
{"042.短期燃油修正(缸组1)",                                                              "--        "},                  
{"043.长期燃油修正(缸组1)",                                                              "--        "},                  
{"044.短期燃油修正(缸组2)",                                                              "--        "},                  
{"045.长期燃油修正(缸组2)",                                                              "--        "},                  
{"046.油轨压力(表压力)",                                                                 "--        "},                  
{"047.进气歧管绝对压力",                                                                 "--        "},                  
{"048.发动机转数",                                                                       "--        "},                  
{"049.车速传感器",                                                                       "--        "},                  
{"050.气缸1点火提前角",                                                                  "--        "},                  
{"051.进气温度",                                                                         "--        "},                  
{"052.来自质量空气流量传感器的空气流量",                                                 "--        "},                  
{"053.节气门绝对位置",                                                                   "--        "},                  
{"054.指令的二次空气喷射状态",                                                           "--        "},                  
{"055.氧传感器位置",                                                                     "--        "},                  
{"056.氧传感器输出电压(缸组1,传感器1)",                                                  "--        "},                  
{"057.短期燃油修正(缸组1,传感器1)",                                                      "--        "},                  
{"058.氧传感器输出电压(缸组1,传感器2)",                                                  "--        "},                  
{"059.短期燃油修正(缸组1,传感器2)",                                                      "--        "},                  
{"060.氧传感器输出电压(缸组1,传感器3)",                                                  "--        "},                  
{"061.短期燃油修正(缸组1,传感器3)",                                                      "--        "},                  
{"062.氧传感器输出电压(缸组1,传感器4)",                                                  "--        "},                  
{"063.短期燃油修正(缸组1,传感器4)",                                                      "--        "},                  
{"064.氧传感器输出电压(缸组2,传感器1)",                                                  "--        "},                  
{"065.短期燃油修正(缸组2,传感器1)",                                                      "--        "},                  
{"066.氧传感器输出电压(缸组2,传感器2)",                                                  "--        "},                  
{"067.短期燃油修正(缸组2,传感器2)",                                                      "--        "},                  
{"068.氧传感器输出电压(缸组2,传感器3)",                                                  "--        "},                  
{"069.短期燃油修正(缸组2,传感器3)",                                                      "--        "},                  
{"070.氧传感器输出电压(缸组2,传感器4)",                                                  "--        "},                  
{"071.短期燃油修正(缸组2,传感器4)",                                                      "--        "},                  
{"072.车辆或发动机认真的OBD请求",                                                        "--        "},                  
{"073.氧传感器位置",                                                                     "--        "},                  
{"074.PTO(动力输出)状态",                                                                "--        "},                  
{"075.发动机启动后时间",                                                                 "--        "},                  
{"076.MIL(故障指示灯)点亮后的行驶距离",                                                  "--        "},                  
{"077.相对于歧管真空度的燃油轨压力",                                                     "--        "},                  
{"078.油轨压力",                                                                         "--        "},                  
{"079.当量比(λ)(缸组1,传感器1)",                                                        "--        "},                  
{"080.氧传感器电压(缸组1,传感器1)",                                                      "--        "},                  
{"081.当量比(λ)(缸组1,传感器2)",                                                        "--        "},                  
{"082.氧传感器电压(缸组1,传感器2)",                                                      "--        "},                  
{"083.当量比(λ)(缸组1,传感器3)",                                                        "--        "},                  
{"084.氧传感器电压(缸组1,传感器3)",                                                      "--        "},                  
{"085.当量比(λ)(缸组1,传感器4)",                                                        "--        "},                  
{"086.氧传感器电压(缸组1,传感器4)",                                                      "--        "},                  
{"087.当量比(λ)(缸组2,传感器1)",                                                        "--        "},                  
{"088.氧传感器电压(缸组2,传感器1)",                                                      "--        "},                  
{"089.当量比(λ)(缸组2,传感器2)",                                                        "--        "},                  
{"090.氧传感器电压(缸组2,传感器2)",                                                      "--        "},                  
{"091.当量比(λ)(缸组2,传感器3)",                                                        "--        "},                  
{"092.氧传感器电压(缸组2,传感器3)",                                                      "--        "},                  
{"093.当量比(λ)(缸组2,传感器4)",                                                        "--        "},                  
{"094.氧传感器电压(缸组2,传感器4)",                                                      "--        "},                  
{"095.指令EGR",                                                                          "--        "},                  
{"096.EGR(废气再循环)故障",                                                              "--        "},                  
{"097.指令的蒸发净化",                                                                   "--        "},                  
{"098.燃油液位输入",                                                                     "--        "},                  
{"099.清除故障码后的暖机次数",                                                           "--        "},                  
{"100.清除故障码后的行驶距离",                                                           "--        "},                  
{"101.燃油蒸气排放系统蒸气压力",                                                         "--        "},                  
{"102.大气压",                                                                           "--        "},                  
{"103.当量比(λ)(缸组1,传感器1)",                                                        "--        "},                  
{"104.氧传感器电流(缸组1,传感器1)",                                                      "--        "},                  
{"105.当量比(λ)(缸组1,传感器2)",                                                        "--        "},                  
{"106.氧传感器电流(缸组1,传感器2)",                                                      "--        "},                  
{"107.当量比(λ)(缸组1,传感器3)",                                                        "--        "},                  
{"108.氧传感器电流(缸组1,传感器3)",                                                      "--        "},                  
{"109.当量比(λ)(缸组1,传感器4)",                                                        "--        "},                  
{"110.氧传感器电流(缸组1,传感器4)",                                                      "--        "},                  
{"111.当量比(λ)(缸组2,传感器1)",                                                        "--        "},                  
{"112.氧传感器电流(缸组2,传感器1)",                                                      "--        "},                  
{"113.当量比(λ)(缸组2,传感器2)",                                                        "--        "},                  
{"114.氧传感器电流(缸组2,传感器2)",                                                      "--        "},                  
{"115.当量比(λ)(缸组2,传感器3)",                                                        "--        "},                  
{"116.氧传感器电流(缸组2,传感器3)",                                                      "--        "},                  
{"117.当量比(λ)(缸组2,传感器4)",                                                        "--        "},                  
{"118.氧传感器电流(缸组2,传感器4)",                                                      "--        "},                  
{"119.催化剂温度(缸组1,传感器1)",                                                        "--        "},                  
{"120.催化剂温度(缸组2,传感器1)",                                                        "--        "},                  
{"121.催化剂温度(缸组1,传感器2)",                                                        "--        "},                  
{"122.催化剂温度(缸组2,传感器2)",                                                        "--        "},                  
{"123.控制模块电压",                                                                     "--        "},                  
{"124.绝对负荷值",                                                                       "--        "},                  
{"125.燃油/空气指令的当量比",                                                            "--        "},                  
{"126.节气门相对位置",                                                                   "--        "},                  
{"127.环境温度",                                                                         "--        "},                  
{"128.节气门绝对位置B",                                                                  "--        "},                  
{"129.节气门绝对位置C",                                                                  "--        "},                  
{"130.节气门绝对位置D",                                                                  "--        "},                  
{"131.节气门绝对位置E",                                                                  "--        "},                  
{"132.节气门绝对位置F",                                                                  "--        "},                  
{"133.指令的节气门执行器控制",                                                           "--        "},                  
{"134.故障指示灯点亮时发动机运转时间",                                                   "--        "},                  
{"135.清除故障码后的发动机运转时间",                                                     "--        "},                  
{"136.车辆当前使用的燃料类型",                                                           "--        "},                  
{"137.酒精燃料百分比",                                                                   "--        "},                  
{"138.燃油蒸气排放系统蒸气绝对压力",                                                     "--        "},                  
{"139.蒸气排放系统蒸气压力",                                                             "--        "},                  
{"140.副氧传感器短期燃油修正-缸组1",                                                     "--        "},                  
{"141.副氧传感器长期燃油修正-缸组1",                                                     "--        "},                  
{"142.副氧传感器短期燃油修正-缸组2",                                                     "--        "},                  
{"143.副氧传感器长期燃油修正-缸组2",                                                     "--        "},                  
{"144.油轨压力(绝对压力)",                                                               "--        "},                  
{"145.油门踏板相对位置",                                                                 "--        "},                  
{"146.混合动力所用电池组剩余使用时间",                                                   "--        "},                  
{"147.发动机机油温度",                                                                   "--        "},                  
{"148.喷油正时",                                                                         "--        "},                  
{"149.发动机供油率",                                                                     "--        "},                  
{"150.车辆排放要求",                                                                     "--        "},                  
        };
    }
}
