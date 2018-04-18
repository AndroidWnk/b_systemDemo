/*******************************************************************************
 * Copyright (C) 2013 Broadcom Corporation
 * <p/>
 * This program is the proprietary software of Broadcom Corporation and/or its
 * licensors, and may only be used, duplicated, modified or distributed
 * pursuant to the terms and conditions of a separate, written license
 * agreement executed between you and Broadcom (an "Authorized License").
 * Except as set forth in an Authorized License, Broadcom grants no license
 * (express or implied), right to use, or waiver of any kind with respect to
 * the Software, and Broadcom expressly reserves all rights in and to the
 * Software and all intellectual property rights therein.
 * IF YOU HAVE NO AUTHORIZED LICENSE, THEN YOU HAVE NO RIGHT TO USE THIS
 * SOFTWARE IN ANY WAY, AND SHOULD IMMEDIATELY NOTIFY BROADCOM AND DISCONTINUE
 * ALL USE OF THE SOFTWARE.
 * <p/>
 * Except as expressly set forth in the Authorized License,
 * <p/>
 * 1.     This program, including its structure, sequence and organization,
 * constitutes the valuable trade secrets of Broadcom, and you shall
 * use all reasonable efforts to protect the confidentiality thereof,
 * and to use this information only in connection with your use of
 * Broadcom integrated circuit products.
 * <p/>
 * 2.     TO THE MAXIMUM EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED
 * "AS IS" AND WITH ALL FAULTS AND BROADCOM MAKES NO PROMISES,
 * REPRESENTATIONS OR WARRANTIES, EITHER EXPRESS, IMPLIED, STATUTORY,
 * OR OTHERWISE, WITH RESPECT TO THE SOFTWARE.  BROADCOM SPECIFICALLY
 * DISCLAIMS ANY AND ALL IMPLIED WARRANTIES OF TITLE, MERCHANTABILITY,
 * NONINFRINGEMENT, FITNESS FOR A PARTICULAR PURPOSE, LACK OF VIRUSES,
 * ACCURACY OR COMPLETENESS, QUIET ENJOYMENT, QUIET POSSESSION OR
 * CORRESPONDENCE TO DESCRIPTION. YOU ASSUME THE ENTIRE RISK ARISING OUT
 * OF USE OR PERFORMANCE OF THE SOFTWARE.
 * <p/>
 * 3.     TO THE MAXIMUM EXTENT PERMITTED BY LAW, IN NO EVENT SHALL BROADCOM OR
 * ITS LICENSORS BE LIABLE FOR
 * (i)   CONSEQUENTIAL, INCIDENTAL, SPECIAL, INDIRECT, OR EXEMPLARY
 * DAMAGES WHATSOEVER ARISING OUT OF OR IN ANY WAY RELATING TO
 * YOUR USE OF OR INABILITY TO USE THE SOFTWARE EVEN IF BROADCOM
 * HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES; OR
 * (ii)  ANY AMOUNT IN EXCESS OF THE AMOUNT ACTUALLY PAID FOR THE
 * SOFTWARE ITSELF OR U.S. $1, WHICHEVER IS GREATER. THESE
 * LIMITATIONS SHALL APPLY NOTWITHSTANDING ANY FAILURE OF
 * ESSENTIAL PURPOSE OF ANY LIMITED REMEDY.
 *******************************************************************************/

package com.etrans.jt.bluetooth.receiver;

import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.broadcom.bt.hfdevice.BluetoothHfDevice;
import com.etrans.jt.bluetooth.BTApplication;
import com.etrans.jt.btlibrary.module.BluetoothModule;
import com.etrans.jt.btlibrary.module.BluetoothMusicModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;


