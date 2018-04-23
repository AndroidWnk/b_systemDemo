package com.etrans.jt.btlibrary.utils;

import android.content.Context;
import android.media.AudioManager;

import com.etrans.jt.btlibrary.manager.XxConfig;


public class XxVoiceUtils {
	private AudioManager mAudioManager;
	public XxVoiceUtils() {
	}

	public void init(Context context) {
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	public boolean isVoiceSenlice() {
		return (mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0);
	}

	public int getStreamVoice(int streamType) {
		return mAudioManager.getStreamVolume(streamType);
	}

	public void setStreamVoice(int streamType , int size) {
		mAudioManager.setStreamVolume(streamType, size, 0);
	}

	public void setStreamVolume(int streamType,int index,int flags){
		mAudioManager.setStreamVolume(streamType, index, flags);
	}


	public void setStreamMute(int streamType , boolean bMute) {
		if(bMute){
			XxConfig.getInstance().setVoiceAutoMute(true);
		}else {
			XxConfig.getInstance().setVoiceAutoMute(false);
		}
		mAudioManager.setStreamMute(streamType, bMute);
	}

	public boolean isMusicActive() {
		return mAudioManager.isMusicActive();
	}

	public int getMaxVolume(int streamType) {
		return mAudioManager.getStreamMaxVolume(streamType);
	}

	public void setRingerMode(int ringerMode){
		mAudioManager.setRingerMode(ringerMode);
	}


	public void setRadioLosesFocus(boolean on) {
		mAudioManager.setRadioLosesFocus(on);
	}

}
