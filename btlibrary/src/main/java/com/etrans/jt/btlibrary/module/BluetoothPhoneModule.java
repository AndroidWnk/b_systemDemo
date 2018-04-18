package com.etrans.jt.btlibrary.module;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.broadcom.bt.hfdevice.BluetoothCallStateInfo;
import com.broadcom.bt.hfdevice.BluetoothClccInfo;
import com.broadcom.bt.hfdevice.BluetoothHfDevice;
import com.broadcom.bt.hfdevice.BluetoothPhoneBookInfo;
import com.broadcom.bt.hfdevice.IBluetoothHfDeviceEventHandler;
import com.etrans.jt.btlibrary.R;
import com.etrans.jt.btlibrary.db.DataOperation;
import com.etrans.jt.btlibrary.domin.ContactBean;
import com.etrans.jt.btlibrary.listener.BluetoothPhoneListener;
import com.etrans.jt.btlibrary.manager.XxConfig;
import com.etrans.jt.btlibrary.utils.BRCMHfDeviceConstants;
import com.etrans.jt.btlibrary.utils.Constants;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 单元名称:BluetoothPhoneModule.java
 * Created by fuxiaolei on 2016/7/7.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/7.
 */
public class BluetoothPhoneModule extends BaseModule<BluetoothPhoneListener> {

    private static final String TAG = BluetoothPhoneModule.class.getSimpleName();
    private static BluetoothPhoneModule instance;
    private boolean isRoaming;
    private BluetoothHfDevice bluetoothHFDevice;
    private HfDeviceEventHandler mHfDeviceEventHandler;
    private BluetoothDevice bluetoothDevice;
    private String mDeviceName = "";
    private String mDeviceAddress;
    private int currentSignalStrength;
    private int currentBatteryCharge;
    //    private BluetoothPhoneListener mListener;
    private boolean isHSPConnection;

    private boolean isPendingClcc = false;
    private boolean isEnableNREC = false;
    private Ringtone mRingtone = null;
    private int mRingState = BRCMHfDeviceConstants.RING_STATE_IDLE;
    private int mInBandRingStatus = BluetoothHfDevice.INBAND_STATE_OFF;
    private String deviceName;
    private boolean proxy;
    private boolean isLocalCall = false;
    private boolean dialing;
    private boolean altering;
    private long startTime;
    private boolean calling;
    private String callStatus = "";
//    private boolean reStartTimer = true;
//    private PhonebookService phonebookService;

    public String getLastNumber() {
        return lastNumber;
    }

    private String lastNumber = "";

    public List<BluetoothClccInfo> getmCallList() {
        return mCallList;
    }

    private List<BluetoothClccInfo> mCallList;
    private int mDownLoadCount;
    private boolean isRegist = false;
    private boolean hasDownload = false;
    private int callState = -1;
    private static SharedPreferences mPreferences = null;
    private String BT_SP_NAME = "bt";
    private boolean isBtCall = false;
    private String multiCallStateAction = "com.etrans.bluetooth.multiCallStateAction";
    private String multiCallChangeOneAction = "com.etrans.bluetooth.multiCallChangeOneAction";

    private BluetoothPhoneModule() {

    }

    public static BluetoothPhoneModule getInstance() {
        synchronized (BluetoothPhoneModule.class) {
            if (instance == null) {
                instance = new BluetoothPhoneModule();
            }
            return instance;
        }
    }

    public void init(Context context) {
        mContext = context;
        if (mPreferences == null)
            mPreferences = context.getSharedPreferences(BT_SP_NAME, Context.MODE_PRIVATE);
        getCallPhoneListener();
        if (BluetoothModule.getInstance().isBtConnect()) {
            initPhoneService();
        }
    }

    private BroadcastReceiver mWbsStateIntentRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, action);
            if (action.equals(BluetoothHfDevice.ACTION_WBS_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                Log.d(TAG, "ACTION_WBS_STATE_CHANGED: state = " + state);
                Message msg1 = Message.obtain();
                msg1.what = BRCMHfDeviceConstants.GUI_UPDATE_WBS_STATE;
                msg1.arg1 = state;
                viewUpdateHandler.sendMessage(msg1);
            }
        }
    };

    public void initPhoneService() {
        if (!proxy) {
            proxy = BluetoothHfDevice.getProxy(mContext, listener);
        }
//        phonebookService = new PhonebookService(mContext);
//        phonebookService.connectPhonebookDevice(getConnectDevice());
//        registerBtPhoneReceiver();
    }

    public void registerBtPhoneReceiver() {
        if (isRegist) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothHfDevice.ACTION_WBS_STATE_CHANGED);
        mContext.registerReceiver(mWbsStateIntentRec, intentFilter);
        isRegist = true;
    }

    public void unRegisterBtPhoneReceiver() {
        /*if (isRegist) {
            mContext.unregisterReceiver(mWbsStateIntentRec);
        }*/
        releaseResources();
    }

    private void updateUi() {
        if (bluetoothHFDevice.getConnectedDevices().size() == 0) {
            // No connected device available exit app
//            Toast.makeText(mContext,
//                    "Please Pair/Connect an Ag from Settings App and launch the app",
//                    Toast.LENGTH_LONG).show();
            return;
        } else if (bluetoothHFDevice.getConnectionState(bluetoothDevice) !=
                BluetoothHfDevice.STATE_CONNECTING) {

            bluetoothDevice = bluetoothHFDevice.getConnectedDevices().get(0);
            mDeviceName = bluetoothDevice.getName();
            mDeviceAddress = bluetoothDevice.getAddress();
//            BluetoothModule.getInstance().setDeviceName(mDeviceName);
//            displayState.setText("Connected to " + mDeviceName);


            Log.d(TAG, "updateUi: device already connected..");

            Log.d(TAG, "Handler.handleMessage: connected to a device named " +
                    bluetoothDevice.getName() + " address: " +
                    mDeviceAddress + " bluetoothDevice.getAddress(): " +
                    bluetoothDevice.getAddress());

            updateIndicators(bluetoothHFDevice.
                    getDeviceIndicators(bluetoothDevice));   // update indicators
            Log.d(TAG, "updateUi: updated indicators..");
            /*Edit the shared preference as it is connected*/
            Log.d(TAG, "updateUi: connected to a device named " + bluetoothDevice.getName());
            Message msg = Message.obtain();

            bluetoothHFDevice.getPeerFeatures().printLog();
            bluetoothHFDevice.getLocalFeatures().printLog();

            isHSPConnection = bluetoothHFDevice.getPeerFeatures().isHSPConnection();

            if (!isHSPConnection) {
                msg.what = BRCMHfDeviceConstants.GUI_UPDATE_CALL_STATUS;
                msg.obj = bluetoothHFDevice.getCallStateInfo(bluetoothDevice);
                viewUpdateHandler.sendMessage(msg);

                Log.d(TAG, "updateUi: call state is:" +
                        bluetoothHFDevice.getCallStateInfo(bluetoothDevice).getCallSetupState());


                bluetoothHFDevice.getCLCC();

                bluetoothHFDevice.queryOperatorSelectionInfo();
                bluetoothHFDevice.querySubscriberInfo();
//                updateViewVrState(mVrState);
                Message msg1 = Message.obtain();
                msg1.what = BRCMHfDeviceConstants.GUI_UPDATE_WBS_STATE;
//                msg1.arg1 = pref.getInt(BRCMHfDeviceConstants.HF_DEVICE_WBS_STATE, 0);
                viewUpdateHandler.sendMessage(msg1);
                Message msg2 = Message.obtain();
                msg2.what = BRCMHfDeviceConstants.GUI_UPDATE_IN_BAND_STATUS;
                if (bluetoothHFDevice.getPeerFeatures().isInBandToneSupported())
                    msg2.arg1 = BluetoothHfDevice.INBAND_STATE_ON;
                else
                    msg2.arg1 = BluetoothHfDevice.INBAND_STATE_OFF;
                viewUpdateHandler.sendMessage(msg2);

                Message msg3 = Message.obtain();
                if (bluetoothHFDevice.getPeerFeatures().isECNRSupported()) {
                    isEnableNREC = bluetoothHFDevice.getDeviceNRECState(bluetoothDevice);
                } else {
                    isEnableNREC = false;
                }
                msg3.what = BRCMHfDeviceConstants.GUI_UPDATE_NREC_STATUS;
                msg3.arg1 = isEnableNREC == true ? 1 : 0;
                viewUpdateHandler.sendMessage(msg3);
            } else {
            }


        }
    }

    /**
     * Hanlder handles all the GUI events
     */
    protected Handler viewUpdateHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BRCMHfDeviceConstants.GUI_UPDATE_CALL_STATUS:
