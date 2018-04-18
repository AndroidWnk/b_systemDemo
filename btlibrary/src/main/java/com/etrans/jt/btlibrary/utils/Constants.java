package com.etrans.jt.btlibrary.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * 单元名称:Constants.java
 * Created by fuxiaolei on 2016/7/5.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/5.
 */
public class Constants {
    //服务
    public static final String ACTION_BLUETOOTH_SERVICE = "com.etrans.jt.bluetooth.service.BluetoothService";
    public static final String ME = "ME";
    public static final String DC = "DC";

    //    public static final int MAX_READ_LIMIT_COUNT = 2147483647;
    public static final int MAX_READ_LIMIT_COUNT = 20;
    public static final long TIME = 1000;

    public class Action {
        //开机广播
        public static final String ACTION_BOOT_COMPLETED = Intent.ACTION_BOOT_COMPLETED;
        public static final String ACTION_BLUETOOTH_CONNECTION_STATE_CHANGED = BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED;

        public static final String AVRCP_TG_NAME = "com.broadcom.bt.app.avrcp.targetname";
        public static final String AVRCP_TG_ADDRESS = "com.broadcom.bt.app.avrcp.targetaddress";
        public static final String AVRCP_CONNECTED = "com.broadcom.bt.app.avrcp.connected";
        public static final int AVRCP_NOTIFICATION_ID = -1000003;   //identifier for the notification
    }

    //music
    public static final int LOAD_CONN_DEV_REQUEST = 10;
    public static final int START_AVRC_REQUEST = 100;
    public static final int STOP_AVRC_REQUEST = 101;
    public static final int LIST_APP_ATTR_REQUEST = 102;
    public static final int LIST_APP_ATTR_VALS_REQUEST = 103;
    public static final int GET_CURR_ATTR_VAL_REQUEST = 104;
    public static final int SET_CURR_ATTR_VAL_REQUEST = 105;
    public static final int GET_APP_ATTR_TEXT_REQUEST = 106;
    public static final int GET_APP_ATTR_VAL_TEXT_REQUEST = 107;
    public static final int GET_ELEMENT_ATTR_REQUEST = 108;
    public static final int GET_PLAYER_STATUS_REQUEST = 109;
    public static final int MEDIA_SEEK_REQUEST = 110;
    /* Command Events for updating the UI based on the callbacks received for the above request
    events.*/
    public static final int CMD_ON_LOAD_APP_ATTR_FAIL = 200;
    public static final int CMD_RELOAD_UI = 201;
    public static final int CMD_RELOAD_METADATA = 202;
    public static final int CMD_RELOAD_PROGRESS = 203;
    public static final int CMD_UPDATE_PROGRESS = 204;
    public static final int CMD_UPDATE_UI_FOR_SEEK = 205;
    public static final int CMD_UPDATE_UI_APP_SETTINGS = 206;
    public static final int CMD_RESET_METADATA = 207;
    public static final int CMD_ALTERNATE_UPDATE_TIMER = 208;

    //For adding menu Items dynamically
    public static final int browse_button = 501;

    public static final String EXTRA_DEVICE = "connected_device";
    public static final boolean D = true;
    public static final int GUI_UPDATE_MSG_WAITING_FOR_INIT_COMPLETE = 1000;
    public static final int GUI_UPDATE_MSG_NO_CONN_DEVICE_ERROR = 1001;

    public static final int GET_MEDIA_PLAYER_LIST = 5001;
    public static final int GET_NOW_PLAYING = 5004;

    public static final int GOT_MEDIA_PLAYER_LIST = 5101;

    public static final int GOT_NOW_PLAYING = 5106;
    public static final int PLAY_NOW_ITEM = 5107;

    //电话相关
    public static final int PHONE_BASE = 10000;
    public static final int PHONE_DOWNLOAD_PHONE_BOOK = PHONE_BASE + 1;
    public static final int PHONE_CLEAN_PHONE_BOOK = PHONE_BASE + 2;
    public static final int PHONE_INSTER_PHONE_BOOK = PHONE_BASE + 3;
    public static final int PHONE_UPDATE_TIME = PHONE_BASE + 4;

}
