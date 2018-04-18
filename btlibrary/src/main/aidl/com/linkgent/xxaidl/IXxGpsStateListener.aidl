// IXxNaviGpsEableListener.aidl
package com.linkgent.xxaidl;

// Declare any non-default types here with import statements

interface IXxGpsStateListener {

    /**
    * GPS状态
    * @param state {@link STATE_OUT_OF_SERVICE}
    * {@link STATE_TEMPORARILY_UNAVAILABLE} {@link STATE_AVAILABLE}
    *{@link STATE_GPS_DISABLE}
    *{@link STATE_GPS_ENABLE}{@link STATE_NET_AVAILABLE}
    */
    void onState(int state);


}
