package com.etrans.jt.btlibrary.listener;

import com.etrans.jt.btlibrary.domin.SongInfo;

/**
 * 单元名称:BluetoothMusicStateListener.java
 * Created by fuxiaolei on 2016/7/5.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/5.
 */
public interface BluetoothMusicStateListener {
    //播放回调
    void onPlay();

    //下一曲回调
    void onForward();

    //上一曲回调
    void onBackward();

    //停止回调
    void onStop();

    //快退回调
    void onRewind();

    //快进回调
    void onFastforward();

    //播放模式回调
    void onPlayMode();

    //刷新按钮的UI
    void onUpdateUI(long avrcpState);

    //更新进度条
    void onUpdateSeek(int currPos);

    //更新播放时间
    void onUpdateCurrTime(String currtime);

    //更新歌曲信息
    void onSongInfo(SongInfo songinfo);

    /**
     * 歌曲总时长
     *
     * @param duration
     */
    void onUpdateDuration(String duration);

    void onBluetoothMusicClose();
}
