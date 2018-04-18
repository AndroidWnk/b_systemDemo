package com.etrans.jt.bluetooth.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.SeekBar;

import com.broadcom.bt.avrcp.BluetoothAvrcpController;
import com.broadcom.bt.hfdevice.BluetoothHfDevice;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.listener.proxy.IPhoneProxy;
import com.etrans.jt.bluetooth.listener.view.IPhoneView;
import com.etrans.jt.btlibrary.domin.ContactBean;
import com.etrans.jt.btlibrary.domin.SongInfo;
import com.etrans.jt.btlibrary.listener.BluetoothMusicStateListener;
import com.etrans.jt.btlibrary.listener.BluetoothPhoneListener;
import com.etrans.jt.btlibrary.listener.BluetoothStateChangeListener;
import com.etrans.jt.btlibrary.module.BaseModule;
import com.etrans.jt.btlibrary.module.BluetoothModule;
import com.etrans.jt.btlibrary.module.BluetoothMusicModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneBookModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;
import com.txznet.sdk.TXZMusicManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 单元名称:PhonePresenter.java
 * Created by fuxiaolei on 2016/8/29.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/29.
 */
public class PhonePresenter extends BasePresenter implements IPhoneProxy, BluetoothStateChangeListener, BluetoothPhoneListener, BluetoothMusicStateListener {
    private static final String TAG = PhonePresenter.class.getSimpleName();
    private Context mContext;
    private IPhoneView iPhoneView;
    private BluetoothDevice bluetoothDevice;
    private List<ContactBean> mLstContact;
    private SeekBar mSeekBar;

    public PhonePresenter(Context _context, IPhoneView phoneView, SeekBar seekBar) {
        super(_context);
        mContext = _context;
        iPhoneView = phoneView;
        mSeekBar = seekBar;
        BluetoothMusicModule.getInstance().initDevice(seekBar);
//        BluetoothPhoneModule.getInstance().initPhoneService();
    }

    @Override
    protected List<BaseModule> getRefHandles() {
        List<BaseModule> modules = new ArrayList<BaseModule>();
        modules.add(BluetoothModule.getInstance());
        modules.add(BluetoothPhoneBookModule.getInstance());
        modules.add(BluetoothPhoneModule.getInstance());
        modules.add(BluetoothMusicModule.getInstance());
        return modules;
    }

    @Override
    public void dial(String phoneNum) {
        BluetoothPhoneModule.getInstance().dial(phoneNum);
    }

    @Override
    public void downloadPhoneBook() {
        //如果下载中则不再相应
        BluetoothPhoneModule.getInstance().downloadPhoneBook(true);
    }

    @Override
    public void play() {
        BluetoothMusicModule.getInstance().play();
        //TXZMusicManager.getInstance().play();
    }

