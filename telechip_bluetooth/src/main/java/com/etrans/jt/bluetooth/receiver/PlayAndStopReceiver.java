package com.etrans.jt.bluetooth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.etrans.jt.btlibrary.module.BluetoothMusicModule;

/**
 * Created by Administrator on 2017/7/18.
 */

public class PlayAndStopReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.foton.btmusic.stop")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BluetoothMusicModule.getInstance().musicPause();
                }
            },1000);
        } else if (intent.getAction().equals("com.foton.btmusic.goon")) {
            BluetoothMusicModule.getInstance().play();
        }
    }
}
