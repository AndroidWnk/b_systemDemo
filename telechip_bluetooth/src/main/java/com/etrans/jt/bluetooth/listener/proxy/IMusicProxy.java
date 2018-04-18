package com.etrans.jt.bluetooth.listener.proxy;

/**
 * 单元名称:IMusicProxy.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public interface IMusicProxy {
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
}