    @Override
    public void stop() {
        BluetoothMusicModule.getInstance().stop();
//        TXZMusicManager.getInstance().pause();
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

    @Override
    public void initPhoneService() {
        BluetoothPhoneModule.getInstance().initPhoneService();
        BluetoothMusicModule.getInstance().initMusicService();
    }

    @Override
    public void pause() {
        BluetoothMusicModule.getInstance().pause();
    }

    @Override
    public void destory() {
        BluetoothMusicModule.getInstance().destory();
    }


    @Override
    public void onSetBluetoothHFDevice(BluetoothHfDevice bluetoothHFDevice) {
       /* if (bluetoothHFDevice.getConnectedDevices().size() > 0) {
            bluetoothDevice = bluetoothHFDevice.getConnectedDevices().get(0);
            iPhoneView.onUpdateConnectDeviceName(bluetoothDevice.getName());
        }*/
        iPhoneView.onUpdateConnectDeviceName(BluetoothPhoneModule.getInstance().getDeviceName());
    }

    @Override
    public void onUpdateSignalStatus(int status, int imageOffset) {

    }

    @Override
    public void onUpdateBatteryStatus(int status) {

    }

    @Override
    public void onUpdateCallTime(int times) {

    }

    @Override
    public void onUpdateUITalking() {

    }

    @Override
    public void onUpdateHangup() {

    }

    @Override
    public void onUpdateAudioState(int state) {

    }

    @Override
    public void onUpdateCallEx(String callNumber, String name) {

    }

    @Override
    public void onUpdateDownloadCount(String downLoadCount) {
        iPhoneView.onPhoneBookDownloadCount(downLoadCount);
    }

    @Override
    public void onChangeAudioEnable() {

    }

    /**
     * 查询数据库
     *
     * @param num
     */
    public void queryContact(String num) {
        ContactBean data = null;
        if (mLstContact == null) {
            mLstContact = new ArrayList<ContactBean>();
        }
        List<ContactBean> lstT9 = BluetoothPhoneBookModule.getInstance().queryContactByInitialT9(
                num);
        List<ContactBean> lstPy = BluetoothPhoneBookModule.getInstance().queryContactByPinyinT9(num);
        List<ContactBean> lstPhoneNumber = BluetoothPhoneBookModule.getInstance().queryContactListByNum(num);
        mLstContact.clear();
        HashSet<String> set = new HashSet<String>();
        if (lstT9 != null) {
            for (int i = 0; i < lstT9.size(); i++) {
                ContactBean bean = lstT9.get(i);
                if (bean == null) {
                    continue;
                }
                String str = bean.getName() + bean.getMobilePhone();
                set.add(str);
                mLstContact.add(bean);
            }
        }

        if (lstPy != null) {
            for (int i = 0; i < lstPy.size(); i++) {
                ContactBean bean = lstPy.get(i);
                if (bean == null) {
                    continue;
                }
                String str = bean.getName() + bean.getMobilePhone();
                if (set.contains(str))
                    continue;
                set.add(str);
                mLstContact.add(bean);
            }
        }

        if (lstPhoneNumber != null) {
            for (int i = 0; i < lstPhoneNumber.size(); i++) {
                ContactBean bean = lstPhoneNumber.get(i);
                if (bean == null) {
                    continue;
                }
                String str = bean.getName() + bean.getMobilePhone();
                if (set.contains(str))
                    continue;
                set.add(str);
                mLstContact.add(bean);
            }
        }
        if (mLstContact.size() > 0) {
            iPhoneView.notify(mLstContact);
            iPhoneView.showQueryList(true);
        } else {
            iPhoneView.showQueryList(false);
        }
    }

    //----------------------------蓝牙音乐相关回调----------------------------
    @Override
    public void onUpdateUI(long avrcpState) {
        if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_PLAYING) {
            iPhoneView.onMediaPlay();
            iPhoneView.startAnimation();
        } else if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_PAUSED) {
            iPhoneView.onMediaPause();
            iPhoneView.stopAnimation();
        } else if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_FWD_SEEK) {

        } else if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_REV_SEEK) {

        } else if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_STOPPED) {
            iPhoneView.onMediaPause();
            iPhoneView.stopAnimation();
        }
    }

    @Override
    public void onUpdateSeek(int currPos) {
        mSeekBar.setProgress(currPos);
//        Log.d(TAG, "onUpdateSeek: " + "currPos =========" + currPos + Log.getStackTraceString(new Throwable()));
        BluetoothMusicModule.getInstance().initDevice(mSeekBar);
    }

    @Override
    public void onUpdateCurrTime(String txt) {
        iPhoneView.onUpdateCurrTime(txt);
    }

    @Override
    public void onSongInfo(SongInfo songinfo) {
        iPhoneView.updateSongInfo(songinfo);
    }

    @Override
    public void onUpdateDuration(String duration) {
        iPhoneView.onUpdateDuration(duration);
    }

    @Override
    public void onBluetoothMusicClose() {
        iPhoneView.onClose();
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

    public void getConnectedDeviceName() {

        iPhoneView.onUpdateConnectDeviceName(BluetoothPhoneModule.getInstance().getDeviceName());
    }

    @Override
    public void onBluetoothOpen() {

    }

    @Override
    public void onBluetoothClose() {

    }

    @Override
    public void onBluetoothConnect() {

    }

    @Override
    public void onBluetoothDisConnect() {

    }

    @Override
    public void onUpdateConnectDeviceName(String deviceName) {
        iPhoneView.onUpdateConnectDeviceName(BluetoothPhoneModule.getInstance().getDeviceName());
    }
}
