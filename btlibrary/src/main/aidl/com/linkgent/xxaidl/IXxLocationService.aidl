/// IXxGpsManager.aidl
package com.linkgent.xxaidl;
import com.linkgent.xxaidl.IXxGpsStarListener;
import com.linkgent.xxaidl.IXxDistrictListener;
import com.linkgent.xxaidl.XxLocationInfo;
import com.linkgent.xxaidl.XxDistrict;
import com.linkgent.xxaidl.IXxGpsStateListener;
import com.linkgent.xxaidl.IXxLocationListener;

// Declare any non-default types here with import statements

interface IXxLocationService {
    /**
     * 添加星历改变监听器
     */
    void addIXxGpsStarListener(in IXxGpsStarListener listener);
     /**
     * 移除星历改变监听器
     */
    void removeIXxGpsStarListener(in IXxGpsStarListener listener);
     /**
     * 添加行政区划改变监听器
     */
    void addIXxDistrictListener(in IXxDistrictListener listener);
    /**
     * 移除行政区划改变监听器
     */
    void removeIXxDistrictListener(in IXxDistrictListener listener);
    /**
     * 添加GPS改变监听器
     */
    void addIXxLocationListener(in IXxLocationListener listener);
    /**
     * 移除GPS改变监听器
     */
    void removeIXxLocationListener(in IXxLocationListener listener);

    /**
     * 添加GPS状态监听器
     */
    void addIXxGpsStateListener(in IXxGpsStateListener listener);
    /**
     * 移除GPS状态监听器
     */
    void removeIXxGpsStateListener(in IXxGpsStateListener listener);

    /**
    *   获取GPS 当前的状态
    *  @return @param state {@link STATE_OUT_OF_SERVICE}
    *  {@link STATE_TEMPORARILY_UNAVAILABLE} {@link STATE_AVAILABLE}
    *  {@link STATE_GPS_DISABLE}
    *  {@link STATE_GPS_ENABLE}{@link STATE_NET_AVAILABLE}
    */
    int getGpsState();

    /**
    * 获取当前的GPS信息
    */
    XxLocationInfo getLocationInfo();

    /**
    * 获取当前的行政区划信息
    */
    XxDistrict getCurDistrictInfo();

    /**
    *  根据行政区划代码查询行政区划信息
    */
    XxDistrict queryDistritByCode(int code);

    /**
    * 根据GPS点查询行政区划
    * @param gpsType  {@link XxGpsConfig.GPSTYPE_WGS84 }{@link XxGpsConfig.GPSTYPE_GCJ02 }
    */
    XxDistrict queryDistrictByGeo(int gpsType,double lon,double lat);


}
