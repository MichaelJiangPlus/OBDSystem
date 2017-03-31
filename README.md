# OBDSystem #
&emsp;&emsp;这是我大学期间为了获取车辆信息而构筑的一整套系统，由于没有车，所以专门购买了一个用于模拟OBD（车载诊断系统）数据的模拟器，OBDStm32和OBDSimulator都是在卖家给的源代码基础上进行开发的，本项目主要分为3各部分

-  汽车数据模拟器
- OBD开发助手
- OBD使用场景

## 以下是各项目的介绍 ##
### OBDDevelopmentAssistant ###
```
├─activity
│  │  DeviceState.java
│  │  MainActivity.java
│  │
│  ├─adapter
│  │      CarDataAdapter.java
│  │      CommandAdapter.java
│  │      LeDeviceListAdapter.java
│  │      PropertyAdapter.java
│  │
│  └─fragment
│          DashBoardFragment.java
│          PubSubFragment.java
│          SettingFragment.java
│
├─model
│      CarData.java
│      Command.java
│      PropertyData.java
│
├─service
│      BluetoothLeService.java
│      MqttService.java
│      Send.java
│
├─setting
│      AppSetting.java
│      BluetoothLeServiceSetting.java
│      MqttSeting.java
│
└─util
        DataAnalysed.java
        OBDCProtocol.java
```

&emsp;&emsp;其中util中的OBDCProtocol是用来按照格式读取OBD信息的，DataAnalysed则是处理回传信息
如果您选用的OBD传感器和我的不一样，请修改项目中的BluetoothLeService以及DataAnalysed和OBDCProtocol类

&emsp;&emsp;本软件使用的是MQTT作为通讯协议，所以如果需要直接使用的话，请下载安装本软件，然后在设置页面输入MQTT的相关信息即可。
### DashBoard ###
```
├─activity
│  DeviceState.java
│  MainActivity.java
│
├─adapter
│      CarDataAdapter.java
│      LeDeviceListAdapter.java
│
├─database
│      MyDatabaseHelper.java
│
├─fragment
│      CarFragment.java
│      DashBoardFragment.java
│      NavigationFragment.java
│
├─model
│      CarData.java
│      Point.java
│
├─service
│      BluetoothLeService.java
│      MqttService.java
│      Send.java
│
└─util
        AppSetting.java
        CoordinateConversion.java
        DataAnalysed.java
        OBDCProtocol.java
```
&emsp;&emsp;本项目和上述开发者辅助工具差不多，基本使用了同样的结构和构架，本项目工程主要是面对希望使用OBD获取车辆信息的人，现在界面和数据分析功能都还不齐全，正在更新中（~~没车让我很忧伤啊，正在考驾照中~~)

## 来自卖家的助攻 ##
&emsp;&emsp;呃呃呃  这部分内容其实没什么可以看得，我是从淘宝上买家哪里获得的源码，它分为两部分，其中一部分是模拟器本身Stm32的程序，另一部分是电脑.NET的程序，我只是根据自己的需求，新增了随机数功能，让他在原有的数据基础上能够随便+1s -1s之类的而已
→_→  

[淘宝链接](https://item.taobao.com/item.htm?spm=a230r.1.14.24.4aFy3O&id=521895133360&ns=1&abbucket=1#detail)

### OBDSimulator （.NET端的程序） ###
&emsp;&emsp;我认真看过了卖家的文档，他其实是用.NET写了一个串口工具，然后还是按照老样子发送AT+SDSxxx:UP之类的指令，所谓的调节精度就是点一下后发送多少次而已，我在思考要不要把它改成直接发送数据的值，然后接受后直接返回当前数据的值，不知道这样会不会更好用一点

![](http://i2.muimg.com/567571/951412ff27020ed4.png)

### OBDStm32（Stm32端的程序） ###
&emsp;&emsp;这部分的话我也仔细看了下，老实说卖家有一部分函数真的是很毒啊，有很多函数名称都是随便起的，而且刚开始看的时候结构真的很尴尬，他实际上是一个while()循环，然后等待串口数据输入，如果有输入则判断输入数据是什么然后修改相关数据的值，最终通过Kan或者Can等协议发送（不过这部分确实很厉害啊，但对我来说我又不需要改Kan或者Can的协议 hhh）

###### 接受信息匹配的部分
![](http://i2.muimg.com/567571/6cd24ad2afaa9842.png)


