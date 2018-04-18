package com.etrans.jt.bluetooth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.etrans.jt.bluetooth.BTApplication;
import com.etrans.jt.bluetooth.event.BaseEvent;
import com.etrans.jt.bluetooth.view.PhoneActivity;
import com.etrans.jt.btlibrary.module.BluetoothMusicModule;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/4/6.
 */

public class TxzReceiver extends BroadcastReceiver {

    private final static String TAG = "btpresskeyLog";
    public static boolean isAccOpen = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", intent.getAction());
        String action = intent.getAction();
        if ("com.txznet.txz.record.dismiss".equals(action)) {
            EventBus.getDefault().post(new BaseEvent(101));
//            Intent intent1 = new Intent(context, PhoneActivity.class);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent1);
        } else if ("com.txznet.txz.record.show".equals(action)) {
//            EventBus.getDefault().post(new BaseEvent(102));
        } else if (intent.getAction().equals("android.jt.media.control")) {
            //改到中控
            int state = intent.getIntExtra("mediaAction", -1);
            Log.e(TAG, "state==" + state);
            if (!isAccOpen && state == 1) {
                BTApplication.isBackingUp = true;
                isAccOpen = true;
                if (BluetoothMusicModule.getInstance().isBluetoothMusicPlaying()) {
                    BTApplication.isPlaying = true;
                    BluetoothMusicModule.getInstance().play();
                }
                Log.e(TAG, "isPlaying1:" + BluetoothMusicModule.getInstance().isBluetoothMusicPlaying());
            } else if (isAccOpen && state == 2) {
                isAccOpen = false;
                BTApplication.isBackingUp = false;
                Log.e(TAG, "isPlaying2:" + BTApplication.isPlaying);
                if (BTApplication.isPlaying) {
                    BluetoothMusicModule.getInstance().play();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!BluetoothMusicModule.getInstance().isBluetoothMusicPlaying()) {
                                BluetoothMusicModule.getInstance().play();
                            }
                        }
                    }, 1000);
                    BluetoothMusicModule.getInstance().play();
                    BTApplication.isPlaying = false;
                }
            }
        }
    }
}
