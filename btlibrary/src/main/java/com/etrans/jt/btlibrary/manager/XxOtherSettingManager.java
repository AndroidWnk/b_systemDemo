package com.etrans.jt.btlibrary.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import com.etrans.jt.btlibrary.R;
import com.etrans.jt.btlibrary.domin.IVoiceAssist;
import com.etrans.jt.btlibrary.domin.VoiceParams;
import com.etrans.jt.btlibrary.domin.XxBroadcastConfig;
import com.etrans.jt.btlibrary.domin.XxResourceString;
import com.etrans.jt.btlibrary.utils.XxLightUtils;
import com.etrans.jt.btlibrary.utils.XxMessage;
import com.etrans.jt.btlibrary.utils.XxVoiceUtils;


public class XxOtherSettingManager extends XxBaseModule implements IVoiceAssist {
    private XxVoiceUtils mVoiceUtils;


    private XxLightUtils mLightUtils;

    private boolean btIsOpen = false;
    private boolean btConnect = false;
    private BluetoothAdapter adapter;
    private final MediaPlayer mediaPlayer;

    public XxVoiceUtils getVoiceUtils() {
        return mVoiceUtils;
    }

    public XxLightUtils getLightUtils() {
        return mLightUtils;
    }

    public void onConnect(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        //TODO 发送消息 刷新界面
        btConnect = true;

    }

    public void onDisConnect() {
        btConnect = false;
    }

    public boolean isBtConnect() {
        return btConnect;
    }

    public void setBtConnect(boolean btConnect) {
        this.btConnect = btConnect;
    }

    public void BtOpen() {
        btIsOpen = true;
        Message.obtain(mHandler, XxMessage.MSG_BTEARPHONE_ON).sendToTarget();
    }

    public void BtClose() {
        btIsOpen = false;
        Message.obtain(mHandler, XxMessage.MSG_BTEARPHONE_OFF).sendToTarget();
    }


    private static class XxOtherSettingManagerInstance {
        private static final XxOtherSettingManager mOtherSettingManager = new XxOtherSettingManager();
    }

    public static XxOtherSettingManager getInstance() {
        return XxOtherSettingManagerInstance.mOtherSettingManager;
    }

    public XxOtherSettingManager() {
        mVoiceUtils = new XxVoiceUtils();
        mLightUtils = new XxLightUtils();
        mediaPlayer = new MediaPlayer();
        initBluetoothState();
    }

    public boolean isBtIsOpen() {
        return btIsOpen;
    }

    public void setBtIsOpen(boolean btIsOpen) {
        this.btIsOpen = btIsOpen;
    }

