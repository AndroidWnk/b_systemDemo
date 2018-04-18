// IXxAudioMamager.aidl
package com.linkgent.xxaidl;
import com.linkgent.xxaidl.IXxAudioFocusChange;
import com.linkgent.xxaidl.IXxPhoneStateListener;
interface IXxAudioService {
    /**
    * 请求音频焦点
    *
    * @return
    * {@link XxAudioConfig.AUDIOFOCUS_REQUEST_FAILED}{@link XxAudioConfig.AUDIOFOCUS_REQUEST_GRANTED}
    * {@link XxAudioConfig.AUDIOFOCUS_REQUEST_LOWVOLUME_PLAY}{@link XxAudioConfig.AUDIOFOCUS_REQUEST_PHONE}
    * {@link XxAudioConfig.AUDIOFOCUS_REQUEST_BTEARPHONE}
    */
    int requestAudioFocus(int module, int stream,int durationHint);

    /**
    *  请求音频焦点
    *
    * @return
    * {@link XxAudioConfig.AUDIOFOCUS_REQUEST_FAILED}{@link XxAudioConfig.AUDIOFOCUS_REQUEST_GRANTED}
    * {@link XxAudioConfig.AUDIOFOCUS_REQUEST_LOWVOLUME_PLAY}{@link XxAudioConfig.AUDIOFOCUS_REQUEST_PHONE}
    * {@link XxAudioConfig.AUDIOFOCUS_REQUEST_BTEARPHONE}
    */
    int requestDefaultAudioFocus(int module,int stream);

    /**
    * 释放音频焦点
    */
    int abandonAudioFocus(int module);

    /**
    * 添加音频焦点监听器
    */
    void addIXxAudioFocusChange(int module,in IXxAudioFocusChange listener);

    /**
    * 移除音频焦点监听器
    */
    void removeIXxAudioFocusChange(int module);


    //void addIXxPhoneStateListener(IXxPhoneStateListener listener);

    //void removeIXxPhoneStateListener(IXxPhoneStateListener lisetner);

    /**
    * 获取当前的音频模块   by heyaobao 2017/03/02
    */
    int getCurrentStreamModule();

}

