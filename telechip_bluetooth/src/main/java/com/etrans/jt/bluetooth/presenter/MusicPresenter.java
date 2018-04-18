package com.etrans.jt.bluetooth.presenter;

import android.content.Context;
import android.widget.SeekBar;

import com.broadcom.bt.avrcp.BluetoothAvrcpController;
import com.etrans.jt.bluetooth.R;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.listener.proxy.IMusicProxy;
import com.etrans.jt.bluetooth.listener.view.IMusicView;
import com.etrans.jt.bluetooth.utils.ToastFactory;
import com.etrans.jt.btlibrary.domin.SongInfo;
import com.etrans.jt.btlibrary.listener.BluetoothMusicStateListener;
import com.etrans.jt.btlibrary.listener.BluetoothStateChangeListener;
import com.etrans.jt.btlibrary.module.BaseModule;
import com.etrans.jt.btlibrary.module.BluetoothModule;
import com.etrans.jt.btlibrary.module.BluetoothMusicModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 单元名称:MusicPresenter.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class MusicPresenter extends BasePresenter implements IMusicProxy
        , BluetoothStateChangeListener, BluetoothMusicStateListener {
    private Context mContext;
    private IMusicView iMusicView;
    private SeekBar mSeekBar;

    public MusicPresenter(Context _context, IMusicView _iMusicView, SeekBar seekBar) {
        super(_context);
        mContext = _context;
        iMusicView = _iMusicView;
        mSeekBar = seekBar;
        BluetoothMusicModule.getInstance().initDevice(seekBar);
    }

    @Override
    protected List<BaseModule> getRefHandles() {
        List<BaseModule> modules = new ArrayList<BaseModule>();
        modules.add(BluetoothModule.getInstance());
        modules.add(BluetoothMusicModule.getInstance());
        return modules;
    }

    @Override
    public void play() {
        BluetoothMusicModule.getInstance().play();
    }

    @Override
    public void stop() {
        BluetoothMusicModule.getInstance().stop();
    }

    @Override
    public void backward() {
        BluetoothMusicModule.getInstance().backward();
    }

    @Override
    public void forward() {
        BluetoothMusicModule.getInstance().forward();
    }

    @Override
    public void rewind(boolean b) {
        BluetoothMusicModule.getInstance().rewind(b);
    }

    @Override
    public void fastForward(boolean b) {
        BluetoothMusicModule.getInstance().fastforward(b);
    }

    @Override
    public void resume() {
        BluetoothMusicModule.getInstance().resume();
    }

    //-------------------蓝牙状态---------------------------
    @Override
    public void onBluetoothOpen() {

    }

    @Override
    public void onBluetoothClose() {
        ToastFactory.getToast(mContext, mContext.getResources().getString(R.string.close)).show();
    }

    @Override
    public void onBluetoothConnect() {

    }

    @Override
    public void onBluetoothDisConnect() {
        ToastFactory.getToast(mContext, mContext.getResources().getString(R.string.disconnect)).show();
    }

    @Override
    public void onUpdateConnectDeviceName(String deviceName) {

    }

    //--------------------蓝牙音乐相关--------------------
    @Override
    public void onUpdateUI(long avrcpState) {
        //TODO 刷新按钮UI
        if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_PLAYING) {
            iMusicView.onMediaPlay();
        } else if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_PAUSED) {
            iMusicView.onMediaPause();
        } else if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_FWD_SEEK) {

        } else if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_REV_SEEK) {

        }  else if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_STOPPED) {

        }
    }

    @Override
    public void onUpdateSeek(int currPos) {
        mSeekBar.setProgress(currPos);
        BluetoothMusicModule.getInstance().initDevice(mSeekBar);
    }

    @Override
    public void onUpdateCurrTime(String txt) {
        iMusicView.onUpdateCurrTime(txt);
    }

    @Override
    public void onSongInfo(SongInfo songinfo) {
        iMusicView.updateSongInfo(songinfo);
    }

    @Override
    public void onUpdateDuration(String duration) {
        iMusicView.onUpdateDuration(duration);
    }

    @Override
    public void onBluetoothMusicClose() {
        iMusicView.onClose();
    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onForward() {

    }

    @Override
    public void onBackward() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRewind() {

    }

    @Override
    public void onFastforward() {

    }

    @Override
    public void onPlayMode() {

    }


}