    private void initBluetoothState() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            boolean enabled = adapter.isEnabled();
            if (enabled) {
                btIsOpen = true;
            } else {
                btIsOpen = false;
            }
        }
    }

    /**
     * 打开蓝牙
     */
    public void openOrClose() {
        if (btIsOpen) {
            adapter.disable();
        } else {
            adapter.enable();
        }
    }

    public void setVolume(float left, float right) {
        mediaPlayer.setVolume(left, right);
    }


    public XxOtherSettingManager init(Context context) {
        super.init(context);
        mVoiceUtils.init(context);
        if (XxConfig.getInstance().isVoiceAutoMute()) {
            mVoiceUtils.setStreamMute(AudioManager.STREAM_MUSIC, true);
        } else {
            mVoiceUtils.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }
        mLightUtils.init(context);
        context.getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS),
                true, mBrightnessObserver);

        context.getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE),
                true, mBrightnessModeObserver);
        return this;
    }

    public void initDefaultStatus() {
        int defaultVolume = (int) (mVoiceUtils.getMaxVolume(AudioManager.STREAM_MUSIC) * 0.6);
        mVoiceUtils.setStreamVoice(AudioManager.STREAM_MUSIC, defaultVolume);

        defaultVolume = (int) (mVoiceUtils.getMaxVolume(AudioManager.STREAM_RADIO) * 0.6);
        mVoiceUtils.setStreamVoice(AudioManager.STREAM_RADIO, defaultVolume);

        defaultVolume = (int) (mVoiceUtils.getMaxVolume(AudioManager.STREAM_VOICE_CALL) * 0.6);
        mVoiceUtils.setStreamVoice(AudioManager.STREAM_VOICE_CALL, defaultVolume);

        //defaultVolume = (int) (mVoiceUtils.getMaxVolume(AudioManager.STREAM_BTEARPHONE) * 0.6);
        //mVoiceUtils.setStreamVoice(AudioManager.STREAM_BTEARPHONE, defaultVolume);

        int defaultLight = (int) (XxLightUtils.MAX_BRIGHTNESS * 0.6);
        mLightUtils.setSysScreenBrightness(defaultLight);
    }


    private boolean gbMute;

    public boolean isMute() {
        return gbMute;
    }

    public void changeVoiceStatus(boolean bmute) {
        gbMute = bmute;
//		if(bmute) {
//			mVoiceUtils.setStreamMute(AudioManager.STREAM_MUSIC, bmute);
//			mVoiceUtils.setStreamMute(AudioManager.STREAM_RADIO, bmute);
//		}else {
//			mVoiceUtils.setStreamMute(AudioManager.STREAM_MUSIC, bmute);
//			mVoiceUtils.setStreamMute(AudioManager.STREAM_RADIO, bmute);
//
//		}
//		if(!XxConfig.getInstance().getMusicMuteByOper()) {
//			mVoiceUtils.setStreamMute(arg1, true);
//			XxConfig.getInstance().setMusicMuteByOper(true);
//		}else {
//			mVoiceUtils.setStreamMute(arg1, false);
//			XxConfig.getInstance().setMusicMuteByOper(false);
//		}
        Message msg = mHandler.obtainMessage(XxMessage.MSG_SETTING_RE_VOICE);
        VoiceParams params = new VoiceParams();
//		if(XxConfig.getInstance().getMusicMuteByOper()) {
//			params.bMute = 0 ;
//		}else {
//			params.bMute = 1;
//		}
        if (bmute) {
            params.bMute = 0;
        } else {
            params.bMute = 1;
        }
        params.refreshType = AudioManager.STREAM_MUSIC;
        params.musicVolume = getStreamVolume(AudioManager.STREAM_MUSIC);
        msg.obj = params;
        mHandler.sendMessage(msg);

        Message msg1 = mHandler.obtainMessage(XxMessage.MSG_SETTING_RE_VOICE);
        VoiceParams params1 = new VoiceParams();
        params1.refreshType = AudioManager.STREAM_RADIO;
        params1.radioVolume = getStreamVolume(AudioManager.STREAM_RADIO);
        msg1.obj = params1;
        mHandler.sendMessage(msg1);
    }

    public int getStreamVolume(int stream) {
        return mVoiceUtils.getStreamVoice(stream);
    }

    public boolean isMusicActive() {
        return mVoiceUtils.isMusicActive();
    }

    private ContentObserver mBrightnessModeObserver = new ContentObserver(
            new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            int mode = mLightUtils.getBrightnessMode();
            boolean bFlag = false;
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                bFlag = true;
            }

            Message msg = mHandler.obtainMessage(XxMessage.MSG_SETTING_RE_LIGHT_AUTO);
            msg.obj = bFlag;
            mHandler.sendMessage(msg);
        }
    };


    private ContentObserver mBrightnessObserver = new ContentObserver(
            new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            int oldBrightness = mLightUtils.getSysScreenBrightness(); // 当前系统的亮度
            int progress = oldBrightness - XxLightUtils.MIN_BRIGHTNESS; // SeekBar的值范围：0~225，代表的亮度值是30~255。
            refreshUI(XxMessage.MSG_SETTING_RE_LIGHT, progress < 0 ? 0 : progress, 0);
        }
    };


    public void refreshUI(int refreshType, int arg1, int arg2) {
        Message msg = mHandler.obtainMessage(refreshType);
        if (refreshType == XxMessage.MSG_SETTING_RE_VOICE) {
            VoiceParams params = new VoiceParams();
            if (arg2 == AudioManager.STREAM_MUSIC) {
                mVoiceUtils.setStreamMute(arg2, false);
                XxConfig.getInstance().setMusicMuteByOper(false);
                params.musicVolume = arg1;
                params.refreshType = AudioManager.STREAM_MUSIC;
                if (XxConfig.getInstance().getMusicMuteByOper()) {
                    params.bMute = 0;
                } else {
                    params.bMute = 1;
                }
            } else if (arg2 == AudioManager.STREAM_RADIO) {
                params.radioVolume = arg1;
                params.refreshType = AudioManager.STREAM_RADIO;
            } else if (arg2 == AudioManager.STREAM_VOICE_CALL /*|| arg2 == AudioManager.STREAM_BTEARPHONE*/) {
                //if(arg2 == AudioManager.STREAM_VOICE_CALL) {
                //if(arg1 !=  (mVoiceUtils.getStreamVoice(AudioManager.STREAM_BTEARPHONE))) {
                //	mVoiceUtils.setStreamVolume(AudioManager.STREAM_VOICE_CALL, arg1, 0);
                //}

                //}

/*				if(arg2 == AudioManager.STREAM_BTEARPHONE) {
                    if((arg1 /3) !=  mVoiceUtils.getStreamVoice(AudioManager.STREAM_VOICE_CALL)) {
						mVoiceUtils.setStreamVolume(AudioManager.STREAM_VOICE_CALL, arg1 /3, 0);
					}
				}*/
                params.callVolume = arg1;
                params.refreshType = AudioManager.STREAM_VOICE_CALL;

            }
            msg.obj = params;
            mHandler.sendMessage(msg);
        } else {
            msg.arg1 = arg1;
            mHandler.sendMessage(msg);
        }
    }

    public int getMaxVolumeByStream(int streamType) {
        return mVoiceUtils.getMaxVolume(streamType);
    }

    public void sendMuteBroadcast(boolean bMute) {
        Intent intent = new Intent();
        intent.setAction("android.vehicle.system.MUTE");
        intent.putExtra("bMute", bMute);
        mContext.sendBroadcast(intent);

    }


    public void setVolumeByStream(int streamType, int volume, boolean bCannelMute) {
        if (gbMute && bCannelMute) {
            sendMuteBroadcast(!gbMute);
        }
        mVoiceUtils.setStreamVoice(streamType, volume);
    }

    public void setVolumeByStream(int streamType, int volume) {
        setVolumeByStream(streamType, volume, true);
    }

    public void refreshBG() {
        Message msg = mHandler.obtainMessage(XxMessage.MSG_SETTING_RE_BG);
        mHandler.sendMessage(msg);
    }


    @Override
    public void onVoiceCmd(String cmd, Bundle bundle) {
        if (cmd == null)
            return;
        if (bundle == null) {
            return;
        }
        String name = bundle.getString(XxBroadcastConfig.CMDContral.EXTRA_NAME);
        if (name == null)
            return;
        if (XxResourceString.getInstance().getResStringSet(R.array.voice_ctrl_volume_mute).contains(name)) {
            gbMute = true;
            sendMuteBroadcast(gbMute);
        } else if (XxResourceString.getInstance().getResStringSet(R.array.voice_ctrl_volume_open).contains(name)) {
            gbMute = false;
            sendMuteBroadcast(gbMute);
        } else if (XxResourceString.getInstance().getResStringSet(R.array.voice_ctrl_volume_more).contains(name)) {
            highVolume();
        } else if (XxResourceString.getInstance().getResStringSet(R.array.voice_ctrl_volume_less).contains(name)) {
            lowerVolume();
        } else if (name.equals(XxBroadcastConfig.CMDContral.CMD_SET_VOLUME)) {
            int val = bundle.getInt(XxBroadcastConfig.CMDContral.EXTRA_VALUE);
            if (val >= 100) {
                setVolume(val / 100);
            } else {
                int volume = mVoiceUtils.getMaxVolume(AudioManager.STREAM_MUSIC);
                volume = (int) (volume * (val / 100.0));
                setVolume(volume);
            }
        }
    }

    void setVolume(int val) {
        int maxVolume = mVoiceUtils.getMaxVolume(AudioManager.STREAM_MUSIC);
        if (val > maxVolume) {
            val = maxVolume;
        }
        if (val < 0) {
            val = 0;
        }
        mVoiceUtils.setStreamVolume(AudioManager.STREAM_MUSIC, val, 0);
    }

    void highVolume() {
        /*if (XxRadioManager.getInstance().getRadioInfo().getState() != XxRadioConst.State.OFF) {
            int volume = mVoiceUtils.getStreamVoice(AudioManager.STREAM_RADIO);
            int maxVolume = mVoiceUtils.getMaxVolume(AudioManager.STREAM_RADIO);
            volume++;
            if (volume > maxVolume)
                return;
            mVoiceUtils.setStreamVolume(AudioManager.STREAM_RADIO, volume, 0);  //音乐音量
        } else {
            int volume = mVoiceUtils.getStreamVoice(AudioManager.STREAM_MUSIC);
            int maxVolume = mVoiceUtils.getMaxVolume(AudioManager.STREAM_MUSIC);
            volume++;
            if (volume > maxVolume)
                return;
            mVoiceUtils.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);  //音乐音量
        }*/
    }

    void lowerVolume() {
        /*if (XxRadioManager.getInstance().getRadioInfo().getState() != XxRadioConst.State.OFF) {
            int volume = mVoiceUtils.getStreamVoice(AudioManager.STREAM_RADIO);
            volume--;
            if (volume < 0)
                return;
            mVoiceUtils.setStreamVolume(AudioManager.STREAM_RADIO, volume, 0);  //音乐音量
        } else {
            int volume = mVoiceUtils.getStreamVoice(AudioManager.STREAM_MUSIC);
            volume--;
            if (volume < 0)
                return;
            mVoiceUtils.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);  //音乐音量
        }*/
    }

    void adjustVolume(int streamType, int volume, int flag) {
        int maxVolume;
        int curVolume = volume;
        maxVolume = mVoiceUtils.getMaxVolume(streamType);
        if (curVolume > maxVolume)
            curVolume = maxVolume;
        if (curVolume < 0)
            curVolume = 0;

        mVoiceUtils.setStreamVolume(streamType, curVolume, flag);
    }

    public void refeshRotate() {
        mHandler.sendEmptyMessage(XxMessage.MSG_SYSTEM_ROTATE);
    }
}
