package com.etrans.jt.btlibrary.listener;

/**
 * 单元名称:BluetoothStateChangeListener.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:蓝牙相关状态回调
 * Last Change by fuxiaolei on 2016/7/4.
 */
public interface BluetoothStateChangeListener {
    //开
    public abstract void onBluetoothOpen();

    //关
    public abstract void onBluetoothClose();

    //连接
    public abstract void onBluetoothConnect();

    //断开
    public abstract void onBluetoothDisConnect();

    void onUpdateConnectDeviceName(String deviceName);
}
