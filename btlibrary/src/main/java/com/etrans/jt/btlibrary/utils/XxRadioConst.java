package com.etrans.jt.btlibrary.utils;

public class XxRadioConst {
	public class Switch {
//		public static final int OFF = RadioManager.SWITCH_OFF;
//		public static final int ON = RadioManager.SWITCH_ON;
	}
	
	public class Band {
		public static final int FM = 0;
		public static final int AM = 1;
	}
	
	public class State {
		public static final int OFF = 0;
		public static final int ON = 1;
		public static final int DOWN = 2;
		public static final int UP = 3;
		public static final int AUTO = 4;
		public static final int STOP = 5;
	}
	
	public class RadioStandard {
		public static final int FMMAXFREQ = 108000; // fm 的搜索做大频率
		public static final int FMMINFREQ = 87500; // fm最小频率
		public static final int AMMAXFREQ = 1620; // fm 的搜索做大频率
		public static final int AMMINFREQ = 531; // fm最小频率
		public static final int FMSTEP = 100;	//FM自动微调单位
		public static final int AMSTEP = 9;		//AM自动微调单位
		public static final int TRIMMINGSTEP = 5; //微调单位
		public static final int DRF_FM_FREQ  = 9150 ;
	}
	
	public class RadioVoiceOper {
		public static final int NONE = 0;
		public static final int ModifyBAND = 1;
		public static final int ModifyFREQ = 2;
		public static final int NoChannelName = 3;
	}
}
