package com.etrans.jt.btlibrary.listener;

import com.broadcom.bt.hfdevice.BluetoothHfDevice;

/**
 * 单元名称:BluetoothPhoneListener.java
 * Created by fuxiaolei on 2016/7/7.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/7.
 */
public interface BluetoothPhoneListener {

    void onSetBluetoothHFDevice(BluetoothHfDevice bluetoothHFDevice);

    /**
     * 手机信号强度
     *
     * @param status
     * @param imageOffset
     */
    void onUpdateSignalStatus(int status, int imageOffset);

    /**
     * 手机电量
     *
     * @param status
     */
    void onUpdateBatteryStatus(int status);

    /**
     * 更新通话时间
     *
     * @param times
     */
    void onUpdateCallTime(int times);

    /**
     * 通话中的状态
     */
    void onUpdateUITalking();

    /**
     * 挂断
     */
    void onUpdateHangup();

    /**
     * 切换通话回调
     *
     * @param state
     */
    void onUpdateAudioState(int state);

    /**
     * 拨打电话出去的时候 获取拨打的号码
     *
     * @param callNumber
     * @param name
     */
    void onUpdateCallEx(String callNumber, String name);

    void onUpdateDownloadCount(String downLoadCount);

    /**
     * 切换私密
     */
    void onChangeAudioEnable();
    //-------------------------蓝牙电话模块相关回调------------------------


}
