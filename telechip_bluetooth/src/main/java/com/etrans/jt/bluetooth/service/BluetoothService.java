package com.etrans.jt.bluetooth.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.etrans.jt.btlibrary.IBtService;


/**
 * 单元名称:BluetoothService.java
 * Created by fuxiaolei on 2016/11/28.
 * 说明:
 * Last Change by fuxiaolei on 2016/11/28.
 */
public class BluetoothService extends Service {
    private IBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class MyBinder extends IBtService.Stub {

        @Override
        public void init() {

        }

        @Override
        public boolean isBtPlaying() throws RemoteException {
            return false;
        }

        @Override
        public void play() throws RemoteException {

        }

        @Override
        public void pause() throws RemoteException {

        }

        @Override
        public void stop() throws RemoteException {

        }
    }

}
