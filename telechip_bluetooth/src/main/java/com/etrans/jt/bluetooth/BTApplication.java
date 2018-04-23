package com.etrans.jt.bluetooth;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.etrans.jt.bluetooth.base.BaseApplication;
import com.etrans.jt.bluetooth.event.BaseEvent;
import com.etrans.jt.bluetooth.utils.CrashHandler;
import com.etrans.jt.bluetooth.utils.XxCrashHandler;
import com.etrans.jt.btlibrary.db.DataOperation;
import com.etrans.jt.btlibrary.gps.XxGpsManager;
import com.etrans.jt.btlibrary.manager.XxConfig;
import com.etrans.jt.btlibrary.manager.XxNetManager;
import com.etrans.jt.btlibrary.manager.XxNotificationManager;
import com.etrans.jt.btlibrary.manager.XxOtherSettingManager;
import com.etrans.jt.btlibrary.module.BluetoothModule;
import com.etrans.jt.btlibrary.module.BluetoothMusicModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneBookModule;
import com.etrans.jt.btlibrary.module.BluetoothPhoneModule;
import com.etrans.jt.btlibrary.module.XxAudioManager;
import com.etrans.jt.btlibrary.utils.XxSetting;
import com.txznet.sdk.TXZCallManager;
import com.txznet.sdk.TXZConfigManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 单元名称:BTApplication.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class BTApplication extends BaseApplication implements TXZConfigManager.InitListener {
    private static final String TAG = BTApplication.class.getSimpleName();
    public static BTApplication appInstance;
    public static Context appContext;
    private XxConfig mConfig;
    private XxSetting mSetting;
    TXZConfigManager.InitParam mInitParam;
    public TXZCallManager.CallToolStatusListener mCallToolStatusListener;
    public static int pageIndex = 0;
    public static boolean isPlaying = false;
    public static boolean isBackingUp = false;//标记是否在倒车或ACC状态中

    @Override
    public void onCreate() {
        super.onCreate();
        //捕捉崩溃异常信息
        CrashHandler.getInstance().init(this);
        EventBus.getDefault().register(this);
        appInstance = this;
        appContext = this;
        XxCrashHandler.getInstance().init(appInstance);
        DataOperation.getInstance().init(appContext);
        XxAudioManager.getInstance().init(appContext);
        BluetoothModule.getInstance().init(appContext);
        BluetoothPhoneBookModule.getInstance().init(appContext);
        BluetoothPhoneModule.getInstance().init(appContext);//蓝牙电话
        BluetoothMusicModule.getInstance().init(appContext);//蓝牙音乐

        //
        initManager();

        getDisplay();

        {
            // TODO 获取接入分配的appId和appToken
            String appId = this.getResources().getString(
                    R.string.txz_sdk_init_app_id);
            String appToken = this.getResources().getString(
                    R.string.txz_sdk_init_app_token);
            // TODO 设置初始化参数
            mInitParam = new TXZConfigManager.InitParam(appId, appToken);
            // TODO 可以设置自己的客户ID，同行者后台协助计数/鉴权
            // mInitParam.setAppCustomId("ABCDEFG");
            // TODO 可以设置自己的硬件唯一标识码
            // mInitParam.setUUID("0123456789");
        }

        {
            // TODO 设置识别和tts引擎类型
            mInitParam.setAsrType(TXZConfigManager.AsrEngineType.ASR_YUNZHISHENG).setTtsType(
                    TXZConfigManager.TtsEngineType.TTS_YUNZHISHENG);
        }

        {
            // TODO 设置唤醒词
//            String[] wakeupKeywords = this.getResources().getStringArray(
//                    R.array.txz_sdk_init_wakeup_keywords);
//            mInitParam.setWakeupKeywordsNew(wakeupKeywords);

            Log.d(TAG, "not set keyword wakeup");
        }
        TXZConfigManager.getInstance().initialize(this, this);

        TXZCallManager.getInstance().setCallTool(new TXZCallManager.CallTool() {
            @Override
            public CallStatus getStatus() {
                return null;
            }

            @Override
            public boolean makeCall(TXZCallManager.Contact contact) {
                return false;
            }

            @Override
            public boolean acceptIncoming() {
                return false;
            }

            @Override
            public boolean rejectIncoming() {
                return false;
            }

            @Override
            public boolean hangupCall() {
                return false;
            }

            @Override
            public void setStatusListener(TXZCallManager.CallToolStatusListener callToolStatusListener) {
                BTApplication.this.mCallToolStatusListener = callToolStatusListener;
                if (BluetoothModule.getInstance().isBtConnect()){
                    mCallToolStatusListener.onEnabled();
                    Log.e("isBtConnect",BluetoothModule.getInstance().isBtConnect()+"");
                    mCallToolStatusListener.onIdle();
                }else mCallToolStatusListener.onDisabled("很抱歉，蓝牙断开了，电话不可用了");
            }
        });


    }

    @Subscribe
    public void onEventMainThread(BaseEvent event) {
        Log.e("onEventMainThread", event.getEventCode() + "");
        switch (event.getEventCode()) {
            case 10:
                if (mCallToolStatusListener != null) {
                    mCallToolStatusListener.onEnabled();
                }
                break;
            case 11:
                if (mCallToolStatusListener != null) {
                    mCallToolStatusListener.onDisabled("很抱歉，蓝牙断开了，电话不可用了");
                }
                break;
        }
    }

    private void initManager() {
        mSetting = XxSetting.getInstance().init(this);
        mConfig = XxConfig.getInstance().init(this,
                "XxSettingModule");
        XxGpsManager.getInstance().init(this);

        XxNotificationManager.getInstance().init(this);

        XxAudioManager.getInstance().init(this);

//        XxRadioManager.getInstance().init(this);
        XxNetManager.getInstance().init(this);
        XxOtherSettingManager.getInstance().init(this);
        if (!XxConfig.getInstance().isDefalutSetting()) {
            XxNetManager.getInstance().initDefaultStatus();
            XxOtherSettingManager.getInstance().initDefaultStatus();
            XxConfig.getInstance().setDefalutSetting(true);
        }

        //启动天气服务 不要注释以下代码
//        try {
//            Intent intent = new Intent("com.hero.xxscreenlock.MYSERVICE");
//            startService(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } catch (Error error) {
//            error.printStackTrace();
//        }
    }

    public void finishActivity() {
        finishAllActivity();
    }

    private void getDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0�?
        int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320�?
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        int screenWidth = dm.widthPixels; // 屏幕宽（像素，如�?80px�?
        int screenHeight = dm.heightPixels; // 屏幕高（像素，如�?00px�?

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);


        Log.e(TAG + "  DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
        Log.e(TAG + "  DisplayMetrics", "density=" + density + "; densityDPI="
                + densityDPI);
        Log.e(TAG, "screenWidth = " + screenWidth + "     screenHeight=" + screenHeight);
        Log.e(TAG, "屏幕尺寸 = " + screenInches);
    }

    @Override
    public void onSuccess() {
//        TXZTtsManager.getInstance().speakText("");
    }

    @Override
    public void onError(int i, String s) {
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EventBus.getDefault().unregister(this);
    }
}
