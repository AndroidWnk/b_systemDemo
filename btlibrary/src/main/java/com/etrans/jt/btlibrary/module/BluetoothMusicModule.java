package com.etrans.jt.btlibrary.module;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.SeekBar;

import com.broadcom.bt.avrcp.BluetoothAvrcpBrowseItem;
import com.broadcom.bt.avrcp.BluetoothAvrcpController;
import com.broadcom.bt.avrcp.IBluetoothAvrcpControllerEventHandler;
import com.etrans.jt.btlibrary.domin.SongInfo;
import com.etrans.jt.btlibrary.listener.AvrcpCommandCallback;
import com.etrans.jt.btlibrary.listener.BluetoothMusicStateListener;
import com.etrans.jt.btlibrary.utils.Constants;
import com.etrans.jt.btlibrary.utils.FormatPlayTime;

import java.nio.ByteBuffer;
import java.util.List;


/**
 * 单元名称:BluetoothMusicModule.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class BluetoothMusicModule extends BaseModule<BluetoothMusicStateListener> {

    private static final String TAG = BluetoothMusicModule.class.getSimpleName();
    private Context mContext;
    private static BluetoothMusicModule instance;
    private BluetoothDevice mConnectedDevice;
    private BluetoothAvrcpController avrcp;
    private AvrcpCommandManager mCmdController;
    long avrcpState = BluetoothAvrcpController.PLAY_STATUS_STOPPED;
    String array_nowPlayingItems[] = new String[0];
    BluetoothAvrcpBrowseItem[] mMediaPlayers;
    BluetoothAvrcpBrowseItem[] mNowPlayingItems;
    BluetoothAvrcpBrowseItem mSelectedNowItem;
    private long mPosition, mDuration;
    private SongInfo songInfo;
    private boolean proxy;
    private int mBtMusicVolume;
    int count = 0;
    private AudioManager mAudioManager;
    private ComponentName mComponentName;
    private boolean getPlayState = false;
    private BluetoothA2dp mA2dp;
    private int streamVolume;
    private String leftLayoutChangeAction = "com.etrans.DesktopActivity.LeftLayoutChangeAction";
    private BluetoothMusicModule() {
        XxAudioManager.getInstance().addListener(
                XxAudioManager.MODULE_BLUETOOTHMUSIC, audioListener);
    }

    public static BluetoothMusicModule getInstance() {
        synchronized (BluetoothMusicModule.class) {
            if (instance == null) {
                instance = new BluetoothMusicModule();
            }
            return instance;
        }
    }

    // 是否真正播放蓝牙音乐
    boolean misPlaying = false;

    // 是否是拨打电话
    public void init(Context context) {
        songInfo = new SongInfo();
        mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        // AudioManager注册一个MediaButton对象
//        mComponentName = new ComponentName(mContext.getPackageName(), MediaButtonReceiver.class.getName());
//        mAudioManager.registerMediaButtonEventReceiver(mComponentName);
        if (BluetoothModule.getInstance().isBtConnect()) {
            initMusicService();
        }
    }


    public void initMusicService() {
        Log.d(TAG, "initMusicService() called  listener = " + listener);
        if (!proxy) {
            proxy = BluetoothAvrcpController.getProxy(mContext, listener);
        }
    }

    /**
     * play  pause
     */
    public void play() {
        initMusicService();
        if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_STOPPED
                || avrcpState == BluetoothAvrcpController.PLAY_STATUS_PAUSED) {
            //播放音乐
            avrcp.play(mConnectedDevice);
            mContext.sendBroadcast(new Intent("com.foton.btmusic.play"));
        } else {
            //暂停
            avrcp.pause(mConnectedDevice);
            mContext.sendBroadcast(new Intent("com.foton.btmusic.pause"));
        }
    }

    /**
     * pause
     */
    public void musicPause() {
        if (avrcp != null) {
            if (mConnectedDevice != null) {
                avrcp.pause(mConnectedDevice);
            }
        }
    }

    /*
     * 停止播放
     */
    public void stop() {
        avrcp.stop(mConnectedDevice);
    }

    /**
     * 下一曲
     */
    public void forward() {
        avrcp.forward(mConnectedDevice);
    }


    /**
     * 上一曲
     */
    public void backward() {
        avrcp.backward(mConnectedDevice);
    }

    /**
     * 快退
     *
     * @param isPressed 判断是否是按住不动的状态
     */
    public void rewind(boolean isPressed) {
        avrcp.rewind(mConnectedDevice, isPressed);
    }

    /**
     * 快进
     *
     * @param isPressed 判断按住的状态
     */
    public void fastforward(boolean isPressed) {
        avrcp.fastforward(mConnectedDevice, isPressed);
    }

    /**
     * 选择播放模式
     *
     * @param attrId BluetoothAvrcpController.ATTRIBUTE_REPEAT 循环播放
     *               BluetoothAvrcpController.ATTRIBUTE_SHUFFLE 随机播放
     */
    public void playMode(byte attrId) {
        byte val = selectAttributeValue(attrId);
        avrcp.setPlayerApplicationSettingValue(mConnectedDevice,
                new byte[]{attrId},
                new byte[]{val});
    }


    public void initDevice(SeekBar seekBar) {
//        this.mSeekBar = seekBar;
//        mSeekBar.setOnSeekBarChangeListener(mSeekListener);
    }

    BluetoothProfile.ServiceListener listener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceDisconnected(int profile) {
            Log.d(TAG, "onServiceDisconnected()");
            if (BluetoothProfile.A2DP == profile) {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.A2DP, mA2dp);
                mA2dp = null;
                Log.e(TAG, "BluetoothProfile.A2DP.ServiceListener.onServiceDisconnected");

            } else if (BluetoothProfile.AVRCP_CT == profile) {
                mConnectedDevice = null;
                if (null != avrcp) {
                    avrcp.unregisterEventHandler();
                    avrcp.closeProxy();
//                    avrcp = null;
                }
                Log.e(TAG, "BluetoothProfile.AVRCP_CT.ServiceListener.onServiceDisconnected");
            }
        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {

            if (BluetoothProfile.A2DP == profile) {
                mA2dp = (BluetoothA2dp) proxy;
                Log.i(TAG, "BluetoothProfile.A2DP.ServiceListener.onServiceConnected");
            }
            avrcp = (BluetoothAvrcpController) proxy;
            if (null != avrcp) {
                avrcp.registerEventHandler(eventHandler);
                if (mConnectedDevice == null)
                    mHandler.sendEmptyMessage(Constants.LOAD_CONN_DEV_REQUEST);
                else {
                    mHandler.sendEmptyMessage(Constants.START_AVRC_REQUEST);
                    if ((avrcp.getTargetFeatures(mConnectedDevice) & avrcp.FEATURE_BROWSE) > 0) {
                        mHandler.sendEmptyMessage(Constants.GET_MEDIA_PLAYER_LIST);
                    }
                }
            }
        }
    };

    IBluetoothAvrcpControllerEventHandler eventHandler = new IBluetoothAvrcpControllerEventHandler() {

        //监听avrcp连接状态

        /**
         * AVRCP连接状态变化
         * @param target   与连接状态的变化相关联的目标设备
         * @param newState STATE_DISCONNECTED，STATE_CONNECTING， STATE_CONNECTED，STATE_DISCONNECTING
         *                 如果返回的是false 则这些参数是无效的
         * @param success  true 连接成功 false 连接失败
         */
        @Override
        public void onConnectionStateChange(BluetoothDevice target,
                                            int newState, boolean success) {
            if (mCmdController != null)
                mCmdController.onConnectionStateChange(target, newState,
                        success);

            mConnectedDevice = target;
        }

        /**
         *
         * @param target        与连接状态的变化相关联的目标设备
         * @param attributeId
         * @param status        AVRCP响应错误/状态代码
         */
        @Override
        public void onListPlayerApplicationSettingAttributesRsp(
                BluetoothDevice target, byte[] attributeId, int status) {
            if (mCmdController != null)
                mCmdController.onListPlayerApplicationSettingAttributesRsp(
                        target, attributeId, status);

        }

        /**
         *
         * @param target        与连接状态的变化相关联的目标设备
         * @param attrId
         * @param valueId
         * @param status
         */
        @Override
        public void onListPlayerApplicationSettingValuesRsp(
                BluetoothDevice target, byte attrId, byte[] valueId,
                int status) {
            Log.d(TAG, "onListPlayerApplicationSettingValuesRsp() " + attrId);
            if (mCmdController != null)
                mCmdController.onListPlayerApplicationSettingValuesRsp(target,
                        attrId, valueId, status);

        }

        @Override
        public void onGetCurrentPlayerApplicationSettingValueRsp(
                BluetoothDevice target, byte[] attributeId, byte[] valueId,
                int status) {
            Log.d(TAG, "onGetCurrentPlayerApplicationSettingValueRsp() ");
            if (mCmdController != null)
                mCmdController.onGetCurrentPlayerApplicationSettingValueRsp(
                        target, attributeId, valueId, status);

        }

        @Override
        public void onGetPlayerApplicationSettingAttributeTextRsp(
                BluetoothDevice target, byte[] attributeId,
                String[] attributeText, int status) {
            Log.d(TAG, "onGetPlayerApplicationSettingAttributeTextRsp() ");
            if (mCmdController != null)
                mCmdController.onGetPlayerApplicationSettingAttributeTextRsp(
                        target, attributeId, attributeText,
                        status);

        }

        @Override
        public void onGetPlayerApplicationSettingValueTextRsp(
                BluetoothDevice target, byte attributeId, byte[] valueId,
                String[] valueText, int status) {
            Log.d(TAG, "onGetPlayerApplicationSettingValueTextRsp() " + attributeId);
            if (mCmdController != null)
                mCmdController.onGetPlayerApplicationSettingValueTextRsp(
                        target, attributeId, valueId, valueText,
                        status);

        }

        @Override
        public void onGetElementAttributesRsp(BluetoothDevice target,
                                              int[] attributeId, String[] valueText,
                                              int status) {
            if (mCmdController != null)
                mCmdController.onGetElementAttributesRsp(target, attributeId,
                        valueText, status);
            if (isBluetoothMusicPlaying()) {
                sendPlayBtMusicBroadCast(1);//切歌
            }
        }

        @Override
        public void onGetPlayStatusRsp(BluetoothDevice target, int songLength,
                                       int songPosition, byte playStatus, int status) {
//                    Log.d(TAG, "onGetPlayStatusRsp() songLength:" + songLength +
//                            ", songPosition:" + songPosition + ", status:" + status);
            if (isStatusPlaying(playStatus) && getPlayState) {
                if (!"tcc897xpm01v2".equals(SystemProperties.get("ro.product.device")) && !"tcc897xzd2sv2".equals(SystemProperties.get("ro.product.device"))) {
                    requestAudioFoucs();
                }
                getPlayState = false;
            }
            if (!BluetoothAvrcpController.isSuccess(status)) {
                Log.d(TAG, "Failed to get Play status. Do not update UI");
                return;
            }
            if (mCmdController != null)
                mCmdController.onGetPlayStatusRsp(target, songLength,
                        songPosition, playStatus, status);
//                    Log.d(TAG, "onGetPlayStatusRsp() : " + avrcpState + " >>> " + (long) playStatus);

            mPosition = songPosition;
            Message msg = mHandler.obtainMessage(Constants.CMD_UPDATE_PROGRESS);
            msg.arg1 = songPosition;
            msg.arg2 = songLength;
            mHandler.sendMessage(msg);
            if (avrcpState != (long) playStatus) {
                avrcpState = (long) playStatus;
                mHandler.sendEmptyMessage(Constants.CMD_RELOAD_UI);
                // If play state is playing, get the play state value.
                mHandler.removeMessages(Constants.GET_ELEMENT_ATTR_REQUEST);
                mHandler.sendEmptyMessage(Constants.GET_ELEMENT_ATTR_REQUEST);
                return;
            }
            mHandler.sendEmptyMessage(Constants.CMD_ALTERNATE_UPDATE_TIMER);
            mHandler.sendEmptyMessage(Constants.CMD_RELOAD_METADATA);
            mHandler.sendEmptyMessage(Constants.CMD_RELOAD_UI);
//                    Log.d(TAG, "onGetPlayStatusRsp() called with: " + "target = [" + target + "], songLength = [" + songLength + "], songPosition = [" + songPosition + "], playStatus = [" + playStatus + "], status = [" + status + "]");

        }

        /**
         * 播放状态改变监听
         * @param target        当前设备
         * @param playStatus    播放参数   暂停 播放 停止 快进 快退 上一曲/下一曲时回调这个方法
         */
        @Override
        public void onPlaybackStatusChanged(BluetoothDevice target,
                                            byte playStatus) {
            Log.d("XXXA", "onPlaybackStatusChanged() " + avrcpState + " >>> " + (long) playStatus);
            boolean hasPlayStatusChanged = (avrcpState == (long) playStatus);
            boolean hasPlayStatusRestarted =
                    (avrcpState == BluetoothAvrcpController.PLAY_STATUS_STOPPED &&
                            playStatus == BluetoothAvrcpController.PLAY_STATUS_PLAYING);
            boolean hasPlayStatusContinued =
                    (avrcpState == BluetoothAvrcpController.PLAY_STATUS_PAUSED &&
                            playStatus == BluetoothAvrcpController.PLAY_STATUS_PLAYING);
            avrcpState = (long) playStatus;
            mHandler.sendEmptyMessage(Constants.CMD_RELOAD_UI);
            // If play state is playing, get the play state value.
            if (isStatusPlaying(avrcpState)) {
                Log.d("XXXA", "onPlaybackStatusChanged() called with: requestAudioFoucs();");
                if (!"tcc897xpm01v2".equals(SystemProperties.get("ro.product.device")) && !"tcc897xzd2sv2".equals(SystemProperties.get("ro.product.device"))) {
                    requestAudioFoucs();
                }
                //通知中控：蓝牙音乐播放
                mContext.sendBroadcast(new Intent("com.foton.btmusic.play"));
                mHandler.sendEmptyMessage(Constants.GET_PLAYER_STATUS_REQUEST);
                mHandler.sendEmptyMessageDelayed(Constants.GET_ELEMENT_ATTR_REQUEST, 100);
                if (hasPlayStatusRestarted && !hasPlayStatusContinued) {
                    for (BluetoothMusicStateListener listener : listeners) {
                        listener.onUpdateSeek(0);
//                                Log.d("onUpdateSeek", "onPlaybackStatusChanged1");
                    }
                }
                mHandler.sendEmptyMessageDelayed(Constants.CMD_RELOAD_PROGRESS, 1000);
                mHandler.sendEmptyMessageDelayed(Constants.CMD_ALTERNATE_UPDATE_TIMER, 1000);
            } else {
                Log.d("XXXA", "onPlaybackStatusChanged() called with: abandonAudioFocus();");
                if (!"tcc897xpm01v2".equals(SystemProperties.get("ro.product.device")) && !"tcc897xzd2sv2".equals(SystemProperties.get("ro.product.device"))) {
                    abandonAudioFocus();
                }
                //通知中控：蓝牙音乐暂停
                mContext.sendBroadcast(new Intent("com.foton.btmusic.pause"));
                if (avrcpState == BluetoothAvrcpController.PLAY_STATUS_STOPPED) {
                    for (BluetoothMusicStateListener listener : listeners) {
                        listener.onUpdateSeek(0);
//                                Log.d("onUpdateSeek", "onPlaybackStatusChanged2");
                    }
                }
                mHandler.removeMessages(Constants.CMD_RELOAD_PROGRESS);
                mHandler.removeMessages(Constants.CMD_UPDATE_PROGRESS);
                mHandler.removeMessages(Constants.CMD_ALTERNATE_UPDATE_TIMER);
            }
            if (playStatus != 0) {
                sendPlayBtMusicBroadCast(playStatus);
            }
        }

        @Override
        public void onTrackChanged(BluetoothDevice target, long trackId) {
            mHandler.sendEmptyMessage(Constants.GET_ELEMENT_ATTR_REQUEST);
            for (BluetoothMusicStateListener listener : listeners) {
                listener.onUpdateSeek(0);
                mPosition = 0;
//                        Log.d("onUpdateSeek", "onTrackChanged");
            }
            mHandler.sendEmptyMessageDelayed(Constants.CMD_RELOAD_PROGRESS, 1000);
        }

        @Override
        public void onTrackReachedEnd(BluetoothDevice target) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTrackReachedStart(BluetoothDevice target) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPlaybackPositionChanged(BluetoothDevice target,
                                              int playbackPosition) {
            // TODO Auto-generated method stub
            // Since this callback is successful, remove alternate timer for
            mHandler.removeMessages(Constants.CMD_ALTERNATE_UPDATE_TIMER);
            Log.d(TAG, "onPlaybackPositionChanged()  playbackPosition: " + playbackPosition);
            mPosition = playbackPosition;
            Message msg = mHandler.obtainMessage(Constants.CMD_UPDATE_PROGRESS);
            msg.arg1 = playbackPosition;
            msg.arg2 = -1;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onPlayerAppSettingChanged(BluetoothDevice target,
                                              byte[] attribute, byte[] value) {
            if (mCmdController != null)
                mCmdController.onPlayerAppSettingChanged(target, attribute,
                        value);
            mHandler.sendEmptyMessage(Constants.CMD_RELOAD_UI);
        }

        @Override
        public void onChangePathRsp(BluetoothDevice target, byte direction, int numberOfItems,
                                    int status) {
        }

        @Override
        public void onGetFolderItemsRsp(BluetoothDevice target, byte scope,
                                        BluetoothAvrcpBrowseItem[] items, int status) {
            if (!BluetoothAvrcpController.isSuccess(status)) {
                Log.d(TAG, "GetFolderItems response failed");
                return;
            }

            if (Constants.D) Log.d(TAG, "GetFolderItems response succeed");

            switch (scope) {
                case BluetoothAvrcpController.SCOPE_MEDIA_PLAYER_LIST:
                    Log.d(TAG, "case:setting Media player list");
                    mMediaPlayers = items;
                    mHandler.sendEmptyMessage(Constants.GOT_MEDIA_PLAYER_LIST);
                    break;
                case BluetoothAvrcpController.SCOPE_NOW_PLAYING:
                    mNowPlayingItems = items;
                    mHandler.sendEmptyMessage(Constants.GOT_NOW_PLAYING);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onGetItemAttributesRsp(BluetoothDevice target, int[] attributes,
                                           String[] valueTexts, int status) {
        }

        @Override
        public void onSearchRsp(BluetoothDevice target, int numberOfItems, int status) {
        }

        @Override
        public void onPlayItemRsp(BluetoothDevice target, int status) {
            if (Constants.D) Log.d(TAG, "onPlayItemRsp");
        }

        @Override
        public void onAddToNowPlayingRsp(BluetoothDevice target, int status) {
        }

        @Override
        public void onAddressedPlayerChanged(BluetoothDevice target, int playerId) {
        }

        @Override
        public void onAvailablePlayersChanged(BluetoothDevice target) {
        }

        @Override
        public void onNowPlayingContentChanged(BluetoothDevice target) {
        }

        @Override
        public void onUIDsChanged(BluetoothDevice target) {
        }

        @Override
        public void onSetBrowsedPlayerRsp(BluetoothDevice target, int numberOfItems,
                                          String[] folderPath, int status) {
        }

    };
    public void sendPlayBtMusicBroadCast(int playStatus) {
        Intent intent = new Intent(leftLayoutChangeAction);
        intent.putExtra("mediaType", 2);
        Bundle bundle = new Bundle();
        if (mCmdController != null)
            bundle.putString("musicName", mCmdController.getMediaTitle());
        if (playStatus == 1) {
            bundle.putBoolean("isBTMusicPlaying", true);
        } else if (playStatus == 2) {
            bundle.putBoolean("isBTMusicPlaying", false);
        }
        intent.putExtras(bundle);
        mContext.sendBroadcast(intent);
    }

    private void setRealHandler() {
        mHandler = mHandlerReal;
    }

    private void setFakeHandler() {
        mHandler = mHandlerFake;
    }

    protected Handler mHandlerFake = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /*do nothing*/
        }
    };

    protected Handler mHandlerReal = new Handler() {

        private int mTgFeatures;

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage : " + msg);
            switch (msg.what) {
                case Constants.LOAD_CONN_DEV_REQUEST:
                    loadConnectedDevice();//
                    getPlayState = true;
                    avrcp.getPlayStatus(mConnectedDevice);
                    break;
                case Constants.START_AVRC_REQUEST:
                    mTgFeatures = avrcp.getTargetFeatures(mConnectedDevice);
//                    Log.d(TAG, "Target features: " + Integer.toBinaryString(mTgFeatures));

                    mCmdController = new AvrcpCommandManager(avrcp, mConnectedDevice);
                    if (mCmdController != null) {
                        mCmdController
                                .processCmds(AvrcpCommandManager.CMD_LOAD_APP_SETTING_ATTR);
                    }
                    waitForAvrcpInit();
                    break;
                case Constants.LIST_APP_ATTR_REQUEST: {
                    //请求已连接设备是否支持 播放器 设置属性  回调给 onListPlayerApplicationSettingAttributesRsp
                    Bundle data = msg.getData();
                    avrcp.listPlayerApplicationSettingAttributes(mConnectedDevice);
                }
                case Constants.LIST_APP_ATTR_VALS_REQUEST: {
                    Bundle data = msg.getData();
                    byte attributeId = data.getByte("attrID");
                    avrcp.listPlayerApplicationSettingValues(mConnectedDevice,
                            attributeId);
                }
                break;
                case Constants.GET_CURR_ATTR_VAL_REQUEST: {
                    Bundle data = msg.getData();
                    byte[] attributeId = data.getByteArray("attrs");
                    avrcp.getCurrentPlayerApplicationSettingValue(mConnectedDevice,
                            attributeId);
                }
                break;
                case Constants.SET_CURR_ATTR_VAL_REQUEST: {
                    Bundle data = msg.getData();
                    byte[] attrID = data.getByteArray("attrs");
                    byte[] valueID = data.getByteArray("vals");
                    avrcp.setPlayerApplicationSettingValue(mConnectedDevice, attrID,
                            valueID);
                }
                break;
                case Constants.GET_APP_ATTR_TEXT_REQUEST: {
                    Bundle data = msg.getData();
                    byte[] attrID = data.getByteArray("attrID");
                    avrcp.getPlayerApplicationSettingAttributeText(mConnectedDevice,
                            attrID);
                }
                break;
                case Constants.GET_APP_ATTR_VAL_TEXT_REQUEST: {
                    Bundle data = msg.getData();
                    byte attrID = data.getByte("attrID");
                    byte[] valueID = data.getByteArray("valueID");
                    avrcp.getPlayerApplicationSettingValueText(mConnectedDevice,
                            attrID, valueID);
                }
                break;
                case Constants.GET_ELEMENT_ATTR_REQUEST:
                    avrcp.getElementAttributes(mConnectedDevice, new int[]{
                            (int) BluetoothAvrcpController.MEDIA_ATTRIBUTE_TITLE,
                            (int) BluetoothAvrcpController.MEDIA_ATTRIBUTE_ARTIST,
                            (int) BluetoothAvrcpController.MEDIA_ATTRIBUTE_ALBUM,
                            (int) BluetoothAvrcpController.MEDIA_ATTRIBUTE_TRACK_NUM,
                            (int) BluetoothAvrcpController.MEDIA_ATTRIBUTE_NUM_TRACKS,
                            (int) BluetoothAvrcpController.MEDIA_ATTRIBUTE_GENRE,
                            (int) BluetoothAvrcpController.MEDIA_ATTRIBUTE_PLAYING_TIME});
                    Log.d(TAG, "GET_ELEMENT_ATTR_REQUEST() called with: msg = [" + count++ + "]");
                    break;

                //刷新UI的消息
                case Constants.GET_PLAYER_STATUS_REQUEST:
                    if (avrcp != null) {
                        avrcp.getPlayStatus(mConnectedDevice);
                    }
                    break;
                case Constants.MEDIA_SEEK_REQUEST://滑动seekbar回调
                    // TODO : Add new API for seek operation
                    break;
                case Constants.CMD_ON_LOAD_APP_ATTR_FAIL:
                    break;
                case Constants.CMD_RELOAD_UI:
                    for (BluetoothMusicStateListener listener : listeners) {
                        listener.onUpdateUI(avrcpState);
                    }
                    break;
                case Constants.CMD_RELOAD_METADATA: {
                    String txt;
                    if (mCmdController == null) {
                        return;
                    }
                    if (mCmdController.getMediaTrackNumber() != -1)
                        txt = mCmdController.getMediaTrackNumber() + " / ";
                    else
                        txt = "-- / ";
                    if (mCmdController.getMediaTotalTracks() != -1)
                        txt += mCmdController.getMediaTotalTracks();
                    else
                        txt += "--";

                    mDuration = mCmdController.getMediaDuration();
                    if (mDuration > 0) {
                        txt = FormatPlayTime.formatPlayTime(mContext, mDuration / 1000);
                        for (BluetoothMusicStateListener listener : listeners) {
                            listener.onUpdateDuration(txt);
                        }
                       /* if (mSeekBar != null) {
                            mSeekBar.setMax(1000);
                        }*/
                        if (mPosition >= 0) {
                            txt = FormatPlayTime.formatPlayTime(mContext, (int) mPosition / 1000);
                            for (BluetoothMusicStateListener listener : listeners) {
                                listener.onUpdateCurrTime(txt);
                            }
                            float tmpVal = ((float) (mPosition * 1000.00) / mDuration);
                            int currPos = Math.round(tmpVal);
//                            Log.d(TAG, "CMD_RELOAD_METADATA  currPos: " + currPos +
//                                    " mPosition:" + msg.arg1 + ", duration:" + mDuration);
                            for (BluetoothMusicStateListener listener : listeners) {
                                listener.onUpdateSeek(currPos);
//                                Log.d("onUpdateSeek", "CMD_RELOAD_METADATA" + currPos);
                            }
                        }
                    }
                    songInfo.setMediaTotalTracks(txt);
                    songInfo.setMediaTitle(mCmdController.getMediaTitle());
                    songInfo.setMediaArtist(mCmdController.getMediaArtist());
                    songInfo.setMediaAlbum(mCmdController.getMediaAlbum());
                    songInfo.setMediaGenre(mCmdController.getMediaGenre());
                    for (BluetoothMusicStateListener listener : listeners) {
                        listener.onSongInfo(songInfo);
                    }
                }
                break;
                case Constants.CMD_RESET_METADATA:
//                    //TODO: Need to update txtPlayername here.
                    for (BluetoothMusicStateListener listener : listeners) {
                        listener.onUpdateSeek(0);
//                        Log.d("onUpdateSeek", "CMD_RESET_METADATA");
                    }

                    break;
                case Constants.CMD_RELOAD_PROGRESS: {
                    // This event takes care of updating the current time and progress seekbar
                    // every 1 second.
                    if (!isStatusPlaying(avrcpState)
                            || mCmdController == null)
                        return;
                    /*if (mSeekBar != null) {
                        float tmpVal = (float) mSeekBar.getProgress() +
                                (float) (mCmdController.getMediaDuration() / 1000.00 / 1000.00);
                        int currPos = Math.round(tmpVal);
                        Log.d(TAG, "CMD_RELOAD_PROGRESS  currPos: " + currPos);
                        for (BluetoothMusicStateListener listener : listeners) {
                            listener.onUpdateSeek(currPos);
                            Log.d("onUpdateSeek" ,"CMD_RELOAD_PROGRESS"+currPos);
                        }
                    }*/
                    sendEmptyMessageDelayed(Constants.CMD_RELOAD_PROGRESS, 1000);
                }
                break;
                case Constants.CMD_UPDATE_PROGRESS: {
                    float duration;
                    // This event will update the progress seekbar with modified play position from
                    // onPlaybackPositionChanged() callback. This event suppliments CMD_RELOAD_PROGRESS
                    // event as it gives accurate playtime of the remote device.
                    if (mCmdController == null) {
                        Log.w(TAG, "mCmdController - Not yet initialized.");
                        return;
                    }
                    removeMessages(Constants.CMD_RELOAD_PROGRESS);
                    removeMessages(Constants.CMD_ALTERNATE_UPDATE_TIMER);
                    if (msg.arg2 != -1) {
                        duration = (float) msg.arg2;
                    } else {
                        // When msg.arg2 is -1, the song duration is obtained from mCmdController
                        duration = (float) mCmdController.getMediaDuration();
                    }
                    float tmpVal = ((float) (msg.arg1 * 1000.00) / duration);
                    int currPos = Math.round(tmpVal);
//                   CMD_RELOAD_METADATA       ", duration:" + duration);
                    for (BluetoothMusicStateListener listener : listeners) {
                        listener.onUpdateSeek(currPos);
//                        Log.d("onUpdateSeek", "CMD_UPDATE_PROGRESS" + currPos);
                    }
                    sendEmptyMessageDelayed(Constants.CMD_RELOAD_PROGRESS, 1000);
                    sendEmptyMessageDelayed(Constants.CMD_ALTERNATE_UPDATE_TIMER, 1000);
                    String txt = FormatPlayTime.formatPlayTime(mContext, (int) msg.arg1 / 1000);
                    for (BluetoothMusicStateListener listener : listeners) {
                        listener.onUpdateCurrTime(txt);
                    }
                }
                break;
                case Constants.CMD_ALTERNATE_UPDATE_TIMER: {
                    if (isStatusPlaying(avrcpState)) {
                        avrcp.getPlayStatus(mConnectedDevice);
                        sendEmptyMessageDelayed(Constants.CMD_ALTERNATE_UPDATE_TIMER, 5000);
                    } else {
                        removeMessages(Constants.CMD_ALTERNATE_UPDATE_TIMER);
                    }
                }
                break;
                case Constants.CMD_UPDATE_UI_FOR_SEEK: {
                    // This event disable/enable the UI controls when seek operation is in progress.
                   /* if (msg.arg1 == 0) {
                        btnPlayPause.setEnabled(false);
                        btnStop.setEnabled(true);
                        btnRew.setEnabled(false);
                        btnFF.setEnabled(false);
                        btnNext.setEnabled(false);
                        btnPrev.setEnabled(false);
                    } else {
                        updateUI();
                    }*/
                }
                break;
                case Constants.CMD_UPDATE_UI_APP_SETTINGS:
                    break;
                case Constants.GET_MEDIA_PLAYER_LIST:
                    if (Constants.D) Log.d(TAG, "handleMessage:GET_MEDIA_PLAYER_LIST");
                    avrcp.getFolderItems(mConnectedDevice, avrcp.SCOPE_MEDIA_PLAYER_LIST, 0, 99, null);
                    break;
                case Constants.GOT_MEDIA_PLAYER_LIST:
                    if (Constants.D) Log.d(TAG, "handleMessage: GOT_MEDIA_PLAYER_LIST");
//                    updateMediaPlayers();
                    break;
                case Constants.GET_NOW_PLAYING:
                    if (Constants.D) Log.d(TAG, "handleMessage: GET_NOW_PLAYING");
                    avrcp.getFolderItems(mConnectedDevice, avrcp.SCOPE_NOW_PLAYING, 0, 99, null);
                    break;
                case Constants.GOT_NOW_PLAYING:
                    if (Constants.D) Log.d(TAG, "handleMessage: GOT_NOW_PLAYING");
                    updateNowPlaying();
                    break;
                case Constants.PLAY_NOW_ITEM:
                    if (Constants.D) Log.d(TAG, "handleMessage: PLAY_NOW_ITEM");
                    playItem(avrcp.SCOPE_NOW_PLAYING, mSelectedNowItem);
                    break;
                default:
                    Log.e(TAG, "Unhandled msg: " + msg.what);
            }
        }
    };

    protected Handler mHandler = "tcc897xpm01v2".equals(SystemProperties.get("ro.product.device")) ? mHandlerFake : mHandlerReal;


    private void playItem(byte scope, BluetoothAvrcpBrowseItem item) {
        if (Constants.D) Log.d(TAG, "playItem");
        if (item.mItemType == BluetoothAvrcpBrowseItem.ITEM_TYPE_FOLDER)
            avrcp.playItem(mConnectedDevice, scope, item.mFolderUid);
        else if (item.mItemType == BluetoothAvrcpBrowseItem.ITEM_TYPE_MEDIA_ELEMENT)
            avrcp.playItem(mConnectedDevice, scope, item.mElementUid);
    }

    private void updateNowPlaying() {
        if (Constants.D) Log.d(TAG, "updateNowPlaying");
        int size = mNowPlayingItems.length;
        array_nowPlayingItems = new String[size];
        for (int i = 0; i < size; i++) {
            array_nowPlayingItems[i] = mNowPlayingItems[i].mDisplayableName;
        }
        // Select the first item by default
        mSelectedNowItem = mNowPlayingItems[0];
    }

    private boolean isStatusPlaying(long playStatus) {
        return (playStatus == BluetoothAvrcpController.PLAY_STATUS_PLAYING ||
                playStatus == BluetoothAvrcpController.PLAY_STATUS_FWD_SEEK ||
                playStatus == BluetoothAvrcpController.PLAY_STATUS_REV_SEEK) ? true : false;
    }

    public boolean isBluetoothMusicPlaying() {
        return isStatusPlaying(avrcpState);
    }

    private void loadConnectedDevice() {
        List<BluetoothDevice> connDevices = avrcp.getConnectedDevices();
        Log.d(TAG, "loadConnectedDevice() called with: " + "connDevices.size" + connDevices.size());
        if (connDevices == null || connDevices.size() == 0) {
            Log.d(TAG, "No connected device available.");
//            showDialog(GUI_UPDATE_MSG_NO_CONN_DEVICE_ERROR);
            return;
        }
        // Retrieve the last connected (and valid) device
        mConnectedDevice = connDevices.get(0);
        Log.d(TAG, "Connected device : " + mConnectedDevice);
        mHandler.sendEmptyMessage(Constants.START_AVRC_REQUEST);
        if ((avrcp.getTargetFeatures(mConnectedDevice) & avrcp.FEATURE_BROWSE) > 0) {
            mHandler.sendEmptyMessage(Constants.GET_MEDIA_PLAYER_LIST);
        }
    }

    private void waitForAvrcpInit() {
        mCmdController.registerCallback(commandCallback);
    }

    private byte selectAttributeValue(byte attrId) {
        ByteBuffer supportedAttrVals = mCmdController
                .getSupportedAttrValues(attrId);
        Log.w(TAG, "selectAttributeValue() attrId:" + attrId);
        if (supportedAttrVals == null) {
            Log.w(TAG, "No supported values set. Set default");
            return 0x01;
        }
        byte currVal = mCmdController.getCurrentAttrVal(attrId);
        int size = supportedAttrVals.array().length;
        if (size == 0) {
            Log.w(TAG, "No supported values available. Set default");
            return 0x01;
        }

        byte startVal = supportedAttrVals.get(0);
        byte endVal = supportedAttrVals.get(size - 1);
        Log.d(TAG, "start: " + startVal + ", end:" + endVal + ", len:" + size
                + " *** pos:" + supportedAttrVals.position());

        supportedAttrVals.position(0);
        for (int i = 0; i < size; i++) {
            byte attr = supportedAttrVals.get();
            Log.d(TAG, "currVal : " + currVal + " >>> " + attr);
            if (attr == currVal) {
                if (attr == endVal) {
                    Log.d(TAG, " >< ATTR VAL :: " + startVal);
                    supportedAttrVals.position(0);
                    return startVal;
                }
                attr = supportedAttrVals.get();
                Log.d(TAG, "ATTR VAL :: " + attr);
                return attr;
            } else
                continue;
        }

        Log.w(TAG, "Returning default XXX_OFF");
        return 0x01;
    }

    AvrcpCommandCallback commandCallback = new AvrcpCommandCallback() {
        @Override
        public void onCommandCompleted(int status) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCommandFailure(int errStatus) {
            // TODO Auto-generated method stub

        }

        @SuppressWarnings("deprecation")
        @Override
        public void onInitCompleted() {
            // TODO Auto-generated method stub
//            dismissDialog(GUI_UPDATE_MSG_WAITING_FOR_INIT_COMPLETE);
            mHandler.sendEmptyMessage(Constants.GET_PLAYER_STATUS_REQUEST);
        }

        @Override
        public void onLoadAttributesFailure() {
            mHandler.sendEmptyMessage(Constants.CMD_ON_LOAD_APP_ATTR_FAIL);
            mHandler.sendEmptyMessage(Constants.GET_PLAYER_STATUS_REQUEST);
        }

        @Override
        public void onLoadMetataCompleted() {
            //mPosition = 0;
            mHandler.sendEmptyMessage(Constants.CMD_RELOAD_METADATA);
        }

        private byte selectAttributeValue(byte attrId) {
            ByteBuffer supportedAttrVals = mCmdController
                    .getSupportedAttrValues(attrId);
            Log.w(TAG, "selectAttributeValue() attrId:" + attrId);
            if (supportedAttrVals == null) {
                Log.w(TAG, "No supported values set. Set default");
                return 0x01;
            }
            byte currVal = mCmdController.getCurrentAttrVal(attrId);
            int size = supportedAttrVals.array().length;
            if (size == 0) {
                Log.w(TAG, "No supported values available. Set default");
                return 0x01;
            }

            byte startVal = supportedAttrVals.get(0);
            byte endVal = supportedAttrVals.get(size - 1);
            Log.d(TAG, "start: " + startVal + ", end:" + endVal + ", len:" + size
                    + " *** pos:" + supportedAttrVals.position());

            supportedAttrVals.position(0);
            for (int i = 0; i < size; i++) {
                byte attr = supportedAttrVals.get();
                Log.d(TAG, "currVal : " + currVal + " >>> " + attr);
                if (attr == currVal) {
                    if (attr == endVal) {
                        Log.d(TAG, " >< ATTR VAL :: " + startVal);
                        supportedAttrVals.position(0);
                        return startVal;
                    }
                    attr = supportedAttrVals.get();
                    Log.d(TAG, "ATTR VAL :: " + attr);
                    return attr;
                } else
                    continue;
            }

            Log.w(TAG, "Returning default XXX_OFF");
            return 0x01;
        }

        @Override
        public void onLoadAppAttrsLoaded() {
            Log.d(TAG, "onLoadAppAttrsLoaded() repeat:" + mCmdController.getRepeatState() +
                    ", shuffle:" + mCmdController.getShuffleState());
            mHandler.sendEmptyMessage(Constants.CMD_UPDATE_UI_APP_SETTINGS);
        }
    };

    public void resume() {
        if("tcc897xpm01v2".equals(SystemProperties.get("ro.product.device"))) {
            setRealHandler();
        }

        if (null != avrcp) {
            avrcp.unregisterEventHandler();
            avrcp.registerEventHandler(eventHandler);
            if (mConnectedDevice == null)
                mHandler.sendEmptyMessage(Constants.LOAD_CONN_DEV_REQUEST);
            else {
                mHandler.sendEmptyMessage(Constants.START_AVRC_REQUEST);
                if ((avrcp.getTargetFeatures(mConnectedDevice) & avrcp.FEATURE_BROWSE) > 0) {
                    mHandler.sendEmptyMessage(Constants.GET_MEDIA_PLAYER_LIST);
                }
            }
        }

        avrcpState = BluetoothAvrcpController.PLAY_STATUS_STOPPED;
        if (avrcp != null && mConnectedDevice != null) {
            loadConnectedDevice();
            getPlayState = true;
            avrcp.getPlayStatus(mConnectedDevice);
        }
        if (!proxy) {
            proxy = BluetoothAvrcpController.getProxy(mContext, listener);
            if (eventHandler == null) {
                avrcp.registerEventHandler(eventHandler);
            }
        }
    }

    public void destory() {
//        if (avrcp != null) avrcp.closeProxy();
//        avrcpState = BluetoothAvrcpController.PLAY_STATUS_STOPPED;
//        mConnectedDevice = null;
//        mCmdController = null;
//        avrcp = null;
//        proxy = false;
    }

    public void pause() {
        Log.e("test","pause()");
        mHandler.removeMessages(Constants.CMD_RELOAD_PROGRESS);
        mHandler.removeMessages(Constants.CMD_UPDATE_PROGRESS);
        mHandler.removeMessages(Constants.CMD_ALTERNATE_UPDATE_TIMER);
        if("tcc897xpm01v2".equals(SystemProperties.get("ro.product.device"))) {
            setFakeHandler();
        }
//        if (mCmdController != null) {
//            mCmdController.unregisterCallback();
//            mCmdController.clear();
//            mCmdController = null;
//        }
//        if(avrcp!=null){
//            avrcp.unregisterEventHandler();
//        }
//        proxy = false;
    }

    public void unregisterProxy() {

        if (mCmdController != null) {
            mCmdController.unregisterCallback();
            mCmdController.clear();
            mCmdController = null;
        }
        if (avrcp != null) {
            avrcp.closeProxy();
        }
        avrcpState = BluetoothAvrcpController.PLAY_STATUS_STOPPED;
        mConnectedDevice = null;
        if (avrcp != null) {
            avrcp.unregisterEventHandler();
        }
        if (proxy) {
            proxy = false;
        }
        if (!"tcc897xpm01v2".equals(SystemProperties.get("ro.product.device")) && !"tcc897xzd2sv2".equals(SystemProperties.get("ro.product.device"))) {
            abandonAudioFocus();
        }
//        mAudioManager.unregisterMediaButtonEventReceiver(mComponentName);
    }

    public int requestAudioFoucs() {
        int ret = XxAudioManager.getInstance().requestAudioFocus(
                XxAudioManager.MODULE_BLUETOOTHMUSIC,
                AudioManager.STREAM_NOTIFICATION, AudioManager.AUDIOFOCUS_GAIN);
        if (ret == XxAudioManager.AUDIOFOCUS_REQUEST_FAILED ||
                ret == XxAudioManager.AUDIOFOCUS_REQUEST_PHONE ||
                ret == XxAudioManager.AUDIOFOCUS_REQUEST_BTEARPHONE) {
            pause();
        }
//        if (ret == XxAudioManager.AUDIOFOCUS_REQUEST_LOWVOLUME_PLAY) {
//            //getBluetoothDevice().setLosesFocusBtearphone(true);
//            mBluetoothDevice.setVolume(10);
//        }


        return XxAudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public int abandonAudioFocus() {
        return XxAudioManager.getInstance().abandonAudioFocus(
                XxAudioManager.MODULE_BLUETOOTHMUSIC);
    }

    private boolean mbLowVolume = false;
    public XxAudioManager.OnXxAudioFocusChange audioListener = new XxAudioManager.OnXxAudioFocusChange() {

        @Override
        public void onChange(int state) {
            Log.d("XXXA", "onChange() called with: " + "state = [" + state + "]");
            switch (state) {

                case XxAudioManager.AUDIOFOUCS_LOWERVOLUME:
                    Log.d("XXXA", "XxAudioManager.AUDIOFOUCS_LOWERVOLUME");
                    if (isStatusPlaying(avrcpState)) {

                        mbLowVolume = true;
//                        setCallVolume(0);
                        streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        setNotificationVolume(1);
                        mAudioManager.setBtMusicLosesFocus(true);
//                        mAudioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                        Log.d(TAG, "AUDIOFOUCS_LOWERVOLUME" + "streamVolume = [" + streamVolume + "]" + mbLowVolume);
                    }
                    break;
                case XxAudioManager.AUDIOFOUCS_RESUME:
                    Log.d("XXXA", "XxAudioManager.AUDIOFOUCS_RESUME");
                    if (avrcp != null) {
                        if (mConnectedDevice != null) {
                            if (!isStatusPlaying(avrcpState)) {
                                avrcp.play(mConnectedDevice);
                            }
                        }
                    }
                    break;
                case XxAudioManager.AUDIOFOUCS_RESUMEVOLUME:
                    Log.d("XXXA", "XxAudioManager.AUDIOFOUCS_RESUMEVOLUME");
                    if (!mbLowVolume) {
                        return;
                    }
                    mbLowVolume = false;
//                    setCallVolume(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
                    mAudioManager.setBtMusicLosesFocus(false);
                    setNotificationVolume(streamVolume);
                    Log.d(TAG, "AUDIOFOUCS_RESUMEVOLUME" + "streamVolume = [" + streamVolume + "]" + mbLowVolume);
//                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                    break;
                case XxAudioManager.AUDIOFOUCS_STOP:
                    Log.d("XXXA", "XxAudioManager.AUDIOFOUCS_STOP");
                    if (avrcp != null) {
                        if (mConnectedDevice != null) {
                            int count = 0;
                            boolean pauseSuccess = false;
                            while (count < 2 && !pauseSuccess) {
                                count++;
                                pauseSuccess = avrcp.pause(mConnectedDevice);
                            }
                        }
                    }
                    break;
                case XxAudioManager.AUDIOFOUCS_PAUSE:
                    Log.d("XXXA", "XxAudioManager.AUDIOFOUCS_PAUSE");
                    if (avrcp != null) {
                        if (mConnectedDevice != null) {
                            avrcp.pause(mConnectedDevice);
                        }
                    }
                    break;
            }
        }
    };

    public BluetoothDevice getConnectedDevice() {
        return mConnectedDevice;
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }

    int getAudioConnectionState(BluetoothDevice device) {
        int state = BluetoothA2dp.STATE_DISCONNECTED;
        if ((null != avrcp) && (null != device)) {
            state = avrcp.getConnectionState(device);
        }
        Log.i(TAG, "getAudioConnectionState: " + state);
        return state;
    }

    public boolean isA2dpConnected(BluetoothDevice device) {
        return (BluetoothA2dp.STATE_CONNECTED == getAudioConnectionState(device));
    }

    public boolean connectAudioDevice(BluetoothDevice device) {
        boolean connect = false;
        if ((null != avrcp) && (null != device)) {
            connect = avrcp.connect(device);
        }
        return connect;
    }

    public void setCallVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                volume,
                AudioManager.STREAM_VOICE_CALL);
    }

    public void setNotificationVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                volume,
                0);
    }

    public void onBluetoothMusicClose() {
        for (BluetoothMusicStateListener listener : listeners) {
            listener.onBluetoothMusicClose();
        }
    }
}
