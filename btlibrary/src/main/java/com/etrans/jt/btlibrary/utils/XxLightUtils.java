package com.etrans.jt.btlibrary.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

public class XxLightUtils {
	public static final int MIN_BRIGHTNESS = 10;
	public static final int MAX_BRIGHTNESS = 255;
	private Context mContext;
	
	public XxLightUtils() {
		
	}
	
	public void init(Context context) {
		this.mContext = context;
	}
	
	/**
	 * 获得当前系统的亮度模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	public int getBrightnessMode() {
		int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
		try {
			brightnessMode = Settings.System.getInt(
					mContext.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE);
		} catch (Exception e) {
		}
		return brightnessMode;
	}
	
	/**
	 * 设置当前系统的亮度模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	 * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	 */
	public void setBrightnessMode(int brightnessMode) {
		try {
			Settings.System.putInt(mContext.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessMode);
		} catch (Exception e) {
//			Log.e(TAG, "设置当前屏幕的亮度模式失败：", e);
		}
	}

	/**
	 * 获得当前系统的亮度值： 0~255
	 */
	public int getSysScreenBrightness() {
		int screenBrightness = MAX_BRIGHTNESS;
		try {
			screenBrightness = Settings.System.getInt(
					mContext.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
//			Log.e(TAG, "获得当前系统的亮度值失败：", e);
			e.printStackTrace();
		}
		return screenBrightness;
	}
	
	/**
	 * 设置当前系统的亮度值:0~255
	 */
	public void setSysScreenBrightness(int brightness) {
		try {
			ContentResolver resolver = mContext.getContentResolver();
			Uri uri = Settings.System
					.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
			Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,
					brightness);
			resolver.notifyChange(uri, null); // 实时通知改变
		} catch (Exception e) {
//			Log.e(TAG, "设置当前系统的亮度值失败：", e);
		}
	}

	public void setBrightnessMode(boolean bAuto) {
		int mode = 0;
		if(bAuto) {
			mode = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		}else {
			mode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
		}
		Settings.System.putInt(mContext.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
		ContentResolver resolver = mContext.getContentResolver();
	}
	
	/**
	 * 设置屏幕亮度，这会反映到真实屏幕上
	 * 
	 * @param activity
	 * @param brightness
	 */
	public void setActScreenBrightness(final Activity activity,
			final int brightness) {
		final WindowManager.LayoutParams lp = activity.getWindow()
				.getAttributes();
		lp.screenBrightness = brightness / (float) MAX_BRIGHTNESS;
		activity.getWindow().setAttributes(lp);
		
	}
}
