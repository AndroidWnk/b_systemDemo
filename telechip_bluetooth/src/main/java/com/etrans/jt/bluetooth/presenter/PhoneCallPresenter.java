package com.etrans.jt.bluetooth.presenter;

import android.content.Context;
import android.media.AudioManager;

import com.broadcom.bt.hfdevice.BluetoothCallStateInfo;
import com.broadcom.bt.hfdevice.BluetoothHfDevice;
import com.etrans.jt.bluetooth.base.BasePresenter;
import com.etrans.jt.bluetooth.listener.proxy.IPhoneCallProxy;
import com.etrans.jt.bluetooth.listener.view.IPhoneCallView;
import com.etrans.jt.btlibrary.listener.BluetoothPhoneListener;
import com.etrans.jt.btlibrary.listener.BluetoothStateChangeListener;
import com.etrans.jt.btlibrary.module.BaseModule;
import com.etrans.jt.btlibrary.module.BluetoothModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneBookModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 单元名称:PhoneCallPresenter.java
 * Created by fuxiaolei on 2016/8/30.
 * 说明:
 * Last Change by fuxiaolei on 2016/8/30.
 */
public class PhoneCallPresenter extends BasePresenter implements IPhoneCallProxy, BluetoothPhoneListener,BluetoothStateChangeListener {
    private static final String TAG = PhoneCallPresenter.class.getSimpleName();
    private Context mContext;
    private IPhoneCallView phoneCallView;
    private final AudioManager ag;

    public PhoneCallPresenter(Context _context, IPhoneCallView iPhoneCallView) {
        super(_context);
        mContext = _context;
        phoneCallView = iPhoneCallView;
        ag = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected List<BaseModule> getRefHandles() {
        List<BaseModule> modules = new ArrayList<BaseModule>();
        modules.add(BluetoothModule.getInstance());
        modules.add(BluetoothPhoneBookModule.getInstance());
        modules.add(BluetoothPhoneModule.getInstance());
        return modules;
    }

    @Override
    public void answer() {
        BluetoothPhoneModule.getInstance().answer();
    }

    @Override
    public void hangup() {
        BluetoothPhoneModule.getInstance().hangup();
    }

    @Override
    public void setVolType(int type) {
        BluetoothPhoneModule.getInstance().setVolume(type, ag.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
    }

    @Override
    public void sendDTMFCode(CharSequence s, int start) {
        BluetoothPhoneModule.getInstance().sendDTMFCode(s, start);
    }

    @Override
    public void changeAudio() {
        BluetoothPhoneModule.getInstance().changeAudio();
    }

    @Override
    public void sendBIA(boolean b) {
        BluetoothPhoneModule.getInstance().sendBIA(b);
    }

    //-----------------------------底层回调-----------------------

    @Override
    public void onSetBluetoothHFDevice(BluetoothHfDevice bluetoothHFDevice) {

    }

    @Override
    public void onUpdateSignalStatus(int status, int imageOffset) {

    }

    @Override
    public void onUpdateBatteryStatus(int status) {

    }

    @Override
    public void onUpdateCallTime(int time) {
        String times = formatTimer(time);
        if (times == null || times.trim().length() == 0)
            return;
        phoneCallView.updateCallTime(times);
    }

    @Override
    public void onUpdateUITalking() {
        phoneCallView.updateTalking();
    }

    @Override
    public void onUpdateHangup() {
        phoneCallView.hangup();
    }

    @Override
    public void onUpdateAudioState(int state) {
        phoneCallView.updateAudioState(state);
    }

    @Override
    public void onUpdateCallEx(String callNumber, String name) {
        phoneCallView.updateCallEx(callNumber, name);
    }

    @Override
    public void onUpdateDownloadCount(String downLoadCount) {

    }

    @Override
    public void onChangeAudioEnable() {
        phoneCallView.updateChangeAudioEnable();
    }

    /**
     * 判断是否是挂断
     *
     * @param callInfo
     * @return
     */
    private boolean isPhoneOnHook(BluetoothCallStateInfo callInfo) {
        return ((callInfo.getNumActiveCall() == 0) &&
                (callInfo.getNumHeldCall() == 0) &&
                (callInfo.getCallSetupState() == BluetoothHfDevice.CALL_SETUP_STATE_IDLE));
    }

    public static String formatTimer(int times) {
        times++;
        if (times >= 3600) {
            long h = times / 3600;
            long tmp = times % 3600;
            long m = tmp / 60;
            tmp %= 60;

            return String.format("%02d:%02d:%02d", h, m, tmp);
        }

        long m = times / 60;
        long s = times % 60;
        return String.format("%02d:%02d", m, s);
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

    }
}
