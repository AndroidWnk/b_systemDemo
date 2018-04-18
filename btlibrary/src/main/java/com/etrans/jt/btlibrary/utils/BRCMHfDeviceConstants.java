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

package com.etrans.jt.btlibrary.utils;

public class BRCMHfDeviceConstants {
    public static final String HF_DEVICE_NAME = "com.broadcom.bt.app.hfdevice.devicename";
    public static final String HF_DEVICE_ADDRESS = "com.broadcom.bt.app.hfdevice.deviceaddress";
    public static final String HF_DEVICE_CONNECTED = "com.broadcom.bt.app.hfdevice.connected";
    public static final String HF_DEVICE_STATUS = "com.broadcom.bt.app.hfdevice.status";
    public static final String HF_DEVICE_STATE_CHANGE_TYPE =
            "com.broadcom.bt.app.hfdevice.statechangetype";
    public static final String HF_DEVICE_WBS_STATE = "com.broadcom.bt.app.hfdevice.wbs_state";
    public static final String HF_DEVICE_IS_HSP_CONNECTION =
            "com.broadcom.bt.app.hfdevice.isHspConnection";

    public static final int HF_DEVICE_BROADCAST_CALL_STATE_CHANGE = 0;
    public static final int HF_DEVICE_BROADCAST_DEVICE_STATE_CHANGE = 1;
    public static final int HF_DEVICE_BROADCAST_AUDIO_STATE_CHANGE = 2;

    public static final int HF_DEVICE_NOTCONNECTED_DIALOG_ID = 1;
    public static final int HF_DEVICE_SERVICE_NOT_ENABLED = 2;
    public static final int HF_DEVICE_AT_CMD_ENTRY_DIALOG_ID = 3;
    public static final int HF_DEVICE_CALL_WAITING_DIALOG_ID = 4;
    public static final int HF_DEVICE_MULTI_CALL_CONTROL_DIALOG_ID = 5;
    public static final int HF_DEVICE_VOLUME_CHANGE_FAILED_DIALOG_ID = 6;
    public static final int HF_NOTIFICATION_ID = -1000001;   //identifier for the notification
    //刷新蓝牙电话刷新UI
    public static final int GUI_UPDATE_DEVICE_STATUS = 1;
    public static final int GUI_UPDATE_CALL_STATUS = 2;
    public static final int GUI_UPDATE_DEVICE_INDICATORS = 3;
    public static final int GUI_UPDATE_INCOMING_CALL_NUMBER = 4;
    public static final int GUI_UPDATE_AUDIO_STATE = 5;
    public static final int GUI_UPDATE_VENDOR_AT_RSP = 6;
    public static final int GUI_UPDATE_CLCC_AT_RSP = 7;
    public static final int GUI_UPDATE_VOLUME = 8;
    public static final int GUI_UPDATE_OPERATOR = 9;
    public static final int GUI_UPDATE_SUBSCRIBER = 10;
    public static final int GUI_UPDATE_VR_STATE = 11;
    public static final int GUI_UPDATE_PHONEBOOK_AT_RSP = 12;
    public static final int GUI_UPDATE_WBS_STATE = 13;
    public static final int GUI_UPDATE_RING = 14;
    public static final int GUI_UPDATE_IN_BAND_STATUS = 15;
    public static final int GUI_UPDATE_NREC_STATUS = 16;

    public static final int RING_STATE_IDLE = 0;
    public static final int RING_STATE_RINGING = 1;
}