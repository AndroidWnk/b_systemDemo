package com.etrans.jt.bluetooth.listener.view;

/**
 * 单元名称:IPhoneCallView.java
 * Created by fuxiaolei on 2016/8/30.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/30.
 */
public interface IPhoneCallView {
    void updateCallState(String s);

    void updateAudioState(int state);

    void updateCallEx(String callNumber, String name);

    void hangup();

    void updateCallTime(String times);

    void updateTalking();

    void localFinish();

    void updateChangeAudioEnable();
}