//                    if (System.currentTimeMillis() - startTime < 500 && dialing) {
//                        dialing = false;
//                    }
//                    if (dialing) {
//                        dialing = false;
//                        return;
//                    }
//                    if (calling) {
//                        calling = false;
//                        return;
//                    }
                    updateViewWithCallStatus((BluetoothCallStateInfo) msg.obj);//123监听蓝牙通话状态
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_DEVICE_INDICATORS:    //Update status of Battery,Signal etc..3
                    BluetoothPhoneModule.getInstance().updateIndicators((int[]) msg.obj);
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_AUDIO_STATE://刷新mic  或者 蓝牙设备输出声音5
                    for (BluetoothPhoneListener listener : listeners) {
                        listener.onUpdateAudioState(msg.arg1);
                    }
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_VENDOR_AT_RSP:
                    Log.d(TAG, "Handler.handleMessage: showing vendor at command response");
                    int status = msg.arg1;
                    String toastMsg;
                    toastMsg = "AT vendor rsp. status=" + status;
                    if (msg.obj != null)
                        toastMsg += " rsp=" + msg.obj.toString();
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_CLCC_AT_RSP:
                    synchronized (this) {
//                    Log.d(TAG, "Handler.handleMessage: CLCC ui update");

                        List<BluetoothClccInfo> bluetoothClccInfos = BluetoothPhoneModule.getInstance().getmCallList();
                        if (bluetoothClccInfos != null && bluetoothClccInfos.size() > 0) {
                            BluetoothClccInfo bluetoothClccInfo = bluetoothClccInfos.get(0);
                            String callNumber = bluetoothClccInfo.getCallNumber();
                            String name = callNumber;
                            Log.d(TAG, "BluetoothClccInfo called with: " + "callNumber = [" + bluetoothClccInfo.getCallNumber() + "]");
                            ContactBean contactBean = BluetoothPhoneBookModule.getInstance().queryContact(callNumber);
                            if (contactBean != null) {
                                name = contactBean.getName();
                            }

                            for (BluetoothPhoneListener listener : listeners) {
                                listener.onUpdateCallEx(callNumber, name);
                            }
                            lastNumber = callNumber;
                        }
                    }
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_VOLUME:
                    Log.d(TAG, "Handler.handleMessage: showing volume changed");
                    int type = msg.arg1;
                    int volume = msg.arg2;
                    if (type == BluetoothHfDevice.VOLUME_TYPE_SPK) {
                    }
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_OPERATOR:
                    String opName = (String) msg.obj;
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_SUBSCRIBER:
                    String subcriberNum = (String) msg.obj;
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_VR_STATE:
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_WBS_STATE:
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_RING:
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_IN_BAND_STATUS:
//                    updateViewInBandState(msg.arg1);
                    break;

                case BRCMHfDeviceConstants.GUI_UPDATE_NREC_STATUS:
