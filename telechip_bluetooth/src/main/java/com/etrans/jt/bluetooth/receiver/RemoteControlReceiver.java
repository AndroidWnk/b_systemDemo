package com.etrans.jt.bluetooth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.etrans.jt.bluetooth.view.PhoneActivity;
import com.etrans.jt.btlibrary.module.BluetoothMusicModule;


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
        if(state == 0){
            Intent ac = new Intent(context, PhoneActivity.class);
            ac.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ac.putExtra(PhoneActivity.EXTRA_START_MUSIC, true);
            context.startActivity(ac);
        }

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
        Log.i("yangtong", "onReceive");
        /*if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode() && KeyEvent.ACTION_UP == event.getAction()) {
                // Handle key press.
                Log.i("yangtong", "onReceive KEYCODE_MEDIA_PLAY");

               *//* if (TXZMusicManager.getInstance().isPlaying()) {
                    return;
                }
                if (isMusicPlaying()) {
                    musicPlay(context,0);
                    return;
                }*//*

                if (isBtearmusicPlaying()) {
                    btearMusicPlay(context,0);
                    return;
                }

                XxBBApplication app = (XxBBApplication) context.getApplicationContext();
                if (app.bShowBtearMusic) {
                    btearMusicPlay(context, 0);
                    return;
                }
                if (app.bShowMusic) {
                    musicPlay(context, 0);
                    return;
                }
            } else if (KeyEvent.KEYCODE_MEDIA_NEXT == event.getKeyCode() && KeyEvent.ACTION_UP == event.getAction()) {
                Log.i("yangtong", "onReceive KEYCODE_MEDIA_NEXT");
                if (TXZMusicManager.getInstance().isPlaying()) {
                   // TXZMusicManager.getInstance().next();
                    return;
                }
                if (isMusicPlaying()) {
                   musicPlay(context,3);
                    return;
                }

                if (isBtearmusicPlaying()) {
                    btearMusicPlay(context,3);
                    return;
                }

                XxBBApplication app = (XxBBApplication) context.getApplicationContext();
                if (app.bShowBtearMusic) {
                    btearMusicPlay(context, 3);
                    return;
                }
                if (app.bShowMusic) {
                    musicPlay(context,3);
                    return;
                }
            } else if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == event.getKeyCode() && KeyEvent.ACTION_UP == event.getAction()) {
                Log.i("yangtong", "onReceive KEYCODE_MEDIA_PREVIOUS");
                if (TXZMusicManager.getInstance().isPlaying()) {
                    //TXZMusicManager.getInstance().prev();
                    return;
                }
                if (isMusicPlaying()) {
                    musicPlay(context,2);
                    return;
                }

                if (isBtearmusicPlaying()) {
                    btearMusicPlay(context,2);
                    return;
                }

                XxBBApplication app = (XxBBApplication) context.getApplicationContext();
                if (app.bShowBtearMusic) {
                    btearMusicPlay(context, 2);
                    return;
                }
                if (app.bShowMusic) {
                    musicPlay(context,2);
                    return;
                }
            } else if (KeyEvent.KEYCODE_MEDIA_PAUSE == event.getKeyCode() && KeyEvent.ACTION_UP == event.getAction()) {
                Log.i("yangtong", "onReceive KEYCODE_MEDIA_PAUSE");
                if (TXZMusicManager.getInstance().isPlaying()) {
                    //TXZMusicManager.getInstance().pause();
                    return;
                }
                if (isMusicPlaying()) {
                    musicPlay(context,1);
                    return;
                }

                if (isBtearmusicPlaying()) {
                    btearMusicPlay(context,1);
                    return;
                }

                XxBBApplication app = (XxBBApplication) context.getApplicationContext();
                if (app.bShowBtearMusic) {
                    btearMusicPlay(context, 1);
                    return;
                }
                if (app.bShowMusic) {
                    musicPlay(context,1);
                    return;
                }
            } else if (KeyEvent.KEYCODE_MEDIA_CLOSE == event.getKeyCode() && KeyEvent.ACTION_UP == event.getAction()) {
                Log.i("yangtong", "onReceive KEYCODE_MEDIA_CLOSE");
            }*/
        }



}
