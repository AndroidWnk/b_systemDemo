package com.etrans.jt.bluetooth.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.etrans.jt.bluetooth.BTApplication;
import com.etrans.jt.bluetooth.event.BaseEvent;

import org.greenrobot.eventbus.EventBus;

public class XxBluetoothReceiver extends BroadcastReceiver {
	final String TAG = "XXBluetooth";

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent == null)
			return;
		String action = intent.getAction();

		//蓝牙状态
		if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
					BluetoothAdapter.ERROR);
			switch (state) {
				case BluetoothAdapter.STATE_OFF:
					Log.d("aaa", "STATE_OFF 手机蓝牙关闭");
					EventBus.getDefault().post(new BaseEvent(11));
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
//					XxOtherSettingManager.getInstance().onClosing();
					Log.d("aaa", "STATE_TURNING_OFF 手机蓝牙正在关闭");
					break;
				case BluetoothAdapter.STATE_ON:
					Log.d("aaa", "STATE_ON 手机蓝牙开启");
					EventBus.getDefault().post(new BaseEvent(10));
					BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					/*if (!bluetoothAdapter.isEnabled()) {
						bluetoothAdapter.enable();//异步的，不会等待结果，直接返回。
					} else {
						bluetoothAdapter.startDiscovery();
					}*/
					break;
				case BluetoothAdapter.STATE_TURNING_ON:
					Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
//					XxOtherSettingManager.getInstance().onOpening();
					break;
				default:
					break;
			}
		} else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {//连接
			Log.d("aaa", "CONNECT 连接");
			EventBus.getDefault().post(new BaseEvent(10));
//			XxOtherSettingManager.getInstance().onConnect(intent);
		} else if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {//断开连接
			Log.d("aaa", "CONNECT 断开连接");
			EventBus.getDefault().post(new BaseEvent(11));
//			XxOtherSettingManager.getInstance().onDisConnect();
		}
	}

}
