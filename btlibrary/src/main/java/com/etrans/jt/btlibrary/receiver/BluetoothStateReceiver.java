package com.etrans.jt.btlibrary.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.etrans.jt.btlibrary.manager.XxOtherSettingManager;
import com.etrans.jt.btlibrary.module.BluetoothModule;

/**
 * 单元名称:BluetoothStateReceiver.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:蓝牙状态change监听
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class BluetoothStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", intent.getAction());
        //蓝牙状态
        if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.d("aaa", "STATE_OFF 手机蓝牙关闭");
                    BluetoothModule.getInstance().onClose();
                    XxOtherSettingManager.getInstance().BtClose();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    BluetoothModule.getInstance().onClosing();
                    Log.d("aaa", "STATE_TURNING_OFF 手机蓝牙正在关闭");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d("aaa", "STATE_ON 手机蓝牙开启");
                    BluetoothModule.getInstance().onOpen();
                    XxOtherSettingManager.getInstance().BtOpen();
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    /*if (!bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.enable();//异步的，不会等待结果，直接返回。
                    } else {
                        bluetoothAdapter.startDiscovery();
                    }*/
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
                    BluetoothModule.getInstance().onOpening();
                    break;
                default:
                    break;
            }
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {//连接
            Log.d("aaa", "CONNECT 连接");
            BluetoothModule.getInstance().onConnect(intent);
//            BluetoothPhoneModule.getInstance().registerBtPhoneReceiver();
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {//断开连接
            Log.d("aaa", "CONNECT 断开连接");
            BluetoothModule.getInstance().onDisConnect();
//            BluetoothPhoneModule.getInstance().unRegisterBtPhoneReceiver();
        }
    }
}
