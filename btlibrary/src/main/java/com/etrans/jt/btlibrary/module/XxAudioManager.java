package com.etrans.jt.btlibrary.module;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import com.etrans.jt.btlibrary.receiver.RemoteControlReceiver;
import com.linkgent.remoute.api.XxAudioConfig;
import com.linkgent.xxaidl.IXxAudioFocusChange;
import com.linkgent.xxaidl.IXxAudioService;

/**
 * 音频焦点管理
 * @author zhaodongping
 */
public class XxAudioManager {


    private ComponentName mMediaControlComponent;
    private AudioManager mAudioManager;

    public interface OnXxAudioFocusChange {
        /**
         * @param {@link #AUDIOFOUCS_PAUSE} {@link #AUDIOFOUCS_STOP}
         *               {@link #AUDIOFOUCS_RESUME} {@link #AUDIOFOUCS_LOWERVOLUME}
         *               {@link #AUDIOFOUCS_RESUMEVOLUME}
         */
        public void onChange(int state);
    }

    /**
     * 未知
     */
    public static final int MODULE_UNKNOWN = XxAudioConfig.MODULE_UNKNOW;
    /**
     * 音乐模块
     */
    public static final int MODULE_MUSIC = XxAudioConfig.MODULE_MUSIC;
    /**
     * 收音机模块
     */
    public static final int MODULE_RADIO = XxAudioConfig.MODULE_RADIO;

    /**
     * 蓝牙模块
     */
    public static final int MODULE_BLUETOOTHMUSIC = XxAudioConfig.MODULE_BTEARMUSIC;

    /**
     * 电话
     */
    public static final int MODULE_PHONE = XxAudioConfig.MODULE_PHONE;

    /**
     * TTS模块
     */
    public static final int MODULE_TTS = XxAudioConfig.MODULE_TTS;

	/**
	 * 暂停
	 */
	public static final int AUDIOFOUCS_PAUSE = XxAudioConfig.AUDIOFOUCS_PAUSE;
	/**
	 * 停止
	 */
	public static final int AUDIOFOUCS_STOP = XxAudioConfig.AUDIOFOUCS_STOP;
	/**
	 * 继续播放
	 */
	public static final int AUDIOFOUCS_RESUME = XxAudioConfig.AUDIOFOUCS_RESUME;
	/**
	 * 降低音量播放
	 */
	public static final int AUDIOFOUCS_LOWERVOLUME = XxAudioConfig.AUDIOFOUCS_LOWERVOLUME;
	/**
	 * 恢复音量播放
	 */
	public static final int AUDIOFOUCS_RESUMEVOLUME = XxAudioConfig.AUDIOFOUCS_RESUMEVOLUME;


    public static final int AUDIOFOUCS_START_PLAY = XxAudioConfig.AUDIOFOUCS_RESTART_PLAY;


    public static final int AUDIOFOCUS_REQUEST_FAILED = XxAudioConfig.AUDIOFOCUS_REQUEST_FAILED;
    public static final int AUDIOFOCUS_REQUEST_GRANTED = XxAudioConfig.AUDIOFOCUS_REQUEST_GRANTED;
    public static final int AUDIOFOCUS_REQUEST_LOWVOLUME_PLAY = XxAudioConfig.AUDIOFOCUS_REQUEST_LOWVOLUME_PLAY;
    public static final int AUDIOFOCUS_REQUEST_PHONE = XxAudioConfig.AUDIOFOCUS_REQUEST_PHONE;
    public static final int AUDIOFOCUS_REQUEST_BTEARPHONE = XxAudioConfig.AUDIOFOCUS_REQUEST_BTEARPHONE;


    private SparseArray<OnXxAudioFocusChange> mSpArrayModuleName = new SparseArray<OnXxAudioFocusChange>();

    private IXxAudioService mAudioService;

    private Context mContext;

    static final class XxAduioManagerHolder {
        static XxAudioManager instance = new XxAudioManager();
    }

    private XxAudioManager() {
    }

    public static XxAudioManager getInstance() {
        return XxAduioManagerHolder.instance;
    }

    public void init(Context context) {
        mContext = context;
        bind();
    }

    public void release() {
        if (mAudioService != null) {

            try {
                mAudioService.removeIXxAudioFocusChange(XxAudioConfig.MODULE_BTEARMUSIC);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @param module 哪个模块
     * @param l
     */
    public boolean addListener(int module, OnXxAudioFocusChange l) {
        if (l == null)
            return false;
        if (mSpArrayModuleName.get(module) != null)
            return true;
        mSpArrayModuleName.put(module, l);
        return true;
    }

    public void initRecv() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        }

        if (mMediaControlComponent == null) {
            mMediaControlComponent = new ComponentName(mContext, RemoteControlReceiver.class);
        }
        try {
            mAudioManager.unregisterMediaButtonEventReceiver(mMediaControlComponent);
        } catch (Exception e) {

        }
        mAudioManager.registerMediaButtonEventReceiver(mMediaControlComponent);

    }

    public void removeListener(int moudle) {
        mSpArrayModuleName.remove(moudle);
    }

    /**
     * 请求焦点
     *
     * @param
     * @param streamType
     * @param durationHint
     * @return
     */
    public int requestAudioFocus(int module, int streamType, int durationHint) {
        initRecv();

        if (mAudioService == null)
            return XxAudioConfig.AUDIOFOCUS_REQUEST_FAILED;
        try {
            return mAudioService.requestAudioFocus(module, streamType, durationHint);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return XxAudioConfig.AUDIOFOCUS_REQUEST_FAILED;
    }

    public int abandonAudioFocus(int module) {
        if (mAudioService == null)
            return 0;
        try {
            return mAudioService.abandonAudioFocus(module);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前音频焦点的module  by heyaobao 2017/03/02
     * @return
     */
    public int getCurrentStreamModule() {
        if (mAudioService != null)
            try {
                return mAudioService.getCurrentStreamModule();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        return XxAudioConfig.MODULE_MUSIC;
    }

    private IXxAudioFocusChange mBtearMusicListener = new IXxAudioFocusChange.Stub() {

        @Override
        public void onAudioFocusChange(int focus) throws RemoteException {
            Message msg = mHandler.obtainMessage();
            msg.what = MSG_BTEARMUSIC;
            msg.arg1 = focus;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onCheck() throws RemoteException {
        }
    };


    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAudioService = IXxAudioService.Stub.asInterface(service);
            Log.i("xxbtrea", mBtearMusicListener.hashCode() + " blutooth");
            try {
                mAudioService.addIXxAudioFocusChange(XxAudioConfig.MODULE_BTEARMUSIC, mBtearMusicListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAudioService = null;
            bind();
        }
    };

    private void bind() {
        Intent service = new Intent(XxAudioConfig.AUDIO_SERVICE);
        mContext.bindService(service, con, Context.BIND_AUTO_CREATE);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_BTEARMUSIC) {
                OnXxAudioFocusChange l = mSpArrayModuleName.get(MODULE_BLUETOOTHMUSIC);
                if (l != null) {
                    l.onChange(msg.arg1);
                }
            }
        }
    };

    private final static int MSG_BTEARMUSIC = 1;
}