//                    updateViewNRECState(msg.arg1);
                    break;
                case Constants.PHONE_UPDATE_TIME:
                    for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
                        bluetoothPhoneListener.onUpdateCallTime((Integer) msg.obj);
                    }
                    break;
                case BRCMHfDeviceConstants.GUI_UPDATE_PHONEBOOK_AT_RSP:
                    updateDownloadPhoneBook((List<BluetoothPhoneBookInfo>) msg.obj, msg.arg1);
                    break;
                case Constants.PHONE_DOWNLOAD_PHONE_BOOK:
                    if (bluetoothHFDevice != null) {
                        bluetoothHFDevice.readPhoneBookList(Constants.ME, -1);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    int mnLoadingContactTimes = 0;

    private void updateDownloadPhoneBook(List<BluetoothPhoneBookInfo> bookInfoList, int status) {
        synchronized (this) {
            String str = "";
            if (BluetoothHfDevice.PHONEBOOK_READ_COMPLETED == status) {
                for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
                    bluetoothPhoneListener.onUpdateDownloadCount(getDeviceName());//已连接：XX手机getDeviceName()
                }
                mDownLoadCount = 0;
                hasDownload = false;
                mPreferences.edit().putBoolean("isDownload", true).commit();
                Log.d(TAG, "updateDownloadPhoneBook: state = " + status);
            } else if (BluetoothHfDevice.PHONEBOOK_READ_PROGRESS_UPDATE == status) {
                if (null != bookInfoList) {
                    BluetoothPhoneModule.getInstance().updateDownloadFileContent(bookInfoList);
                    mDownLoadCount = mDownLoadCount + bookInfoList.size();
                }
                if (bookInfoList.size() == 0) {
                    str = "正在导入联系人";
                    mnLoadingContactTimes++;
                    String strDot = "";
                    if (mnLoadingContactTimes % 4 == 0) {
                        strDot += ".    ";
                    } else if (mnLoadingContactTimes % 4 == 1) {
                        strDot += "..   ";
                    } else if (mnLoadingContactTimes % 4 == 2) {
                        strDot += "...  ";
                    } else {
                        strDot += ".... ";
                    }
                    str = str + strDot;
                } else {
                    str = mContext.getString(R.string.bt_Downloading) + mDownLoadCount + "";
                }
                for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
                    bluetoothPhoneListener.onUpdateDownloadCount(str);
                }
                hasDownload = true;
                Log.d(TAG, "handleMessage() called with: " + "mDownLoadCount = [" + mDownLoadCount + "]"
                        + "bookInfoList.size()" + bookInfoList.size());
            } else if (status == 0) {
                mDownLoadCount = 0;
                hasDownload = false;
            }

        }
    }

    public BluetoothProfile.ServiceListener getListener() {
        return listener;
    }

    BluetoothProfile.ServiceListener listener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceDisconnected(int profile) {
            Log.i(TAG, "onServiceDisconnected()");
            bluetoothHFDevice = null;
        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.i(TAG, "onServiceConnected()");
            if (bluetoothHFDevice == null) {
                bluetoothHFDevice = (BluetoothHfDevice) proxy;
            }
            if (bluetoothHFDevice != null) {
                if (mHfDeviceEventHandler == null) {
                    mHfDeviceEventHandler = new HfDeviceEventHandler();
                }
                try {
                    Log.d(TAG, "onServiceConnected() called with: " + "bluetoothHFDevice = " + bluetoothHFDevice == null ? "1" : "0");
                    bluetoothHFDevice.registerEventHandler(mHfDeviceEventHandler);
                    Log.d(TAG, "onProxyAvailable: registered event handler..");
                } catch (Exception e) {
                    Log.d(TAG, "onServiceConnected: Exception = " + e.getStackTrace());
                }

            }
            //
            downloadPhoneBook(false);
            updateUi();
            for (BluetoothPhoneListener listener : listeners) {
                listener.onSetBluetoothHFDevice(bluetoothHFDevice);
            }
            if (bluetoothHFDevice.getConnectedDevices().size() == 0) {
                return;
            }
            BluetoothDevice device = bluetoothHFDevice.getConnectedDevices().get(0);
            deviceName = device.getName();
            BluetoothModule.getInstance().setDeviceName(deviceName);

        }
    };

    /**
     * 下载蓝牙电话本
     *
     * @param bClick ?刷新:初始化连接下载
     */
    public void downloadPhoneBook(boolean bClick) {
        //读取电话本 根据不同的类型
        if (bluetoothHFDevice == null) {
            if (!proxy) {
                BluetoothHfDevice.getProxy(mContext, listener);
            }
            return;
        }
        if (!hasDownload) {
            if (bClick) {
                cleanPhoneBook();
                viewUpdateHandler.sendEmptyMessage(Constants.PHONE_DOWNLOAD_PHONE_BOOK);
            } else {
                if (mPreferences.getBoolean("isDownload", false)) {
                    return;
                }
                cleanPhoneBook();
                viewUpdateHandler.sendEmptyMessageDelayed(Constants.PHONE_DOWNLOAD_PHONE_BOOK, 5000);
            }
        }
    }


    /**
     * 清空电话本
     */
    public void cleanPhoneBook() {
        //TODO 清空数据库
        DataOperation.getInstance().clean();
        mPreferences.edit().putBoolean("isDownload", false).commit();
    }

    /**
     * 拨打电话
     *
     * @param phoneNum
     */
    public void dial(String phoneNum) {
        if (bluetoothHFDevice != null) {
            Log.e(TAG, "dial");
            bluetoothHFDevice.dial(phoneNum);
            lastNumber = phoneNum;
        }
    }

    /**
     * 重拨
     */
    public void redial() {
        if (bluetoothHFDevice != null) {
            bluetoothHFDevice.redial();
        }
    }

    /**
     * 接听
     */
    public void answer() {
        if (bluetoothHFDevice != null) {
            RingStop();
            bluetoothHFDevice.answer();
        }
    }

    /**
     * 挂断
     */
    public void hangup() {
        Log.e(TAG, "hangup");
        if (bluetoothHFDevice != null) {
            bluetoothHFDevice.hangup();
            RingStop();
        }
    }

    /**
     * 保持呼叫
     *
     * @param holdType
     */
    public void hold(int holdType) {
        if (bluetoothHFDevice != null) {
            bluetoothHFDevice.hold(holdType);
        }
    }

    /**
     * 设置指定类型的音量大小
     *
     * @param volType
     * @param volume  Current volume level (0-15)
     */
    public void setVolume(int volType, int volume) {
        if (bluetoothHFDevice != null) {
            bluetoothHFDevice.setVolume(volType, volume);
        }
    }

    public void getClcc() {
        if (null != bluetoothHFDevice) {
            bluetoothHFDevice.getCLCC();
        }
    }

    //判断是否手持设备连接
    public int getHandsfreeConnectionState() {
        int state = BluetoothHfDevice.STATE_DISCONNECTED;
        if ((null != bluetoothHFDevice) && (null != bluetoothDevice)) {
            state = bluetoothHFDevice.getConnectionState(bluetoothDevice);
            Log.i(TAG, "getAudioConnectionState: " + bluetoothDevice.getAddress());
        }
        return state;
    }

    /**
     * 设置电话中输入的字符
     *
     * @param s
     * @param start
     */
    public void sendDTMFCode(CharSequence s, int start) {
        if (bluetoothHFDevice != null) {
            BluetoothCallStateInfo callInfo = bluetoothHFDevice.getCallStateInfo(bluetoothDevice);
            if (!callInfo.isInCall())
                return;
            char keyChar = 'x';
            try {
                keyChar = s.charAt(start);
                String allowedChars = "0123456789*#";
                if (allowedChars.indexOf(keyChar) != -1) {
                    Log.d(TAG, "DTMF key code char = " + keyChar);
                    bluetoothHFDevice.sendDTMFcode(keyChar);
                } else {
                    Log.e(TAG, "Invalid character input");
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 蓝牙/听筒输出
     */
    public void changeAudio() {
        if (bluetoothHFDevice != null) {
            int audioState = bluetoothHFDevice.getAudioState(bluetoothDevice);
            Log.d(TAG, "changeAudio: audioState = " + audioState);
            if (audioState == 2) {
                bluetoothHFDevice.disconnectAudio();
            } else if (audioState == 0) {
                bluetoothHFDevice.connectAudio();
            }
        }
    }

    public int getAudioConnectionState() {
        int state = -1;
        if ((null != bluetoothHFDevice) && (null != bluetoothDevice)) {
            state = bluetoothHFDevice.getAudioState(bluetoothDevice);
        }
        Log.i(TAG, "getAudioConnectionState: state" + state);
        return state;
    }


    public BluetoothCallStateInfo getCallStateInfo() {
        if (bluetoothHFDevice != null) {
            return bluetoothHFDevice.getCallStateInfo(bluetoothDevice);
        }
        return null;
    }

    public void sendBIA(boolean b) {
        if (bluetoothHFDevice != null) {
            bluetoothHFDevice.sendBIA(b, b, b, b);
        }
    }

    public void startPhoneActivity() {
        ComponentName cn = new ComponentName("com.etrans.jt.bluetooth",
                "com.etrans.jt.bluetooth.view.PhoneActivity");
        Intent in = new Intent();
        in.setComponent(cn);
//        in.setClass(content, PhoneCallExActivity.class);
//       in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d(TAG, "onReceive: Broadcast Reciever started an activity..");
        mContext.startActivity(in);
    }

    public BluetoothDevice getConnectDevice() {
        return bluetoothDevice;
    }

    protected class HfDeviceEventHandler implements IBluetoothHfDeviceEventHandler {

        @Override
        public void onConnectionStateChange(int errCode,
                                            BluetoothDevice remoteDevice, int newState, int prevState, int disconnectReason) {
            bluetoothDevice = null;
            Log.e(TAG, "onConnectionStateChange()");
            Log.d(TAG, "======================TCC TEST disconnectReason =" + disconnectReason);
            if (BluetoothHfDevice.DISCONNECT_REASON_LINK_LOSS == disconnectReason) {
                Log.d(TAG, "into BluetoothHfDevice.DISCONNECT_REASON_LINK_LOSS == disconnectReason");
                mContext.sendBroadcast(new Intent("android.vehicel.bluetooth.START_AUTO_CONNECT"));
            }
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_DEVICE_STATUS;
            msg.arg1 = newState;
            if (remoteDevice == null) {
                msg.obj = bluetoothDevice;
            } else {
                msg.obj = remoteDevice;
            }
            viewUpdateHandler.sendMessage(msg);

            mInBandRingStatus = BluetoothHfDevice.INBAND_STATE_OFF;
            switch (newState) {
                case BluetoothHfDevice.STATE_DISCONNECTING:
                    Log.i(TAG, remoteDevice.getName() + "(" + remoteDevice.getAddress() + ") is BluetoothHfDevice.STATE_DISCONNECTING");
                    break;
                case BluetoothHfDevice.STATE_DISCONNECTED:
                    break;
                case BluetoothHfDevice.STATE_CONNECTING:
                    Log.i(TAG, remoteDevice.getName() + "(" + remoteDevice.getAddress() + ") is BluetoothHfDevice.STATE_CONNECTING");
                    break;
                case BluetoothHfDevice.STATE_CONNECTED:
                    bluetoothDevice = remoteDevice;
                    Log.i(TAG, remoteDevice.getName() + "(" + remoteDevice.getAddress() + ") is BluetoothHfDevice.STATE_CONNECTED");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onAudioStateChange(int newState, int prevState) {
            Log.e(TAG, "onHfDeviceAudioStateChange()" + "newState = " + newState + "prevState" + prevState);
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_AUDIO_STATE;
            msg.arg1 = newState;
            viewUpdateHandler.sendMessageDelayed(msg, 200);
        }

        @Override
        public void onIndicatorsUpdate(int[] indValue) {
            Log.e(TAG, "onHfDeviceIndicatorsUpdate()");
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_DEVICE_INDICATORS;
            msg.obj = indValue;
            viewUpdateHandler.sendMessage(msg);

        }

        @Override
        public void onCallStateChange(int status, int callSetupState, int numActive,
                                      int numHeld, String number, int addrType) {
            Log.e(TAG, "onCallStateChange()" + " callSetupState:"
                    + callSetupState + " numActive:" + numActive + " numHeld:" + numHeld +
                    " isPendingClcc:" + isPendingClcc + " status = " + status + " number==" + number);
            if (BluetoothHfDevice.NO_ERROR == status) {
                isPendingClcc = false;
                if (!isPendingClcc) {
                    if (bluetoothHFDevice.getCLCC()) {
                        isPendingClcc = true;
                        // wait for clcc to update the UI
                        Log.e(TAG, "update the UI");
                    } else {
                        Log.e(TAG, "Get Clcc failed");
                    }
                }

                if (isPendingClcc) {
                    Message msg = Message.obtain();
                    msg.what = BRCMHfDeviceConstants.GUI_UPDATE_CALL_STATUS;
                    msg.obj = new BluetoothCallStateInfo(numActive, callSetupState, numHeld, number);
                    viewUpdateHandler.sendMessageDelayed(msg, 500);
                }
                /*if (callSetupState == 2 && numActive == 0) {
                    dialing = true;
                    startTime = System.currentTimeMillis();

                    Message msg = Message.obtain();
                    msg.what = BRCMHfDeviceConstants.GUI_UPDATE_CALL_STATUS;
                    msg.obj = new BluetoothCallStateInfo(numActive, callSetupState, numHeld, number);
                    viewUpdateHandler.sendMessageDelayed(msg, 300);
                    Log.e(TAG, "System.currentTimeMillis(): 2 =" + System.currentTimeMillis());
                    return;
                }
                if (callSetupState == 3 && numActive == 0) {
                    Log.e(TAG, "System.currentTimeMillis(): 3 =" + System.currentTimeMillis());
                    if (dialing) {
                        altering = true;
                        dialing = false;
                        if (System.currentTimeMillis() - startTime < 200) {
                            altering = false;
                            return;
                        }
                    }
                }
                if (callSetupState == 6 && numActive == 1) {
                    Log.e(TAG, "System.currentTimeMillis(): 6  1 =" + System.currentTimeMillis());
                    //回调 切换私密模式可以点击
                    for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
                        bluetoothPhoneListener.onChangeAudioEnable();
                    }
                    if (dialing) {//2    0
                        calling = true;
                        return;
                    } else if (!dialing && altering) {
                        altering = false;
                        if (System.currentTimeMillis() - startTime < 200) {
                            return;
                        }
                    } else if (!dialing && !altering) {//三星手机   状态和通话完全一致
                        if (System.currentTimeMillis() - startTime < 200) {
                            calling = true;
                            return;
                        }
                    }
                }*/

                if (6 == callSetupState && 0 == numActive && 1 == numHeld) {
//                    update your UI;
                    Log.e(TAG, "updateViewOnActiveCall()--只有一个通话");
                    Intent intent = new Intent(multiCallChangeOneAction);
                    intent.putExtra("waittingPhoneNum", number);
                    intent.putExtra("multiCallReceived", true);
                    mContext.sendBroadcast(intent);

//                    if (BluetoothHfDevice.NO_ERROR == status) {
//                        Message msg = Message.obtain();
//                        msg.what = BRCMHfDeviceConstants.GUI_UPDATE_CLCC_AT_RSP;
//                        viewUpdateHandler.sendMessage(msg);
//                    }
                }
                if (callSetupState == 6 && numActive == 0) {
                    dialing = false;
                    altering = false;
                }
                if (callSetupState == 6 && numActive == 1) {
                    Log.e(TAG, "System.currentTimeMillis(): 6  1 =" + System.currentTimeMillis());
                    //回调 切换私密模式可以点击
                    for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
                        bluetoothPhoneListener.onChangeAudioEnable();
                    }
                    if (callStatus.equals("Waiting")) {
                        Intent intent = new Intent(multiCallChangeOneAction);
                        intent.putExtra("waittingPhoneNum", number);
                        intent.putExtra("multiCallReceived", false);
                        mContext.sendBroadcast(intent);
                        callStatus = "Alerting..";

//                        if (BluetoothHfDevice.NO_ERROR == status) {
//                            Message msg = Message.obtain();
//                            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_CLCC_AT_RSP;
//                            viewUpdateHandler.sendMessage(msg);
//                        }
                    }
                    /*if (dialing) {//2    0
                        calling = true;
                        return;
                    } else if (!dialing && altering) {
                        altering = false;
                        if (System.currentTimeMillis() - startTime < 200) {
                            return;
                        }
                    } else if (!dialing && !altering) {//三星手机   状态和通话完全一致
                        if (System.currentTimeMillis() - startTime < 200) {
                            calling = true;
                            return;
                        }
                    }*/
                }
            } else {
                Log.e(TAG, "Call status failed:" + status);
            }


        }

        @Override
        public void onVRStateChange(int status, int vrState) {
            // TODO Auto-generated method stub
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_VR_STATE;
            msg.arg1 = status;
            msg.arg2 = vrState;
            viewUpdateHandler.sendMessage(msg);

        }

        @Override
        public void onVolumeChange(int volType, int volume) {
            // TODO Auto-generated method stub
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_VOLUME;
            msg.arg1 = volType;
            msg.arg2 = volume;
            viewUpdateHandler.sendMessage(msg);

        }


        @Override
        public void onOperatorSelectionRsp(int status, int mode, String operatorName) {

            if (BluetoothHfDevice.NO_ERROR == status) {
                Log.e(TAG, "onOperatorSelectionRsp()" + operatorName);
                Message msg = Message.obtain();
                msg.what = BRCMHfDeviceConstants.GUI_UPDATE_OPERATOR;
                msg.arg1 = status;
                msg.arg2 = mode;
                msg.obj = operatorName;
                viewUpdateHandler.sendMessage(msg);
            } else {
                Log.e(TAG, "Error in onOperatorSelectionRsp" + status);
            }
        }

        @Override
        public void onSubscriberInfoRsp(int status, String number, int addrType) {

            if (BluetoothHfDevice.NO_ERROR == status) {

                Log.e(TAG, "onSubscriberInfoRsp()" + number);

                Message msg = Message.obtain();
                msg.what = BRCMHfDeviceConstants.GUI_UPDATE_SUBSCRIBER;
                msg.arg1 = status;
                msg.obj = number;
                viewUpdateHandler.sendMessage(msg);
                XxConfig.getInstance().saveLocalNum(number);
            } else {
                Log.e(TAG, "Error in onSubscriberInfoRsp" + status);
            }
        }

        @Override
        public void onExtendedErrorResult(int errorResultCode) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCLCCRsp(int status, List<BluetoothClccInfo> clcc) {
            Log.e(TAG, "onCallListRsp" + status + clcc.size() +
                    "isPendingClcc" + isPendingClcc);

            if (BluetoothHfDevice.NO_ERROR == status) {
                Message msg = Message.obtain();
                msg.what = BRCMHfDeviceConstants.GUI_UPDATE_CLCC_AT_RSP;
                mCallList = clcc;
                viewUpdateHandler.sendMessage(msg);
                Log.d(TAG, clcc.toString());
            }
           /* if (isPendingClcc) {
                if (bluetoothHFDevice.getCLCC()) {
                    isPendingClcc = false;
                    // wait for clcc to update the UI
                    return;
                } else {
                    Log.e(TAG, "onCallListRsp Get Clcc failed");
                }
            }*/
        }

        @Override
        public void onVendorAtRsp(int status, String atRsp) {
            // TODO Auto-generated method stub
            Log.e(TAG, "onVendorAtRsp(): status=" + status + " atRsp=" + atRsp);
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_VENDOR_AT_RSP;
            msg.arg1 = status;
            msg.obj = atRsp;
            viewUpdateHandler.sendMessage(msg);
        }

        @Override
        public synchronized void onPhoneBookReadRsp(int status, List<BluetoothPhoneBookInfo> phoneNumList) {
            Log.e(TAG, "onPhoneBookReadRsp" + status);
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_PHONEBOOK_AT_RSP;
            msg.arg1 = status;
            msg.obj = phoneNumList;
            //mPhoneNumList = phoneNumList;
            viewUpdateHandler.sendMessage(msg);
        }

        @Override
        public void onRingEvent() {
            Log.e(TAG, "onRingEvent"); /* Telechips' Remark */

            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_RING;
            viewUpdateHandler.sendMessage(msg);
            if (mInBandRingStatus == BluetoothHfDevice.INBAND_STATE_OFF && mRingState == BRCMHfDeviceConstants.RING_STATE_IDLE) {
                RingPlay();
            } else if (mInBandRingStatus == BluetoothHfDevice.INBAND_STATE_ON) {
                RingStop();
            }
        }

        @Override
        public void onInBandRingStatusEvent(int inBandRingStatus) {
            // TODO Auto-generated method stub
            /* Telechips' Remark */
            Log.e(TAG, "onInBandRingStatusEvent status = " + inBandRingStatus);
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_IN_BAND_STATUS;
            msg.arg1 = inBandRingStatus;
            viewUpdateHandler.sendMessage(msg);
        }

        @Override
        public void onBIAStatus(int status) {
            Log.e(TAG, "onBIAStatus status: " + status);
        }

        @Override
        public void onNRECEvent(int nrecState) {
            isEnableNREC = bluetoothHFDevice.getDeviceNRECState(bluetoothDevice);
            Log.e(TAG, "onNRECEvent nrecState : " + nrecState + ", isEnableNREC: " + isEnableNREC);
            Message msg = Message.obtain();
            msg.what = BRCMHfDeviceConstants.GUI_UPDATE_NREC_STATUS;
            msg.arg1 = isEnableNREC == true ? 1 : 0;
            viewUpdateHandler.sendMessage(msg);
        }
    }

    /**
     * 处理不同的电话状态
     *
     * @param callInfo
     */
    private void updateViewWithCallStatus(BluetoothCallStateInfo callInfo) {
        int numActive = callInfo.getNumActiveCall();
        int numHeld = callInfo.getNumHeldCall();
        int callSetup = callInfo.getCallSetupState();
        Log.e(TAG, "TestApp" + "numActive" + numActive + "callSetup" + callSetup + "numHeld" + numHeld + " number==" + callInfo.getPhoneNumber());
        //When a call setup is in progress show the status
        //1 判断是否是 waiting alerting dialing incoming 这些状态if (callSetup == 2 && numActive == 0) {
        if ((mCallList != null && mCallList.size() > 0 && mCallList.get(0).equals("+" + XxConfig.getInstance().getSaveLocalNum()))
                || (lastNumber != null && lastNumber.equals("+" + XxConfig.getInstance().getSaveLocalNum()))) {
            return;
        }
        if (isCallSetupInProgress(callInfo)) {
            updateViewOnCallProgress(callInfo);
            callState = callSetup;
            isBtCall = true;
            //TODO 接通
            startActivity(mContext, callState);
            return;
        }
        callState = callSetup;
        //When there is no active calls 挂断刷新界面
        //2 是否是挂断
        if (isPhoneOnHook(callInfo)) {
//            updateViewOnPhoneHook();
//            hangup();
            stopTimer();
            for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
                bluetoothPhoneListener.onUpdateHangup();
                Log.e(TAG, "onUpdateHangup");
                Log.d(TAG, "updateViewWithCallStatus() called with: " + "callInfo = [" + callInfo + "]");
            }
            RingStop();
            isBtCall = false;
            return;
        }
        //When there is  active call 通话中
        //否则通话中
        updateViewOnActiveCall(callInfo);//判断是否通话中

//        if (mTimer != null) {
//            stopTimer();
//        }
//        startTimer();
//        } else {
//            reStartTimer = true;
//        }
//        mUiHandler.sendEmptyMessage(MSG_HANDSFREE_UI_START_CALL);
    }

//    public void canRestartTimer(boolean reStartTimer) {
//        this.reStartTimer = reStartTimer;
//    }

    //各种刷新

    /**
     * This function is used to update the signal status
     * 获取到的手机信号强度
     */
    private void updateSignalStatus(int status, int imageOffset) {
        Log.d(TAG, "updateSignalStatus: Updating signal status");
        for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
            bluetoothPhoneListener.onUpdateSignalStatus(status, imageOffset);
        }
    }

    /**
     * This function is used to update the battery status
     * 电量强度
     */
    private void updateBatteryStatus(int status) {
        Log.d(TAG, "updateBatteryStatus: Updating signal status");
        for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
            bluetoothPhoneListener.onUpdateBatteryStatus(status);
        }
    }

    /**
     * 响铃
     * Start playing the ring sound, and send delayed message when it's time to stop.
     * <p/>
     * the  ring sound duration in milliseconds
     */
    private void RingPlay() {
        // RingStop() checks to see if we are already playing.
        RingStop();

        Log.d(TAG, "RingPlay()");

        Uri alertUri = RingtoneManager.getActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_RINGTONE);
        Log.d(TAG, "uri = " + alertUri.toString());

        mRingtone = RingtoneManager.getRingtone(mContext, alertUri);
        if (mRingtone != null) {
            mRingtone.setStreamType(RingtoneManager.TYPE_RINGTONE);
            mRingtone.play();
            mRingState = BRCMHfDeviceConstants.RING_STATE_RINGING;
        }
    }

    /**
     * 停止铃声
     * Stops ring audio.
     */
    public void RingStop() {
        Log.d(TAG, "RingStop()");

        if (mRingState == BRCMHfDeviceConstants.RING_STATE_RINGING) {
            if (mRingtone.isPlaying()) {
                mRingtone.stop();
            }
        }
        mRingState = BRCMHfDeviceConstants.RING_STATE_IDLE;
    }

    /**
     * 对得到的数据进行操作
     *
     * @param bookInfoList
     */
    public void updateDownloadFileContent(List<BluetoothPhoneBookInfo> bookInfoList) {
        //将下载的电话本进行文字转拼音 然后进行存入数据库
        BluetoothPhoneBookModule.getInstance().setSearchKey(bookInfoList);
    }

    public void releaseResources() {
        if (bluetoothHFDevice != null && mHfDeviceEventHandler != null) {
            bluetoothHFDevice.unregisterEventHandler();
            bluetoothHFDevice.closeProxy();
            bluetoothHFDevice = null;
            hasDownload = false;
            stopTimer();
            cleanPhoneBook();
            mDownLoadCount = 0;
            proxy = false;
            deviceName = "";
            mHfDeviceEventHandler = null;
            mPreferences.edit().putBoolean("isDownload", false).commit();
            bluetoothDevice = null;
            Log.d(TAG, "onStop: un registered event handler..");
        }
        RingStop();
//        updateUi();
    }

    /**
     * This function is used to update the indicators
     * 电量　信号
     */

    public void updateIndicators(int[] indicators) {

        Log.d(TAG, "Service =" + indicators[0] + ",Roam =" + indicators[1] +
                ",Signal =" + indicators[2] + ",Batt chgv =" + indicators[3]);

        if (indicators[BluetoothHfDevice.INDICATOR_TYPE_SERVICE] !=
                BluetoothHfDevice.SERVICE_NOT_AVAILABLE) {

            if ((indicators


                    [BluetoothHfDevice.INDICATOR_TYPE_ROAM] ==
                    BluetoothHfDevice.SERVICE_TYPE_ROAMING)) {
                isRoaming = true;
            } else {
                isRoaming = false;
            }


            currentSignalStrength =
                    indicators[BluetoothHfDevice.INDICATOR_TYPE_SIGNAL];
            if (!isRoaming) {
                // The image offset param indicates image index
                // referrred from stat_signal.xml
                // "0" offset sets the non- roaming images(1-5 index)
                //信号强度
                updateSignalStatus(currentSignalStrength, 0);
            } else {
                // The image offset param indicates image index
                // referrred from stat_signal.xml
                // "5" offset sets the roaming images(6-10 index)
                updateSignalStatus(currentSignalStrength, 5);
            }

        } else {
            Log.d(TAG, "updateIndicators: no active service..");
            // "-1" offset sets the no signal image index (0 index) and signal as zero as no service
            updateSignalStatus(0, -1);
        }

        if (currentBatteryCharge != indicators[BluetoothHfDevice.INDICATOR_TYPE_BATTERY]) {
            currentBatteryCharge = indicators[BluetoothHfDevice.INDICATOR_TYPE_BATTERY];
            //电量多少
            updateBatteryStatus(currentBatteryCharge);
        }

    }

    private Timer mTimer;
    int mTimes;
    TimerTask mTimerTask;

    public void startTimer() {
        if (mTimer != null)
            return;
        Log.e(TAG, "startTimer()");
        mTimes = -1;
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mTimes++;
                Message.obtain(viewUpdateHandler, Constants.PHONE_UPDATE_TIME, mTimes).sendToTarget();
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public void stopTimer() {
        if (mTimer == null)
            return;
        Log.e(TAG, "stopTimer()");
        mTimes = 0;
        mTimerTask.cancel();
        mTimer.purge();
        mTimer.cancel();
        mTimer = null;
        mTimerTask = null;
//        for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
//            bluetoothPhoneListener.onUpdateCallTime(mTimes);
//        }
    }

    private boolean isPhoneOnHook(BluetoothCallStateInfo callInfo) {
        Log.e("isPhoneOnHook", "callInfo.getNumActiveCall():" + callInfo.getNumActiveCall());
        Log.e("isPhoneOnHook", "callInfo.getNumHeldCall():" + callInfo.getNumHeldCall());
        Log.e("isPhoneOnHook", "callInfo.getCallSetupState():" + callInfo.getCallSetupState());
        Log.e("isPhoneOnHook", "BluetoothHfDevice.CALL_SETUP_STATE_IDLE:" + BluetoothHfDevice.CALL_SETUP_STATE_IDLE);
        return ((callInfo.getNumActiveCall() == 0) &&
                (callInfo.getNumHeldCall() == 0) &&
                (callInfo.getCallSetupState() == BluetoothHfDevice.CALL_SETUP_STATE_IDLE));
    }

    private boolean isCallSetupInProgress(BluetoothCallStateInfo callInfo) {
        Log.e("isCallSetupInProgress", "callInfo.getCallSetupState():" + callInfo.getCallSetupState());
        Log.e("isCallSetupInProgress", "BluetoothHfDevice.CALL_SETUP_STATE_IDLE:" + BluetoothHfDevice.CALL_SETUP_STATE_IDLE);
        return (callInfo.getCallSetupState() != BluetoothHfDevice.CALL_SETUP_STATE_IDLE);
    }

    /**
     * 未接通的状态
     *
     * @param callInfo
     */
    private void updateViewOnCallProgress(BluetoothCallStateInfo callInfo) {
        int numActive = callInfo.getNumActiveCall();
        int numHeld = callInfo.getNumHeldCall();
        int callSetup = callInfo.getCallSetupState();
        String number = callInfo.getPhoneNumber();
//        String callStatus = "";
        switch (callSetup) {
            case BluetoothHfDevice.CALL_SETUP_STATE_DIALING:
                callStatus = "Dialing..";
                Log.d(TAG, "updateViewOnCallProgress() called with: " + "callStatus = [" + callStatus + "]");
                break;
            case BluetoothHfDevice.CALL_SETUP_STATE_WAITING:
                if (callState == BluetoothHfDevice.CALL_SETUP_STATE_WAITING)
                    return;//already in waiting state
//                updateViewOnCallWaiting(number);   三方通话
                callStatus = "Waiting";
                Log.e(TAG, "三方通话：updateViewOnCallProgress() called with: " + "callStatus = [" + callStatus + "]");
                Intent intent = new Intent(multiCallStateAction);
                mContext.sendBroadcast(intent);
                break;
            case BluetoothHfDevice.CALL_SETUP_STATE_ALERTING://图标改为挂断图标
                callStatus = "Alerting..";
//                displayState.setText(callStatus);
//                endCall.setText(endCallStr);
//                endCall.setVisibility(View.VISIBLE);
                Log.d(TAG, "updateViewOnCallProgress() called with: " + "callStatus = [" + callStatus + "]");
                break;
            case BluetoothHfDevice.CALL_SETUP_STATE_INCOMING:
                if (null == number) {// number not available
//                    updateViewOnIncoming();//先启动界面
                } else { // when number is availabe 刷新界面
//                    displayNumber.setText(number);
//                    incomingCallNumber = number;
                }
                callStatus = "Incoming";
                break;
            default:
                break;
        }


    }

    /**
     * 通话中状态
     *
     * @param callInfo
     */
    private void updateViewOnActiveCall(BluetoothCallStateInfo callInfo) {
        int numActive = callInfo.getNumActiveCall();
        int numHeld = callInfo.getNumHeldCall();
        int callSetup = callInfo.getCallSetupState();

        //When there is only active call
        if ((0 != numActive) && (0 == numHeld)) {
            //通话中
            Log.e(TAG, "updateViewOnActiveCall()--通话中");
        }
        //When there is only held call
        if ((0 == numActive) && (0 != numHeld)) {
            //只有一个保持通话
            Log.e(TAG, "updateViewOnActiveCall()--只有一个保持通话");

        }
        //When there is more than one call
        if ((0 != numActive) && (0 != numHeld)) {//多方通话
            Log.e(TAG, "updateViewOnActiveCall()--多方通话");
        }
        Log.d(TAG, "updateViewOnActiveCall() called with: " + "callInfo = [" + callInfo + "]");
        startActivity(mContext, callState);//切换到通话界面 重连时切换到通话界面
        startTimer();
        RingStop();
        for (BluetoothPhoneListener bluetoothPhoneListener : listeners) {
            bluetoothPhoneListener.onUpdateUITalking();
        }
        isBtCall = true;
    }

    public boolean isDownload() {
        return hasDownload;
    }

    /**
     * 获取连接设备的名称  如果已经连接   没有获取到名称 则只显示已连接
     *
     * @return
     */
    private String name = "";

    /*
    魅族5手机与车机蓝牙连接成功，点击蓝牙应用进入界面，随机出现左上角显示未连接bug
    原因是已配对的设备列表没有该手机的设备名
     */
    public String getDeviceName() {
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice bondedDevice : bondedDevices) {
                if (BluetoothMusicModule.getInstance().isA2dpConnected(bondedDevice)) {
                    Log.d(TAG, "initData() called bondedDevice = " + bondedDevice.getName());
                    return mContext.getResources().getString(R.string.bt_connected) + bondedDevice.getName();
                }
            }
        }
        return mContext.getResources().getString(R.string.bt_unconnected);
    }

    //------------------------本机电话相关状态------------------------
    private void getCallPhoneListener() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneListener(),
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    class PhoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:// 来电状态
                    BluetoothMusicModule.getInstance().musicPause();
                    isLocalCall = true;
                    /*gw add +*/
                    hangup();
                    /*add end*/
                    Log.d(TAG, "onCallStateChanged() called with: " + "state = [" + state + "], incomingNumber = [" + incomingNumber + "]");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:// 接听状态
                     /*gw add +*/
                    hangup();
                    /*add end*/
                    BluetoothMusicModule.getInstance().musicPause();
                    isLocalCall = true;
                    Log.d(TAG, "onCallStateChanged() called with: " + "state = [" + state + "], incomingNumber = [" + incomingNumber + "]");
                    return;
                case TelephonyManager.CALL_STATE_IDLE:// 挂断后回到空闲状态
                    isLocalCall = false;
                    Log.d(TAG, "onCallStateChanged() called with: " + "state = [" + state + "], incomingNumber = [" + incomingNumber + "]");
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isLocalCall() {
        return isLocalCall;
    }

    public boolean isBtCall() {
        return isBtCall;
    }

    /**
     * 启动activity
     *
     * @param content
     */
    private void startActivity(Context content, int state) {
        if (XxConfig.getInstance().hasInternetConnect()) {
            return;
        }

        ComponentName cn = new ComponentName("com.etrans.jt.bluetooth",
                "com.etrans.jt.bluetooth.view.PhoneCallExActivity");
        Intent in = new Intent();
        in.setComponent(cn);
//        in.setClass(content, PhoneCallExActivity.class);
        in.putExtra("callState", state);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(in);
        Log.d(TAG, "XstartActivity() called with: content = [" + content + "], state = [" + state + "]");
    }

