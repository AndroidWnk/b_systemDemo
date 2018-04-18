package com.etrans.jt.btlibrary.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.broadcom.bt.avrcp.BluetoothAvrcpController;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;
import com.etrans.jt.btlibrary.receiver.BluetoothStateReceiver;


/**
 * 单元名称:BluetoothService.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class BluetoothService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initModule();
        registerReceiver();
    }

    //初始化module
    private void initModule() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 注册监听
     */
    private void registerReceiver() {
        registerBluetoothStateReceiver();
        registerBluetoothMusicReceiver();
        BluetoothPhoneModule.getInstance().initPhoneService();
    }

    /**
     * 注册蓝牙音乐监听
     */
    private void registerBluetoothMusicReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAvrcpController.ACTION_CONNECTION_STATE_CHANGED);
//        registerReceiver(new AvrcpBroadcastReceiver(), filter);
//        Toast.makeText(BluetoothService.this, "蓝牙音乐监听", Toast.LENGTH_LONG).show();
    }

    /**
     * 注册蓝牙状态Change监听
     */
    private void registerBluetoothStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(new BluetoothStateReceiver(), filter);
//        Toast.makeText(BluetoothService.this, "蓝牙状态监听", Toast.LENGTH_LONG).show();
    }
}
