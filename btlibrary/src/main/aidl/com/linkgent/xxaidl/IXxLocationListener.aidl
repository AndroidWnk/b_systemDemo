// IXxNaviGpsListener.aidl
package com.linkgent.xxaidl;

// Declare any non-default types here with import statements

interface IXxLocationListener {

    /**
    * GPS位置改变
    * @param gpsType {@link XxGpsConfig.GPSTYPE_WGS84}{@link XxGpsConfig.GPSTYPE_GCJ02}
    *
    */
    void onLocationChange(int gpsType,double lon,double lat,float speed,float bearing,double altitude,long timestamp);


}
