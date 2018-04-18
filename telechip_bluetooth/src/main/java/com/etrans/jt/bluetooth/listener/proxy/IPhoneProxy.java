package com.etrans.jt.bluetooth.listener.proxy;

/**
 * 单元名称:IPhoneProxy.java
 * Created by fuxiaolei on 2016/8/29.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/29.
 */
public interface IPhoneProxy{
    /**
     * 拨打
     *
     * @param phoneNum
     */
    void dial(String phoneNum);

    /**
     * 下载电话本
     */
    void downloadPhoneBook();
    /**
     * 播放 暂停
     */
    void play();

    /**
     * 停止
     */
    void stop();

    /**
     * 下一曲
     */
    void backward();

    /**
     * 上一曲
     */
    void forward();

    /**
     * 快退
     *
     * @param b
     */
    void rewind(boolean b);

    /**
     * 快进
     *
     * @param b
     */
    void fastForward(boolean b);

    void resume();


    void initPhoneService();

    void pause();

    void destory();
}
