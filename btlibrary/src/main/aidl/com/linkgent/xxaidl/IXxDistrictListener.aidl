// IXxNaviDistrictListener.aidl
package com.linkgent.xxaidl;
import com.linkgent.xxaidl.XxDistrict;
// Declare any non-default types here with import statements

interface IXxDistrictListener {

    /**
    * 行政区划改变
    */
    void onDistrictChange(in XxDistrict district);
}
