// IPhoneStateListener.aidl
package com.linkgent.xxaidl;

// Declare any non-default types here with import statements
/**
* 监听电话和蓝牙电话状态
*/
interface IXxPhoneStateListener {

   //module 只有蓝牙电话或本地电话
   void onState(int module,int state);

}
