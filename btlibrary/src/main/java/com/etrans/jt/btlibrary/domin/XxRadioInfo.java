package com.etrans.jt.btlibrary.domin;

import java.util.ArrayList;
import java.util.List;

public class XxRadioInfo {
	private int band ;
	private int fmCurFreq ;
	private int amCurFreq ;
	private int state ;
	private List<String> fmFavoChannels;
	private int fmFavoChannelsIndex;
	private int amFavoChannelsIndex;
	private List<String> amFavoChannels;
	private String curChannelName;
	
	public XxRadioInfo() {
		fmFavoChannels = new ArrayList<String>();
		amFavoChannels = new ArrayList<String>();
	}
	public int getBand() {
		return band;
	}
	public void setBand(int band) {
		this.band = band;
	}
	
	public int getFmCurFreq() {
		return fmCurFreq;
	}
	public void setFmCurFreq(int fmCurFreq) {
		this.fmCurFreq = fmCurFreq;
	}
	public int getAmCurFreq() {
		return amCurFreq;
	}
	public void setAmCurFreq(int amCurFreq) {
		this.amCurFreq = amCurFreq;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public List<String> getFmFavoChannels() {
		return fmFavoChannels;
	}
	public void setFmFavoChannels(List<String> fmFavoChannels) {
		this.fmFavoChannels = fmFavoChannels;
	}
	public int getFmFavoChannelsIndex() {
		return fmFavoChannelsIndex;
	}
	public void setFmFavoChannelsIndex(int fmFavoChannelsIndex) {
		this.fmFavoChannelsIndex = fmFavoChannelsIndex;
	}
	public int getAmFavoChannelsIndex() {
		return amFavoChannelsIndex;
	}
	public void setAmFavoChannelsIndex(int amFavoChannelsIndex) {
		this.amFavoChannelsIndex = amFavoChannelsIndex;
	}
	public List<String> getAmFavoChannels() {
		return amFavoChannels;
	}
	public void setAmFavoChannels(List<String> amFavoChannels) {
		this.amFavoChannels = amFavoChannels;
	}
	public String getCurChannelName() {
		return curChannelName;
	}
	public void setCurChannelName(String curChannelName) {
		this.curChannelName = curChannelName;
	}
}
