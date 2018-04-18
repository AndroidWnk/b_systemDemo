package com.etrans.jt.bluetooth.listener.view;

import com.etrans.jt.btlibrary.domin.SongInfo;

/**
 * 单元名称:IMusicView.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public interface IMusicView {
    /**
     * 播放
     */
    void onMediaPlay();

    void updateSeekBar();

    void updateSongInfo(SongInfo songinfo);

    /**
     * 歌曲时长
     *
     * @param duration
     */
    void onUpdateDuration(String duration);

    /**
     * 播放位置
     *
     * @param txt
     */
    void onUpdateCurrTime(String txt);

    /**
     * 暂停
     */
    void onMediaPause();

    void onClose();
}