/*    public int requestAudioFoucs() {
        int ret = XxAudioManager.getInstance().requestAudioFocus(
                XxAudioManager.MODULE_PHONE,
                AudioManager.STREAM_NOTIFICATION, AudioManager.AUDIOFOCUS_GAIN);

        if (ret == XxAudioManager.AUDIOFOCUS_REQUEST_FAILED ) {
        }
//        if (ret == XxAudioManager.AUDIOFOCUS_REQUEST_LOWVOLUME_PLAY) {
//            //getBluetoothDevice().setLosesFocusBtearphone(true);
//            mBluetoothDevice.setVolume(10);
//        }
        return XxAudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public int abandonAudioFocus() {
        return XxAudioManager.getInstance().abandonAudioFocus(
                XxAudioManager.MODULE_PHONE);
    }*/ //zwj-

    /********************
     * zwj+
     *************************/
    AudioManager.OnAudioFocusChangeListener mAudioFocusListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        //do nothing
                        Log.d(TAG, "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT");
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //do nothing
                        Log.d(TAG, "AudioFocus: received AUDIOFOCUS_GAIN");
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        //do nothing
                        Log.d(TAG, "AudioFocus: received AUDIOFOCUS_LOSS");
                    }
                }
            };
    AudioManager mAudioManager = null;

    void checkAudioManager(Context ctx) {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    public int requestAudioFoucs(Context ctx) {
        checkAudioManager(ctx);
        return mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN);
    }

    public int abandonAudioFocus(Context ctx) {
        checkAudioManager(ctx);
        return mAudioManager.abandonAudioFocus(mAudioFocusListener);
    }
    /************************* zwj+end ***********************/
}
