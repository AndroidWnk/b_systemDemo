package com.linkgent.remoute.api;

/**
 * Created by Administrator on 2017/3/2.
 */

public class XxLocationConfig {
    public static final String LOCATION_SERVICE = "com.linkgent.remote.service.XXLOCATIONSERVICE";
    public static final String ACTION_LOCATION = "com.linkgent.remote.action.XXLOCATION";
    public static final String EXTRA_LOCATIONSTATE = "state";
    public static final int LOCATIONSERVICE_START = 0;
    public static final int LOCATIONSERVICE_STOP = 1;
    public static final int GPSTYPE_WGS84 = 0;
    public static final int GPSTYPE_GCJ02 = 1;
    public static final int STATE_OUT_OF_SERVICE = 0;
    public static final int STATE_TEMPORARILY_UNAVAILABLE = 1;
    public static final int STATE_AVAILABLE = 2;
    public static final int STATE_GPS_DISABLE = 3;
    public static final int STATE_GPS_ENABLE = 4;

    public XxLocationConfig() {
    }
}
