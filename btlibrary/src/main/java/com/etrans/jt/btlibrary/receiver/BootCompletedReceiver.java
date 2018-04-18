package com.etrans.jt.btlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.etrans.jt.btlibrary.utils.Constants;


/**
 * 单元名称:BootCompletedReceiver.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:监听开机广播 绑定Service
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = BootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Constants.Action.ACTION_BOOT_COMPLETED)) {
            //接收到开机广播
            intent.setAction(Constants.ACTION_BLUETOOTH_SERVICE);
//            context.startService(intent.setClass(context, BluetoothService.class));
        }
    }


}