public class BRCMHfDeviceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothReceiver";

    private static boolean isCallDisconnected = true;
    private String mDeviceName;
    private String mDeviceAddress;
    SharedPreferences.Editor editor;
    NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context content, Intent intent) {
        Log.d(TAG, "onReceive()" + intent.getAction());
        String action = intent.getAction();

        Intent in = null;
        int state;

        Bundle bundle = intent.getExtras();
        SharedPreferences sharedPref;
        mNotificationManager =
                (NotificationManager) content.getSystemService(content.NOTIFICATION_SERVICE);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


        state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
        if (action.equals(BluetoothHfDevice.ACTION_CONNECTION_STATE_CHANGED)) {
            switch (state) {
                case BluetoothHfDevice.STATE_CONNECTED://连接成功
                    if (device != null) {
                        mDeviceName = device.getName();
                        mDeviceAddress = device.getAddress();
                        BluetoothModule.getInstance().setDeviceName(mDeviceName);
                        BluetoothPhoneModule.getInstance().cleanPhoneBook();
                    }
                    BluetoothPhoneModule.getInstance().initPhoneService();
                    BluetoothMusicModule.getInstance().initMusicService();
                    Log.d(TAG, "onReceive: Hf device connection ..");
                    break;
                case BluetoothHfDevice.STATE_DISCONNECTED://已经断开
                    BluetoothPhoneModule.getInstance().unRegisterBtPhoneReceiver();
                    BluetoothMusicModule.getInstance().unregisterProxy();
                    BTApplication.appInstance.finishActivity();
                    Log.d(TAG, "onReceive: Hf device disconnected ..");
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    Log.d(TAG, "onReceive: Hf device CONNECTING ..");
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    Log.d(TAG, "onReceive: Hf device DISCONNECTING ..");
                    break;
                default:
                    break;
            }

        } else if (action.equals(BluetoothHfDevice.ACTION_CALL_STATE_CHANGED)) {//电话状态
            switch (state) {
                case BluetoothHfDevice.CALL_SETUP_STATE_IDLE: //闲置
                    isCallDisconnected = false;
                    Log.d(TAG, "onReceive: IDLE call received..");
                    break;
                case BluetoothHfDevice.CALL_SETUP_STATE_INCOMING://打进来的电话
                    Log.d(TAG, "onReceive: INCOMING call received..");
                    if (BluetoothPhoneModule.getInstance().isLocalCall()) {
                        BluetoothPhoneModule.getInstance().hangup();
                        return;
                    }
                    isCallDisconnected = true;
                    startActivity(content, BluetoothHfDevice.CALL_SETUP_STATE_INCOMING);
                    break;
                case BluetoothHfDevice.CALL_SETUP_STATE_WAITING://当有一个电话正在通话中 进来第二个电话
                    Log.d(TAG, "onReceive: WAITING received..");
                    break;
                case BluetoothHfDevice.CALL_SETUP_STATE_ALERTING://接听
                    Log.d(TAG, "onReceive: ALERTING received..");
                    break;
                case BluetoothHfDevice.CALL_SETUP_STATE_DIALING://打电话中
                    Log.d(TAG, "onReceive: DIALING received..");
                    if (BluetoothPhoneModule.getInstance().isLocalCall()) {
                        BluetoothPhoneModule.getInstance().hangup();
                        return;
                    }
                    BluetoothPhoneModule.getInstance().initPhoneService();
                    startActivity(content, BluetoothHfDevice.CALL_SETUP_STATE_DIALING);
                    break;
                case BluetoothHfDevice.CALL_STATE_HELD://三方通话 保持通话
                    Log.d(TAG, "onReceive: HELD received..");
                    break;
                default:
                    break;
            }
        } else if (action.equals(BluetoothHfDevice.ACTION_WBS_STATE_CHANGED)) {
            Log.d(TAG, "WBS config:" + state);
        } else if (action.equals(BluetoothHfDevice.ACTION_RING_EVENT)) {
            Log.d(TAG, "onReceive: RING event ..");
        } else if (action.equals(BluetoothHfDevice.ACTION_INBANDRING_EVENT)) { /* Telechips' Remark */
            Log.d(TAG, "onReceive: INBAND RING event .. state = " + state);
        } else if (action.equals(BluetoothHfDevice.ACTION_AUDIO_STATE_CHANGED)) {//切换声道时 切换图片
            //0 手机   2 蓝牙设备  默认拨打电话时 是蓝牙设备输出声音
            int audioState = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
            Log.d(TAG, "onReceive: ACTION_AUDIO_STATE_CHANGED event audioState= " + audioState);

            if (audioState != BluetoothHfDevice.STATE_AUDIO_CONNECTED)
                return;
            sharedPref = PreferenceManager.getDefaultSharedPreferences(content);
//            if (sharedPref.getBoolean(BRCMHfDeviceConstants.HF_DEVICE_IS_HSP_CONNECTION, false)) {
//                startActivity(content);
//            }
        }
    }

    /**
     * 启动activity
     *
     * @param content
     */
    private void startActivity(Context content, int state) {
       /* Intent in = new Intent();
        in.setClass(content, PhoneCallExActivity.class);
        in.putExtra(BRCMHfDeviceConstants.HF_DEVICE_NAME, mDeviceName);
        in.putExtra(BRCMHfDeviceConstants.HF_DEVICE_ADDRESS, mDeviceAddress);
        in.putExtra("callState", state);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG, "onReceive: Broadcast Reciever started an activity..");
        content.startActivity(in);
        Log.d(TAG, "startActivity() called with: content = [" + content + "], state = [" + state + "]");*/
    }


}
