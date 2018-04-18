package com.etrans.jt.btlibrary.utils;


import android.content.Context;
//import android.vehicle.radio.RadioListener;
//import android.vehicle.radio.RadioManager;
//import android.vehicle.radio.RadioParam;

//public class XxRadioUtil implements XxIRadioUtil{
public class XxRadioUtil {
//	private static RadioManager radioManager;
	
//	private RadioListener listener;
	private boolean dealing ;

	public XxRadioUtil(Context context) throws Error{
		init(context);
	}

	public void init(Context context) throws Error{
//		radioManager = (RadioManager) context
//				.getSystemService(Context.RADIO_SERVICE);
//		RadioParam radioParam = new RadioParam();
//		radioParam.fm_max_freq = 108000;
//		radioParam.fm_min_freq = 87500;
//		radioParam.fm_seek_step = 100;
//		radioParam.am_max_freq = 1620;
//		radioParam.am_min_freq = 522;
//		radioParam.am_seek_step = 9;
//		setParam(radioParam);
	}
	
//	public void setParam(RadioParam radioParam) {
//		radioManager.setParam(radioParam);
//	}

	public void serviceRadioOn(int band, int curFreq) {
//		XxLogUtil.getInstance().log("RadioManService", "serviceRadioOn");
		if(!dealing) {
			dealing = true;
//			radioManager.radioOn(band, curFreq);
		}
	}

	public void serviceRadioOff() {
//		XxLogUtil.getInstance().log("RadioManService", "serviceRadioOff");
		if(!dealing) {
			dealing = true;
//			radioManager.radioOff();
		}
	}

	public void setCurrentFerq(int freq) {

//		if(radioManager.getFreq() == freq) {
//			return ;
//		}

		if(!dealing) {
			dealing = true;
//			radioManager.setFreq(freq);
		}
	}

	public void setSearchType(int stateType) {


		if(!dealing && stateType != 5) {
			dealing = true;
//			radioManager.setSearch(stateType - 2);
		}else {
//			radioManager.setSearch(stateType - 2);
		}
	}

//	public void setRadioListener(RadioListener listener) {
//		this.listener = listener;
//		radioManager.addRadioListener(listener);
//	}

//	public void removeRadioListener(RadioListener listener) {
//		radioManager.removeRadioListener(listener);
//	}

//	public int getRadioState() {
//		return radioManager.getState();
//	}
	
//	public int getFreq(){
//		return radioManager.getFreq();
//	}
	
//	public int getBand() {
//		return radioManager.getBand();
//	}
	
//	public void setSensitivity(int sensitivity) {
//		radioManager.setSensitivity(sensitivity);
//	}

//	@Override
//	public void setSwitch(int flag) {
//		radioManager.setSwitch(flag);
//	}

	public void setDealing(boolean dealing) {
		this.dealing = dealing;
	}
}
