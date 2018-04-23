package com.etrans.jt.bluetooth.base;

import android.app.Activity;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.etrans.jt.bluetooth.BTApplication;
import com.etrans.jt.bluetooth.R;
import com.etrans.jt.btlibrary.domin.MobileParams;
import com.etrans.jt.btlibrary.domin.VoiceParams;
import com.etrans.jt.btlibrary.gps.XxGpsManager;
import com.etrans.jt.btlibrary.manager.XxBaseModule;
import com.etrans.jt.btlibrary.manager.XxNetManager;
import com.etrans.jt.btlibrary.manager.XxNotificationManager;
import com.etrans.jt.btlibrary.manager.XxOtherSettingManager;
import com.etrans.jt.btlibrary.manager.XxTelephoneManager;
import com.etrans.jt.btlibrary.utils.TimeUtil;
import com.etrans.jt.btlibrary.utils.XxMessage;

import butterknife.ButterKnife;


/**
 * Created by liyanze on 2016/3/8.
 * <p/>
 * Last Change by fuxiaolei on 2016/3/15.
 */
public abstract class BaseActivity extends Activity implements XxBaseModule.IUpdateUI {

    protected BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BTApplication.appInstance.addActivity(this);
        setContentView(getLayout());
        ButterKnife.bind(this);
        init();
        presenter = initPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BTApplication.appInstance.removeActivity(this);
        if (presenter != null) {
            presenter.onDestory();
        }
        XxNotificationManager.getInstance().removeUpdateUIListener(this);
        XxNetManager.getInstance().removeUpdateUIListener(this);
        XxOtherSettingManager.getInstance().removeUpdateUIListener(this);
    }

    public abstract void init();

    protected abstract BasePresenter initPresenter();

    /**
     * 获取布局id
     *
     * @return
     */
    public abstract int getLayout();
    private TextView mCurrentTime;
    private ImageView mWifiSignal;
    private ImageView mMobileState;
    private ImageView mMobileSignal;
    private ImageView mMobileType;
    private ImageView mGpsState;
    private ImageView mBlueToothState;
    private ImageView mSoundState;
    private TextView topTitle;
    private ImageView back;

