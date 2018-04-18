package com.etrans.jt.bluetooth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.etrans.jt.btlibrary.module.BluetoothMusicModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;


public class VehicleReceiver extends BroadcastReceiver {
    private static final String TAG = VehicleReceiver.class.getSimpleName();

    private static final String ACTION_BT_MUSIC_CLOSE = "com.link.xxlauncher.BT_MUSIC_CLOSE";

    //android.vehicle.voice.turn 音源切换
    //android.vehicle.voice.mute 静音
    //android.vehicle.seek.up  上一曲
    //android.vehicle.seek.down 下一曲
    //android.vehicle.telephone.answer 电话接听

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        Log.e("seek",action);
        if (action == null) {
            return;
        }
        if (action.equals("foton.vehicle.seek.down")) {
            if (BluetoothPhoneModule.getInstance().isLocalCall()) {
                abort();
                return;
            }
            if (BluetoothPhoneModule.getInstance().isBtCall()) {
                abort();
                return;
            }
            if (BluetoothMusicModule.getInstance().isBluetoothMusicPlaying()) {
                abort();
                BluetoothMusicModule.getInstance().forward();
            }

        } else if (action.equals("foton.vehicle.seek.up")) {
            if (BluetoothPhoneModule.getInstance().isLocalCall()) {
                abort();
                return;
            }
            if (BluetoothPhoneModule.getInstance().isBtCall()) {
                abort();
                return;
            }
            if (BluetoothMusicModule.getInstance().isBluetoothMusicPlaying()) {
                abort();
                BluetoothMusicModule.getInstance().backward();
            }

        } else if (action.equals("com.android.vehicle.CALL")) {
            //接听
            BluetoothPhoneModule.getInstance().answer();
        } else if (action.equals("com.android.vehicle.ENDCALL")) {
            //接听电话
            BluetoothPhoneModule.getInstance().hangup();
        }
        else if (action.equals(ACTION_BT_MUSIC_CLOSE)) {
            Log.i(TAG, "action: " + ACTION_BT_MUSIC_CLOSE);
            BluetoothMusicModule.getInstance().onBluetoothMusicClose();
        }

    }

	/*public void musicPlay(Context context) {
        Intent ac = new Intent(context, XxMusicActivity.class);
		ac.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(ac);
		int state = XxMediaManager.getInstance().getPlayState();
		if (state == XxMediaManager.MUSIC_STOP || state == -1) {
			XxMediaManager.getInstance().play();
		} else {
			XxMediaManager.getInstance().resume();
		}
	}*/

	/*public void btearMusicPlay(Context context) {
		Intent ac = new Intent(context, XxPhoneActivity.class);
		ac.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ac.putExtra(XxPhoneActivity.EXTRA_STARTMUSIC, true);
		context.startActivity(ac);
		XxBluetoothPhoneManager.getInstance().getBluetoothDevice().setMediaPlay();
	}*/

    private void txzMusicNext(Context context) {
        Intent intent = new Intent("com.txznet.music.operator");
        intent.putExtra(OPERATOR, NEXT);
        context.sendBroadcast(intent);
    }

    private void txzMusicPrev(Context context) {
        Intent intent = new Intent("com.txznet.music.operator");
        intent.putExtra(OPERATOR, PREV);
        context.sendBroadcast(intent);
    }


    private void abort() {
        try {
            abortBroadcast();
        } catch (Exception e) {

        }
    }

    public static final String OPERATOR = "operator";
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String NEXT = "NEXT";
    public static final String PREV = "PREV";
}
