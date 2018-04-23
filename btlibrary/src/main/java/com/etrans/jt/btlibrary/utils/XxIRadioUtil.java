package com.etrans.jt.btlibrary.utils;

//import android.vehicle.radio.RadioListener;//++android.jar增加部分
//import android.vehicle.radio.RadioParam;//++android.jar增加部分


public interface XxIRadioUtil {

//	public void setParam(RadioParam radioParam);//++android.jar增加部分 无需用到XxIRadioUtil继承接口

	public void serviceRadioOn(int band, int curFreq) ;

	public void serviceRadioOff() ;

	public void setCurrentFerq(int freq) ;

	public void setSearchType(int stateType) ;

//	public void setRadioListener(RadioListener listener) ;//++android.jar增加部分

//	public void removeRadioListener(RadioListener listener);//++android.jar增加部分

	public int getRadioState() ;

	public int getFreq();

	public int getBand();

	public void setSensitivity(int sensitivity);

	public void setSwitch(int flag);

	public void setDealing(boolean b);
}
