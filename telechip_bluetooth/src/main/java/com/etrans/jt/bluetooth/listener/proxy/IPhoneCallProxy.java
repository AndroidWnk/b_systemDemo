package com.etrans.jt.bluetooth.listener.proxy;

/**
 * 单元名称:IPhoneCallProxy.java
 * Created by fuxiaolei on 2016/8/30.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/30.
 */
public interface IPhoneCallProxy {
    void answer();

    void hangup();

    /**
     * BluetoothHfDevice.VOLUME_TYPE_MIC   SPK
     *
     * @param type
     */
    void setVolType(int type);

    /**
     * 拨打客服电话时  再次输入的字符
     *
     * @param
     */
    void sendDTMFCode(CharSequence s, int start);


    void changeAudio();

    void sendBIA(boolean b);
}