//    private int mLastWifiStatus = WifiManager.WIFI_STATE_UNKNOWN;

    public void initTopView(String title) {
        mCurrentTime = (TextView) findViewById(R.id.notification_time);
        mWifiSignal = (ImageView) findViewById(R.id.notification_wifi_signal);
        mMobileSignal = (ImageView) findViewById(R.id.notification_mobile_signal);
        mMobileType = (ImageView) findViewById(R.id.notification_mobile_type);
        mGpsState = (ImageView) findViewById(R.id.notification_gps_state);
        mBlueToothState = (ImageView) findViewById(R.id.notification_bluetooth_state);
        mSoundState = (ImageView) findViewById(R.id.notification_sound_state);
        if(title != null) {
            topTitle = (TextView) findViewById(R.id.tv_title);
            topTitle.setText(title);
        }
        if(null != title) {
            back = (ImageView) findViewById(R.id.iv_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

//        initTopData();
    }

    private void initTopData() {
        XxNotificationManager.getInstance().addUpdateUIListener(this);
        XxNetManager.getInstance().addUpdateUIListener(this);
        XxOtherSettingManager.getInstance().addUpdateUIListener(this);
        //XxBluetoothPhoneManager.getInstance().addUpdateUIListener(this);
        refreshTopTime();
        refreshWifiSignal(XxNetManager.getInstance().getWifiManager().getWifiStatus());
        refreshGpsState(XxGpsManager.getInstance().getGpsState());
        updateMobileSignal(XxNetManager.getInstance().getTelephoneManager().getServiceState(), XxNetManager.getInstance().getTelephoneManager().getLevel());
        refreshMobileData(XxNetManager.getInstance().getTelephoneManager().getServiceState());
        refreshSoundState(XxOtherSettingManager.getInstance().isMute() ? 0 : 1);
        int wifiSignal = XxNetManager.getInstance().getWifiSignal();
        refreshWifiSignal(wifiSignal);
//        refreshTopStatusPosition();
        boolean bluetoothState = XxOtherSettingManager.getInstance().isBtIsOpen();
        //if (XxBluetoothPhoneManager.getInstance().getState() != IXxBluetoothDevice.XxBluetoothState.STATE_OFF)
        refreshBlueToothState(bluetoothState);
    }

    private void refreshSoundState(int state) {
        if (mSoundState == null) {
            return;
        }
        if (state == 0)
            mSoundState.setBackgroundResource(R.drawable.icon_sound_off);
        else
            mSoundState.setBackgroundResource(R.drawable.icon_sound_on);
    }

    private void refreshBlueToothState(boolean state) {
        if (mBlueToothState == null) {
            return;
        }
        if (state)
            mBlueToothState.setBackgroundResource(R.drawable.icon_bluetooth_on);
        else
            mBlueToothState.setBackgroundResource(R.drawable.icon_bluetooth_off);
    }

    private void refreshGpsState(int state) {
        if (mGpsState == null) {
            return;
        }
        switch (state) {
            case XxGpsManager.STATE_AVAILABLE: {
                mGpsState.setVisibility(View.VISIBLE);
                mGpsState.setBackgroundResource(R.drawable.icon_gps_valid);
                break;
            }
            default: {
                mGpsState.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void refreshWifiSignal(int signal) {
        if (mWifiSignal == null) {
            return;
        }
        NetworkInfo netWorkInfo = XxNetManager.getInstance().getConnectivityManager().getNetWorkInfo(ConnectivityManager.TYPE_WIFI);
        if (netWorkInfo != null) {
            NetworkInfo.State state = netWorkInfo.getState();
            if (state == NetworkInfo.State.CONNECTED) {
                mWifiSignal.setVisibility(View.VISIBLE);
            } else {
                mWifiSignal.setVisibility(View.GONE);
            }
        }
        if (Math.abs(signal) > 100) {
            mWifiSignal.setImageResource(R.drawable.icon_wifi_signal_0);
        } else if (Math.abs(signal) > 80) {
            mWifiSignal.setImageResource(R.drawable.icon_wifi_signal_1);
        } else if (Math.abs(signal) > 70) {
            mWifiSignal.setImageResource(R.drawable.icon_wifi_signal_1);
        } else if (Math.abs(signal) > 60) {
            mWifiSignal.setImageResource(R.drawable.icon_wifi_signal_2);
        } else if (Math.abs(signal) > 50) {
            mWifiSignal.setImageResource(R.drawable.icon_wifi_signal_2);
        } else {
            mWifiSignal.setImageResource(R.drawable.icon_wifi_signal_3);
        }
    }

    private void refreshMobileData(int serviceState) {
        if (mMobileType == null) {
            return;
        }
        if (serviceState == ServiceState.STATE_OUT_OF_SERVICE) {
            mMobileType.setVisibility(View.GONE);
            return;
        } else {
            mMobileType.setVisibility(View.VISIBLE);
        }
        XxTelephoneManager manager = XxNetManager.getInstance().getTelephoneManager();
        int status = manager.getTelephoneState();
        if (status == TelephonyManager.SIM_STATE_READY) {
            boolean mobileDataStatus = manager.getMobileDataState(null);
            int type = manager.getMobileType();
            if (mobileDataStatus && type != TelephonyManager.NETWORK_CLASS_UNKNOWN) {

                switch (type) {
                    case TelephonyManager.NETWORK_CLASS_2_G:
                        mMobileType.setBackgroundResource(R.drawable.icon_mobile_type_2g);
                        break;
                    case TelephonyManager.NETWORK_CLASS_3_G:
                        mMobileType.setBackgroundResource(R.drawable.icon_mobile_type_3g);
                        break;
                    case TelephonyManager.NETWORK_CLASS_4_G:
                        mMobileType.setBackgroundResource(R.drawable.icon_mobile_type_4g);
                        break;
                    default:
                        break;
                }

                //mMobileState.setVisibility(View.VISIBLE);
                mMobileType.setVisibility(View.VISIBLE);
            } else {
                //mMobileSignal.setBackgroundResource(R.drawable.icon_mobile_signal_0);
                //mMobileState.setVisibility(View.INVISIBLE);
                mMobileType.setVisibility(View.GONE);
            }
        } else {

            //mMobileSignal.setBackgroundResource(R.drawable.icon_mobile_signal_0);
            //mMobileState.setVisibility(View.INVISIBLE);
            mMobileType.setVisibility(View.GONE);
        }
    }

    private int mLastWifiStatus = WifiManager.WIFI_STATE_UNKNOWN;

    public void refreshWifi(int wifiStatus) {
        if (mWifiSignal == null) {
            return;
        }
        switch (wifiStatus) {
            case WifiManager.WIFI_STATE_ENABLED:
                if (mLastWifiStatus == WifiManager.WIFI_STATE_UNKNOWN || mLastWifiStatus == WifiManager.WIFI_STATE_ENABLING) {
                    NetworkInfo netWorkInfo = XxNetManager.getInstance().getConnectivityManager().getNetWorkInfo(ConnectivityManager.TYPE_WIFI);
                    if (netWorkInfo != null) {
                        NetworkInfo.State state = netWorkInfo.getState();
                        if (state == NetworkInfo.State.CONNECTED) {
                            mWifiSignal.setVisibility(View.VISIBLE);
                        } else {
                            mWifiSignal.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                if (mLastWifiStatus == WifiManager.WIFI_STATE_UNKNOWN || mLastWifiStatus == WifiManager.WIFI_STATE_DISABLING) {
                    mWifiSignal.setVisibility(View.GONE);
                }
                break;
            default:
                mWifiSignal.setVisibility(View.GONE);
                break;
        }
        mLastWifiStatus = wifiStatus;
    }

    private void updateMobileSignal(int serviceState, int level) {
        if (mMobileSignal == null) {
            return;
        }
        refreshMobileData(serviceState);
        if (serviceState == ServiceState.STATE_OUT_OF_SERVICE) {
            mMobileSignal.setVisibility(View.GONE);
            return;
        } else {
            mMobileSignal.setVisibility(View.VISIBLE);
        }

        switch (level) {
            case -1: {
                mMobileSignal.setBackgroundResource(R.drawable.icon_mobile_signal_0);
                break;
            }
            case 0: {
                mMobileSignal.setBackgroundResource(R.drawable.icon_mobile_signal_0);
                break;
            }
            case 1: {
                mMobileSignal.setBackgroundResource(R.drawable.icon_mobile_signal_1);
                break;
            }
            case 2: {
                mMobileSignal.setBackgroundResource(R.drawable.icon_mobile_signal_2);
                break;
            }
            case 3: {
                mMobileSignal.setBackgroundResource(R.drawable.icon_mobile_signal_3);
                break;
            }
            case 4: {
                mMobileSignal.setBackgroundResource(R.drawable.icon_mobile_signal_4);
                break;
            }
            default:
                break;
        }
    }

    private void refreshTopTime() {
        mCurrentTime.setText(TimeUtil.getTopTime());
    }

    public void refreshTopUI(Message msg){
        if (msg.what == XxMessage.MSG_NOTIFICATION_TIME) {
            refreshTopTime();
            XxNetManager.getInstance().getTelephoneManager().getNetworkClass();
            refreshMobileData(XxNetManager.getInstance().getTelephoneManager().getServiceState());
            updateMobileSignal(XxNetManager.getInstance().getTelephoneManager().getServiceState(), XxNetManager.getInstance().getTelephoneManager().getLevel());
            int wifiSignal = XxNetManager.getInstance().getWifiSignal();
            refreshWifiSignal(wifiSignal);
        } else if (msg.what == XxMessage.MSG_NOTIFICATION_MOBILE) {
            MobileParams params = (MobileParams) msg.obj;
            updateMobileSignal(params.serviceState, params.level);
        } else if (msg.what == XxMessage.MSG_SETTING_RE_MOBILE_DATA) {
            refreshMobileData(XxNetManager.getInstance().getTelephoneManager().getServiceState());
        } else if (msg.what == XxMessage.MSG_SETTING_RE_WIFI_AND_AP) {
            refreshWifi(msg.arg1);
        } else if (msg.what == XxMessage.MSG_NOTIFICATION_GPS_STATE) {
            refreshGpsState(msg.arg1);
        } else if (msg.what == XxMessage.MSG_BTEARPHONE_OFF) {
            refreshBlueToothState(false);
        } else if (msg.what == XxMessage.MSG_BTEARPHONE_ON) {
            refreshBlueToothState(true);
        } else if (msg.what == XxMessage.MSG_SETTING_RE_VOICE) {
            VoiceParams params = (VoiceParams) msg.obj;
            if (params.refreshType == AudioManager.STREAM_MUSIC) {
                refreshSoundState(params.bMute);
            }
        }
    }


    @Override
    public void onUpdateUI(Message msg) {}

}
