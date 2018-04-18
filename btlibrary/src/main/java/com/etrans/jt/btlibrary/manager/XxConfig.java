package com.etrans.jt.btlibrary.manager;

import android.content.Context;
import android.content.SharedPreferences;


public class XxConfig {
    private static XxConfig mConfig = new XxConfig();
    private static SharedPreferences mPreferences = null;

    private XxConfig() {
    }

    public static XxConfig getInstance() {
        return mConfig;
    }

    public XxConfig init(Context context, String name) {
        if (mPreferences == null)
            mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return mConfig;
    }

    public void setBackgroundResId(int id) {
        mPreferences.edit().putInt("backResId", id).commit();
    }

    public int getBackgourndResId() {
        return mPreferences.getInt("backResId", -1);
    }

    public void setLaunchView(String name) {
        mPreferences.edit().putString("launch_view", name).commit();
    }

    public String getLaunchView() {
        return mPreferences.getString("launch_view", null);
    }

    public void setMusicType(int type) {
        mPreferences.edit().putInt("music_type", type).commit();
    }

   /* public int getMusicType() {
        return mPreferences.getInt("music_type", XxMediaManager.TYPE_LIST_LOCAL);
    }*/

    public void setCurMusic(String url) {
        mPreferences.edit().putString("music_sel", url).commit();
    }

    public String getCurMusic() {
        return mPreferences.getString("music_sel", "");
    }

    public void setPlayMode(int mode) {
        mPreferences.edit().putInt("play_mode", mode).commit();
    }

   /* public int getPlayMode() {
        return mPreferences.getInt("play_mode", XxMediaManager.CYCLEMODE_SEQUENCE);
    }*/

    public void setCurVideo(String url) {
        mPreferences.edit().putString("video_sel", url).commit();
    }

    public String getCurVideo() {
        return mPreferences.getString("video_sel", "");
    }

    public void setMusicVolume(int volume) {
        mPreferences.edit().putInt("MusicVolume", volume);
    }

    public int getMusicVolume() {
        return mPreferences.getInt("MusicVolume", 0);
    }

    public void setMusicMuteByOper(boolean bOper) {
        mPreferences.edit().putBoolean("MusicMuteOper", bOper).commit();
    }

    public boolean getMusicMuteByOper() {
        return mPreferences.getBoolean("MusicMuteOper", false);
    }


    public void setWifiApAutoOpen(boolean bAutoOpen) {
        mPreferences.edit().putBoolean("setting_wifiap_auto_open", bAutoOpen).commit();
    }

    public boolean isWifiApAutoOpen() {
        return mPreferences.getBoolean("setting_wifiap_auto_open", false);
    }

    public void setWifiAutoOpen(boolean bWifiAutoOpen) {
        mPreferences.edit().putBoolean("setting_wifi_auto_open", bWifiAutoOpen).commit();
    }

    public boolean isWifiAutoOpen() {
        return mPreferences.getBoolean("setting_wifi_auto_open", false);
    }


    public void setRadioAmFreq(int freq) {
        mPreferences.edit().putInt("radio_amfreq", freq).commit();
    }

    /*  public int getRadioAmFreq() {
          return mPreferences.getInt("radio_amfreq", XxRadioConst.RadioStandard.AMMINFREQ);
      }
  */
    public void setRadioFmFreq(int freq) {
        mPreferences.edit().putInt("radio_fmfreq", freq).commit();
    }

   /* public int getRadioFmFreq() {
        return mPreferences.getInt("radio_fmfreq", XxRadioConst.RadioStandard.FMMINFREQ);
    }*/

    public void setRadioBand(int band) {
        mPreferences.edit().putInt("radio_band", band).commit();
    }

   /* public int getRadioBand() {
        return mPreferences.getInt("radio_band", XxRadioConst.Band.FM);
    }*/

    public void setRadioFmChannels(String channels) {
        mPreferences.edit().putString("radio_fmchannels", channels).commit();
    }

    public String getRadioFmChannels() {
        return mPreferences.getString("radio_fmchannels", null);
    }

    public void setRadioAmChannels(String channels) {
        mPreferences.edit().putString("radio_amchannels", channels).commit();
    }

    public String getRadioAmChannels() {
        return mPreferences.getString("radio_amchannels", null);
    }

    public void setRadioFmChannelsIndex(int index) {
        mPreferences.edit().putInt("radio_fmchannelsindex", index).commit();
    }

    public int getRadioFmChannelsIndex() {
        return mPreferences.getInt("radio_fmchannelsindex", 0);
    }

    public void setRadioAmChannelsIndex(int index) {
        mPreferences.edit().putInt("radio_amchannelsindex", index).commit();
    }

    public int getRadioAmChannelsIndex() {
        return mPreferences.getInt("radio_amchannelsindex", 0);
    }

    public void setLocation(String cityName) {
        if (cityName.endsWith("市") || cityName.endsWith("区") || cityName.endsWith("县")) {
            cityName = cityName.substring(0, cityName.length() - 1);
        }
        mPreferences.edit().putString("local_cityName", cityName).commit();
    }


    public String getLocation() {
        return mPreferences.getString("local_cityName", "广州");
    }

    public String getRadioLocation() {
        return mPreferences.getString("local_radiocityName", "广州");
    }

    public void setRadioLocation(String cityName) {
        if (cityName.endsWith("市") || cityName.endsWith("区") || cityName.endsWith("县")) {
            cityName = cityName.substring(0, cityName.length() - 1);
        }
        mPreferences.edit().putString("local_radiocityName", cityName).commit();
    }

