// IXxAudioFocusChange.aidl
package com.linkgent.xxaidl;
// Declare any non-default types here with import statements

interface IXxAudioFocusChange {

   /**
   *  音频焦点改变
   *
   */
   void onAudioFocusChange(int change);

   /**
   * 只在语音助理中使用 其他模块不会调用
   */
   void onCheck();

}
