package com.etrans.jt.btlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.etrans.jt.btlibrary.module.BluetoothMusicModule;
import com.etrans.jt.btlibrary.utils.XxActivityTool;


public class RemoteControlReceiver extends BroadcastReceiver {

    /*private boolean isMusicPlaying() {
        int state = XxMediaManager.getInstance().getPlayerState(XxMediaManager.PLAYER_LOCAL);
        int state_n = XxMediaManager.getInstance().getPlayerState(XxMediaManager.PLAYER_NET);

        return state == XxMediaManager.MUSIC_PLAY || state_n == XxMediaManager.MUSIC_PLAY || state_n == XxMediaManager.MUSIC_BUFFERING;
    }*/

    /*private boolean isRadioOn() {
        return XxRadioManager.getInstance().getRadioInfo().getState() != XxRadioConst.State.OFF;
    }*/

    private boolean isBtearmusicPlaying() {
        //int state = XxBluetoothPhoneManager.getInstance().getBluetoothDevice().getMediaState();
        return BluetoothMusicModule
                .getInstance().isBluetoothMusicPlaying();
    }

    public RemoteControlReceiver() {
        // TODO Auto-generated constructor stub
    }

    public void btearMusicPlay(Context context, int state) {
        switch (state) {
            case 0:
                BluetoothMusicModule.getInstance().play();
                break;
            case 1:
                BluetoothMusicModule.getInstance().pause();
                break;
            case 2:
                BluetoothMusicModule.getInstance().backward();
                break;
            case 3:
                BluetoothMusicModule.getInstance().forward();
                break;
        }
    }

    public void musicPlay(Context context, int state) {
        BluetoothMusicModule.getInstance().pause();
        /*if(state == 0) {
            Intent ac = new Intent(context, XxMusicActivity.class);
            ac.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ac);
        }
        switch (state) {
            case 0:
                XxMediaManager.getInstance().play();
                break;
            case 1:
                XxMediaManager.getInstance().pause(true);
                break;
            case 2:
                XxMediaManager.getInstance().previous();
                break;
            case 3:
                XxMediaManager.getInstance().next();
                break;
        }*/
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RemoteControlReceiver", "onReceive");
        // 获得Action
        String intentAction = intent.getAction();

        // 获得KeyEvent对象
        KeyEvent keyEvent = (KeyEvent) intent
                .getParcelableExtra(Intent.EXTRA_KEY_EVENT);

        // 按下 / 松开 按钮
        int keyAction = keyEvent.getAction();

        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)
                && (KeyEvent.ACTION_DOWN == keyAction)) {
            // 获得按键字节码
            int keyCode = keyEvent.getKeyCode();

            // 获得事件的时间
            // long downtime = keyEvent.getEventTime();

            // 获取按键码 keyCode
//          StringBuilder sb = new StringBuilder();
//          // 这些都是可能的按键码 ， 打印出来用户按下的键
//          if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
//              sb.append("KEYCODE_MEDIA_NEXT");
//          }
            // 说明：当我们按下MEDIA_BUTTON中间按钮时，实际出发的是 KEYCODE_HEADSETHOOK 而不是
            // KEYCODE_MEDIA_PLAY_PAUSE
            if (KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode) {
                BluetoothMusicModule.getInstance().pause();
            }
            if (keyEvent.KEYCODE_MEDIA_PLAY == keyCode) {
                BluetoothMusicModule.getInstance().play();
            }

            if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode) {
//              sb.append("KEYCODE_MEDIA_PLAY_PAUSE");
            }
            if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
//              sb.append("KEYCODE_HEADSETHOOK");
            }
            if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
//              sb.append("KEYCODE_MEDIA_PREVIOUS");
            }
            if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode) {
//              sb.append("KEYCODE_MEDIA_STOP");
            }
            if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {//下一首
//              sb.append("KEYCODE_MEDIA_STOP");
                BluetoothMusicModule.getInstance().forward();
            }
            if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {//上一首
//              sb.append("KEYCODE_MEDIA_STOP");
                BluetoothMusicModule.getInstance().backward();
            }
        } else if (KeyEvent.ACTION_UP == keyAction) {
            Log.i("chengjie", "aaa");
        }
    }


}
