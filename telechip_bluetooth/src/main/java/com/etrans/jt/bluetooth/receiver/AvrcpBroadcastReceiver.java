/*******************************************************************************
 * Copyright (C) 2013 Broadcom Corporation
 * <p>
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
 * <p>
 * Except as expressly set forth in the Authorized License,
 * <p>
 * 1.     This program, including its structure, sequence and organization,
 * constitutes the valuable trade secrets of Broadcom, and you shall
 * use all reasonable efforts to protect the confidentiality thereof,
 * and to use this information only in connection with your use of
 * Broadcom integrated circuit products.
 * <p>
 * 2.     TO THE MAXIMUM EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED
 * "AS IS" AND WITH ALL FAULTS AND BROADCOM MAKES NO PROMISES,
 * REPRESENTATIONS OR WARRANTIES, EITHER EXPRESS, IMPLIED, STATUTORY,
 * OR OTHERWISE, WITH RESPECT TO THE SOFTWARE.  BROADCOM SPECIFICALLY
 * DISCLAIMS ANY AND ALL IMPLIED WARRANTIES OF TITLE, MERCHANTABILITY,
 * NONINFRINGEMENT, FITNESS FOR A PARTICULAR PURPOSE, LACK OF VIRUSES,
 * ACCURACY OR COMPLETENESS, QUIET ENJOYMENT, QUIET POSSESSION OR
 * CORRESPONDENCE TO DESCRIPTION. YOU ASSUME THE ENTIRE RISK ARISING OUT
 * OF USE OR PERFORMANCE OF THE SOFTWARE.
 * <p>
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

import android.app.Notification;
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

import com.broadcom.bt.avrcp.BluetoothAvrcpController;
import com.etrans.jt.bluetooth.R;
import com.etrans.jt.btlibrary.utils.Constants;


public class AvrcpBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AvrcpBroadcastReceiver";
    private static boolean isCallDisconnected = true;
    private String mDeviceName;
    private String mDeviceAddress;
    SharedPreferences.Editor editor;
    NotificationManager mNotificationManager;
    public static final String AVRCP_TG_NAME = "com.broadcom.bt.app.avrcp.targetname";
    public static final String AVRCP_TG_ADDRESS = "com.broadcom.bt.app.avrcp.targetaddress";
    public static final String AVRCP_CONNECTED = "com.broadcom.bt.app.avrcp.connected";
    public static final int AVRCP_NOTIFICATION_ID = -1000003;   //identifier for the notification


    @Override
    public void onReceive(Context content, Intent intent) {
        Log.d(TAG, "onReceive()" + intent.getAction());
        String action = intent.getAction();

        Intent in;
        int state;

        Bundle bundle = intent.getExtras();
        SharedPreferences sharedPref;
        mNotificationManager =
                (NotificationManager) content.getSystemService(content.NOTIFICATION_SERVICE);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (device != null) {
            mDeviceName = device.getName();
            mDeviceAddress = device.getAddress();
        }
        state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);

        if (action.equals(BluetoothAvrcpController.ACTION_CONNECTION_STATE_CHANGED)) {
            switch (state) {
                case BluetoothProfile.STATE_CONNECTED: {
                    Log.d(TAG, "onReceive: AVRCP Controller connection ..");
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(content);
                    editor = sharedPref.edit();
                    editor.putBoolean(Constants.Action.AVRCP_CONNECTED, true);
                    editor.putString(Constants.Action.AVRCP_TG_NAME, mDeviceName);
                    editor.putString(Constants.Action.AVRCP_TG_ADDRESS, mDeviceAddress);
                    editor.commit();
                    showNotification(R.mipmap.ic_launcher, content);
                }
                break;

                case BluetoothProfile.STATE_DISCONNECTED: {
                    Log.d(TAG, "onReceive: AVRCP Controller disconnected ..");
                    mNotificationManager.cancel(Constants.Action.AVRCP_NOTIFICATION_ID);
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(content);
                    editor = sharedPref.edit();
                    editor.putBoolean(Constants.Action.AVRCP_CONNECTED, false);
                    editor.putString(Constants.Action.AVRCP_TG_NAME, null);
                    editor.putString(Constants.Action.AVRCP_TG_ADDRESS, null);
                    editor.commit();
                }
                break;
            }
        }
    }

    /**
     * This function is called to send notification to NotificationManager
     */
//    private void showNotification(int icon, Context context) {
//        Log.d(TAG, "showNotification: notification sent.." + mDeviceAddress);
//        Notification notification = new Notification(icon, "", System.currentTimeMillis());
//        Intent notificationIntent = new Intent(context, MusicActivity.class);
//        notificationIntent.putExtra(AVRCP_TG_NAME, mDeviceName);
//        notificationIntent.putExtra(AVRCP_TG_ADDRESS, mDeviceAddress);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        notification.setLatestEventInfo(context, "Connected to", mDeviceName, contentIntent);
//        mNotificationManager.notify(AVRCP_NOTIFICATION_ID, notification);
//    }
    //测试
    /**
     * This function is called to send notification to NotificationManager
     */
    private void showNotification(int icon, Context context) {
        Log.d(TAG, "showNotification: notification sent.." + mDeviceAddress);
//        Notification notification = new Notification(icon, "", System.currentTimeMillis());
//        Intent notificationIntent = new Intent(context, MusicActivity.class);
//        notificationIntent.putExtra(AVRCP_TG_NAME, mDeviceName);
//        notificationIntent.putExtra(AVRCP_TG_ADDRESS, mDeviceAddress);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        notification.setLatestEventInfo(context, "Connected to", mDeviceName, contentIntent);

        Notification noti = new Notification.Builder(context)
                .setContentTitle(mDeviceName)
                .setContentText(mDeviceAddress)
                .setSmallIcon(icon)
                .build();
//        mNotificationManager.notify(AVRCP_NOTIFICATION_ID, notification);
        mNotificationManager.notify(AVRCP_NOTIFICATION_ID, noti);
    }
}
