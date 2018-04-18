package com.etrans.jt.bluetooth.listener.view;

import com.etrans.jt.btlibrary.domin.ContactBean;
import com.etrans.jt.btlibrary.domin.SongInfo;

import java.util.List;

/**
 * 单元名称:IPhoneView.java
 * Created by fuxiaolei on 2016/8/29.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/29.
 */
public interface IPhoneView {
    /**
     * 更新下载手机通讯录个数状态
     *
     * @param count
     */
    void onPhoneBookDownloadCount(String count);

    void showQueryList(boolean b);

    /**
     * 显示查询到的联系人列表
     *
     * @param mLstContact
     */
    void notify(List<ContactBean> mLstContact);

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
     * 进度条
     */
    void updateSeekBar();

    void onMediaPlay();

    void onMediaPause();

    void localFinish();

    /**
     * 连接设备的名称
     *
     * @param name
     */
    void onUpdateConnectDeviceName(String name);

    void startAnimation();
    void stopAnimation();

    void onClose();
}
