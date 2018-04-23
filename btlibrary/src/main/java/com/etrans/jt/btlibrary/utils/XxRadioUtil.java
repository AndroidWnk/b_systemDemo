package com.etrans.jt.btlibrary.utils;


import android.content.Context;
//import android.vehicle.radio.RadioListener;//++android.jar增加部分
//import android.vehicle.radio.RadioManager;//++android.jar增加部分
//import android.vehicle.radio.RadioParam;//++android.jar增加部分

//public class XxRadioUtil implements XxIRadioUtil{
public class XxRadioUtil {
//	private static RadioManager radioManager;//++android.jar增加部分 无需用到XxRadioUtil类

//	private RadioListener listener;//++android.jar增加部分
	private boolean dealing ;

	public XxRadioUtil(Context context) throws Error{
		init(context);
	}

	public void init(Context context) throws Error{
//		radioManager = (RadioManager) context.getSystemService(Context.RADIO_SERVICE);//++android.jar增加部分
//		RadioParam radioParam = new RadioParam();//++android.jar增加部分
//		radioParam.fm_max_freq = 108000;
//		radioParam.fm_min_freq = 87500;
//		radioParam.fm_seek_step = 100;
//		radioParam.am_max_freq = 1620;
//		radioParam.am_min_freq = 522;
//		radioParam.am_seek_step = 9;
//		setParam(radioParam);
	}

//	public void setParam(RadioParam radioParam) { //++android.jar增加部分
//		radioManager.setParam(radioParam);
//	}

	public void serviceRadioOn(int band, int curFreq) {
//		XxLogUtil.getInstance().log("RadioManService", "serviceRadioOn");
		if(!dealing) {
			dealing = true;
//			radioManager.radioOn(band, curFreq); //++android.jar增加部分
		}
	}

	public void serviceRadioOff() {
//		XxLogUtil.getInstance().log("RadioManService", "serviceRadioOff");
		if(!dealing) {
			dealing = true;
//			radioManager.radioOff(); //++android.jar增加部分
		}
	}

	public void setCurrentFerq(int freq) {

//		if(radioManager.getFreq() == freq) { //++android.jar增加部分
//			return ;
//		}

		if(!dealing) {
			dealing = true;
//			radioManager.setFreq(freq); //++android.jar增加部分
		}
	}

	public void setSearchType(int stateType) {


		if(!dealing && stateType != 5) {
			dealing = true;
//			radioManager.setSearch(stateType - 2); //++android.jar增加部分
		}else {
//			radioManager.setSearch(stateType - 2); //++android.jar增加部分
		}
	}

//	public void setRadioListener(RadioListener listener) { //++android.jar增加部分
//		this.listener = listener;
//		radioManager.addRadioListener(listener);
//	}

//	public void removeRadioListener(RadioListener listener) { //++android.jar增加部分
//		radioManager.removeRadioListener(listener);
//	}

//	public int getRadioState() {
//		return radioManager.getState(); //++android.jar增加部分
//	}

//	public int getFreq(){
//		return radioManager.getFreq(); //++android.jar增加部分
//	}

//	public int getBand() {
//		return radioManager.getBand(); //++android.jar增加部分
//	}

	public void setSensitivity(int sensitivity) { //++android.jar增加部分
//		radioManager.setSensitivity(sensitivity);
	}

//	@Override
//	public void setSwitch(int flag) {
//		radioManager.setSwitch(flag); //++android.jar增加部分
//	}

	public void setDealing(boolean dealing) {
		this.dealing = dealing;
	}
}