    public void setDefalutSetting(boolean bFirst) {
        mPreferences.edit().putBoolean("setting_is_first_init", bFirst).commit();
    }

    public boolean isDefalutSetting() {
        return mPreferences.getBoolean("setting_is_first_init", false);
    }


    public boolean isVoiceAutoMute() {
        return mPreferences.getBoolean("setting_voice_auto_mute", false);
    }

    public void setVoiceAutoMute(boolean bMute) {
        mPreferences.edit().putBoolean("setting_voice_auto_mute", bMute).commit();
    }

    public void setFirstInitProcessDB(boolean nbFirst) {
        mPreferences.edit().putBoolean("processDB_first", nbFirst).commit();
    }

    public boolean isFirstInitProcessDB() {
        return mPreferences.getBoolean("processDB_first", false);
    }

    public void setEmergencyPhone(String phone) {
        mPreferences.edit().putString("emergency_phone", phone).commit();
    }

    public String getEmergencyPhone() {
        return mPreferences.getString("emergency_phone", "4006539128");
    }

    public void setServicePhone(String phone) {
        mPreferences.edit().putString("service_phone", phone).commit();
    }

    public String getServicePhone() {
        return mPreferences.getString("service_phone", "4006539128");
    }

    public void setLoadType(int type) {
        mPreferences.edit().putInt("linkgent_loading_type", type).commit();
    }

    public int getLoadType() {
        return mPreferences.getInt("linkgent_loading_type", 0);
    }

    public int getDistrict() {
        return mPreferences.getInt("gps_district", 0);
    }

    public String getCity() {
        return mPreferences.getString("gps_city", "广州市天河区");
    }

    public void setDistrict(int district) {
        mPreferences.edit().putInt("gps_district", district).commit();
    }

    public void setCity(String city) {
        mPreferences.edit().putString("gps_city", city).commit();
    }

    public String getWeatherCity() {
        return mPreferences.getString("weather_city", "广州市天河区");
    }

    public void setWeatherCity(String city) {
        mPreferences.edit().putString("weather_city", city).commit();
    }

    public String getBtearName() {
        return mPreferences.getString("btear_name", "audio");
    }

    public void setBtearName(String str) {
        mPreferences.edit().putString("btear_name", str).apply();
    }

    public String getBtearPwd() {
        return mPreferences.getString("btear_pwd", "0000");
    }

    public void setBtearPwd(String str) {
        mPreferences.edit().putString("btear_pwd", str).apply();
    }

    public void setLockHour(String hour) {
        mPreferences.edit().putString("lock_hour", hour).commit();
    }

    public String getLockHour() {
        return mPreferences.getString("lock_hour", "");
    }

    public void setLockMin(String miu) {
        mPreferences.edit().putString("lock_min", miu).commit();
    }

    public String getLockMin() {
        return mPreferences.getString("lock_min", "");
    }

    public void setLockSec(String sec) {
        mPreferences.edit().putString("lock_sec", sec).commit();
    }

    public String getLockSec() {
        return mPreferences.getString("lock_sec", "");
    }

    public void setPermanentLock(boolean flag) {
        mPreferences.edit().putBoolean("lock_permanent", flag).commit();
    }

    public boolean getPermanentLock() {
        return mPreferences.getBoolean("lock_permanent", false);
    }


    public void setWifiAPName(String wifiApName) {
        mPreferences.edit().putString("wifi_ap_name", wifiApName).commit();
    }

    public String getWifiAPName() {
        return mPreferences.getString("wifi_ap_name", "mycar_wifiap");
    }

    public void setWifiAPPass(String wifiApPass) {
        mPreferences.edit().putString("wifi_ap_pass", wifiApPass).commit();
    }

    public String getWifiApPass() {
        return mPreferences.getString("wifi_ap_pass", "12345678");
    }

    //保存启动的状态
    public void saveLauncherState(boolean bState) {
        mPreferences.edit().putBoolean("launcher_state", bState).commit();
    }

    public boolean getLauncherState(boolean defVal) {
        return mPreferences.getBoolean("launcher_state", defVal);
    }

    public boolean isPlayedRadio() {
        return mPreferences.getBoolean("is_played_radio", false);
    }

    public void setPlayedRadio(boolean bPlayed) {
        mPreferences.edit().putBoolean("is_played_radio", bPlayed).commit();
    }

    public String getBindUid() {
        return mPreferences.getString("uid", "-1");
    }

    public void setBindUid(String uid) {
        mPreferences.edit().putString("uid", uid).commit();
    }

    public String getAccount() {
        return mPreferences.getString("bind_account", "");
    }

    public void setAccount(String account) {
        mPreferences.edit().putString("bind_account", account).commit();
    }


    public String getMusicInnerSound() {
        return mPreferences.getString("inner_sound", "");
    }

    public void setMusicInnerSound(String value) {
        mPreferences.edit().putString("inner_sound", value).commit();
    }

    public void setMobileState(int state) {
        mPreferences.edit().putInt("mobile_state", state).commit();
    }

    public int getMobileState() {
        return mPreferences.getInt("mobile_state", 1);
    }

    public void setInternetState(boolean b) {
        mPreferences.edit().putBoolean("internet_state", b).commit();
    }

    public boolean hasInternetConnect() {
        return mPreferences.getBoolean("internet_state", false);
    }

    public void saveLocalNum(String number) {
        mPreferences.edit().putString("local_num", number).commit();
    }

    public String getSaveLocalNum() {
        return mPreferences.getString("local_num", "");
    }
}
