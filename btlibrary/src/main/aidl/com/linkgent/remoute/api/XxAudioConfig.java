package com.linkgent.remoute.api;

/**
 * Created by Administrator on 2017/3/2.
 */

public class XxAudioConfig {
    public static final String AUDIO_SERVICE = "com.linkgent.remote.service.XXAUDIOSERVICE";
    public static final String EXTRA_MODULE_LIST = "module_list";
    public static final String ACTION_AUDIO = "com.linkgent.remote.action.AUDIO";
    public static final String EXTRA_AUDIOSTATE = "state";
    public static final int AUDIOSERVICE_START = 0;
    public static final int AUDIOSERVICE_STOP = 1;
    public static final int MODULE_UNKNOW = -1;
    public static final int MODULE_PHONE = 0;
    public static final int MODULE_BTEARPHONE = 1;
    public static final int MODULE_VOICE = 2;
    public static final int MODULE_NAVI = 3;
    public static final int MODULE_TTS = 4;
    public static final int MODULE_MUSIC = 5;
    public static final int MODULE_RADIO = 6;
    public static final int MODULE_VEDIO = 7;
    public static final int MODULE_BTEARMUSIC = 8;
    public static final int AUDIOFOUCS_PAUSE = 1;
    public static final int AUDIOFOUCS_STOP = 2;
    public static final int AUDIOFOUCS_RESUME = 3;
    public static final int AUDIOFOUCS_LOWERVOLUME = 4;
    public static final int AUDIOFOUCS_RESUMEVOLUME = 5;
    public static final int AUDIOFOUCS_ABANDON = 6;
    public static final int AUDIOFOUCS_RESTART_PLAY = 7;
    public static final int AUDIOFOCUS_REQUEST_FAILED = 0;
    public static final int AUDIOFOCUS_REQUEST_GRANTED = 1;
    public static final int AUDIOFOCUS_REQUEST_LOWVOLUME_PLAY = 2;
    public static final int AUDIOFOCUS_REQUEST_PHONE = 3;
    public static final int AUDIOFOCUS_REQUEST_BTEARPHONE = 4;

    public XxAudioConfig() {
    }
}
