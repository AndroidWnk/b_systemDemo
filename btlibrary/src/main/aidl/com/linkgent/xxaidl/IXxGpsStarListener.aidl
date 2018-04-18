// IXxNavGpsStarListener.aidl
package com.linkgent.xxaidl;

// Declare any non-default types here with import statements

interface IXxGpsStarListener {
      /**
        * GPS 星历改变
       */
      void onStarChange(int startCount,inout int []arrPrn,inout float []arrSnr,inout float []arrAzimuth,inout float []arrEle);


}
