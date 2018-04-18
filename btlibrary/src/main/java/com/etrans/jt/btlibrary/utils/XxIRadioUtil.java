package com.etrans.jt.btlibrary.utils;

import android.vehicle.radio.RadioListener;
import android.vehicle.radio.RadioParam;


public interface XxIRadioUtil {
	
	public void setParam(RadioParam radioParam);
	
	public void serviceRadioOn(int band, int curFreq) ;
	
	public void serviceRadioOff() ;

	public void setCurrentFerq(int freq) ;

	public void setSearchType(int stateType) ;

	public void setRadioListener(RadioListener listener) ;

	public void removeRadioListener(RadioListener listener);

	public int getRadioState() ;
	
	public int getFreq();
	
	public int getBand();
	
	public void setSensitivity(int sensitivity);
	
	public void setSwitch(int flag);

	public void setDealing(boolean b);
}
