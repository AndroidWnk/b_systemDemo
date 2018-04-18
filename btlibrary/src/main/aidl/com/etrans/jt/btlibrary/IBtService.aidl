// IBtAidlInterface.aidl
package com.etrans.jt.btlibrary;

// Declare any non-default types here with import statements

interface IBtService {
      void init();

      boolean isBtPlaying();

      void play();

      void pause();

      void stop();
}
