package com.etrans.jt.btlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.etrans.jt.btlibrary.manager.XxConfig;
import com.etrans.jt.btlibrary.manager.XxNetManager;
import com.etrans.jt.btlibrary.manager.XxOtherSettingManager;
import com.etrans.jt.btlibrary.utils.XxMessage;


public class XxHardwareReceiver extends BroadcastReceiver {

    public static final String WIFITAG = "NewWifiLog";
    public static final String VOICETAG = "NewVoiceLog";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            Log.i(WIFITAG, "action = " + action);
        }
        if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            XxNetManager.getInstance().getWifiManager().scanWifiFinish();
        } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            Log.i(WIFITAG, "EXTRA_WIFI_STATE = " + intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0));
            Log.i(WIFITAG, "EXTRA_PREVIOUS_WIFI_STATE = " + intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, 0));
            XxNetManager.getInstance().refreshUI(XxMessage.MSG_SETTING_RE_WIFI, intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0), intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, 0));
        } else if (action.equals("android.net.wifi.WIFI_AP_STATE_CHANGED")) {
            Log.i(WIFITAG, "wifi_state = " + intent.getIntExtra("wifi_state", 0));
            Log.i(WIFITAG, "previous_wifi_state = " + intent.getIntExtra("previous_wifi_state", 0));
            XxNetManager.getInstance().refreshUI(XxMessage.MSG_SETTING_RE_AP, intent.getIntExtra("wifi_state", 0), intent.getIntExtra("previous_wifi_state", 0));
        } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            Log.i(WIFITAG, "EXTRA_NETWORK_INFO = " + intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO));
            XxNetManager.getInstance().getWifiManager().startScan();
        } else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            Parcelable parcelable = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if (!parcelable.toString().equals("COMPLETED")) {
                XxNetManager.getInstance().setWifiHadConnected(false);
            }
            XxNetManager.getInstance().getWifiManager().updateConnectingDesc();
        } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            XxNetManager.getInstance().notifyWifiConnected(); //判断连接成功
            //XxEmergencyPhoneManager.getInstance().requestEmergencyPhone();
            /*if(!XxOtherSettingManager.getInstance().getUpgradeSystemManager().isHadLoaded()) {
				XxOtherSettingManager.getInstance().getUpgradeSystemManager().checkServerVersion();
			}*/
            //if(!XxSystemUpgradeManager.bHaveInvateServer) {
            //XxSystemUpgradeManager.getInstance().init(context);
            //}
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            if (activeInfo != null && activeInfo.isAvailable()) {
//                XxBBStandbyWeatherManager.getInstance().requestNetWork();// 网络连接成功 ， 请求天气
            } else {
//                XxBBStandbyWeatherManager.getInstance().requestNetError();
            }
        } else if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
            int type = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -100);
            int volu = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -100);
            XxOtherSettingManager.getInstance().refreshUI(XxMessage.MSG_SETTING_RE_VOICE, volu, type);
        } else if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
            XxNetManager.getInstance().refreshUI(XxMessage.MSG_SETTING_RE_MOBILE_DATA, 0, 0);
            XxNetManager.getInstance().getTelephoneManager().updateWifiApName();
        } /*else if (action.equals("com.uniits.map.loadcomplete") || action.equals("com.uniits.brightbean.loadcomlete")) {
            XxNaviManager.getInstance().sendIntentAction();
        } else if (action.equals("com.uniits.district.CHANGED")) {  //提供给测试使用的接口
            String s = intent.getStringExtra("districtname");
            XxBBStandbyWeatherManager.getInstance().setNetList(intent.getStringExtra("districtname"), XxBBStandbyWeatherManager.REQUEST_WEATHER);
            //XxBBStandbyWeatherManager.getInstance().onDistrictChanged1(intent.getStringExtra("districtname"), 0);
            XxConfig.getInstance().setLocation(intent.getStringExtra("districtname"));
            XxRadioManager.getInstance().refreshChannelName();
        } */else if (action.equals(Intent.ACTION_SHUTDOWN)) {
//            XxRadioManager.getInstance().saveRadioInfo(false);
            //	XxSystemUpgradeManager.getInstance().upgrade();
        } /*else if (action.equals(XxBroadcast.BROADCAST_CAN_CARSPEED)) {
            int speed = intent.getIntExtra("speed", 0);
//            XxMediaManager.getInstance().setCarSpeed(speed);
        }*/ else if (action.equals("android.vehicle.action.EXTRAKEY")) {
            XxNetManager.getInstance().checkDeviceId();
//            if (XxNetManager.getInstance().isCanCallOut() && 61 == intent.getIntExtra("key", 0)) {
//                Uri uri = Uri.parse("tel:" + XxEmergencyPhoneManager.getInstance().getEmergencyPhone() + "abcd");
//                Intent call = new Intent(Intent.ACTION_CALL, uri); // 直接播出电话  sos
//                call.putExtra("forceUseGsmDialer", true);
//                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(call);
//            } else if (XxNetManager.getInstance().isCanCallOut() && 62 == intent.getIntExtra("key", 0)) {
//                Uri uri = Uri.parse("tel:" + XxServicePhoneManager.getInstance().getServicePhone() + "abcd");
//                Intent call = new Intent(Intent.ACTION_CALL, uri); // 直接播出电话   service
//                call.putExtra("forceUseGsmDialer", true);
//                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(call);
//            }
        } else if (action.equals("android.vehicle.amplifier.MUTE")) {
            boolean bMute = intent.getBooleanExtra("bMute", false);
            Log.i("SystemMute", "bMute = " + bMute);
            XxOtherSettingManager.getInstance().changeVoiceStatus(bMute);

        } else if (action.equals("android.vehicle.amplifier.MUTE")) {
            Log.i("SystemMute", "bMute = ");
        } else if (action.equals("android.intent.system.isp")) {
            if (intent.getIntExtra("isp", 0) == 1) {
            }
        } else if (action.equals("android.intent.action.SERVICE_STATE")) {
            Log.i("updateMobileSignal", "      TelephonyIntents.ACTION_SERVICE_STATE_CHANGED");
//            ServiceState ss = ServiceState.newFromBundle(intent.getExtras());
//            XxConfig.getInstance().setMobileState(ss.getState());
//            XxNetManager.getInstance().getTelephoneManager().onServiceStateChanged(ss);
        } else if (action.equals("com.etrans.launcher.phone")) {
            String sosPhone = intent.getStringExtra("sos");
            String servicePhone = intent.getStringExtra("service");
            Log.i("getPhoneNumber", "sos" + sosPhone);
            XxConfig.getInstance().setEmergencyPhone(sosPhone);
            XxConfig.getInstance().setServicePhone(servicePhone);
        }

    }
}
