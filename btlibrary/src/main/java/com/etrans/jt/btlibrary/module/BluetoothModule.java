package com.etrans.jt.btlibrary.module;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.etrans.jt.btlibrary.R;
import com.etrans.jt.btlibrary.listener.BluetoothStateChangeListener;

/**
 * 单元名称:BluetoothModule.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:蓝牙状态监听
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class BluetoothModule extends BaseModule<BluetoothStateChangeListener> {

    private static Context mContext;
    private static BluetoothModule instance;
    private boolean btIsOpen = false;
    private boolean btIsConnect = false;
    private BluetoothAdapter adapter;
    private String deviceName = "";
    private String BT_SP_NAME = "bt";
    private static SharedPreferences mPreferences = null;
    private int isBlueCon;

    private BluetoothModule() {
        super();
    }

    public static BluetoothModule getInstance() {

        synchronized (BluetoothModule.class) {
            if (instance == null) {
                instance = new BluetoothModule();
            }
        }
        return instance;
    }

    /**
     * 初始化设备信息
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取车机的设备信息
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (mPreferences == null)
            mPreferences = context.getSharedPreferences(BT_SP_NAME, Context.MODE_PRIVATE);
//        initData();
        boolean isConnect = mPreferences.getBoolean("isConnect", false);
        btIsConnect = isConnect();
//        btIsConnect = isConnect;

    }

    /**
     * 蓝牙连接了
     *
     * @param intent
     */
    public void onConnect(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        setDeviceName(device.getName());
        boolean enabled = adapter.isEnabled();
        if (enabled) {
            btIsOpen = enabled;
            getDevicesName();
        }
        //TODO 保存连接状态
        mPreferences.edit().putBoolean("isConnect", true).commit();
        mPreferences.edit().putString("deviceName", device.getName()).commit();
        btIsConnect = true;
        Log.d("bluetoothName", "save name=" + device.getName());
    }

    /**
     * 连接断开了
     */
    public void onDisConnect() {
        mPreferences.edit().putBoolean("isConnect", false).commit();
        mPreferences.edit().putString("deviceName", "").commit();
        clear();
        Log.d("bluetoothName", "clear name===============");
    }

    /**
     * 初始化蓝牙状态信息
     */
    private void initData() {
        boolean enabled = adapter.isEnabled();
        if (enabled) {
            btIsOpen = enabled;
            btIsConnect = true;
//            getDevicesName();
        }
        //获取设备信息 展示
        //下载电话本
        //获取通话状态
        //获取音乐状态

    }

    /**
     * 断开连接后 清除设备相关信息
     */
    private void clear() {
        btIsConnect = false;
        deviceName = "";
        //清除设备信息
        //清除电话本数据库
        //UI刷新
    }

    /**
     * 打开蓝牙
     */
    public void openOrClose() {
        if (btIsOpen) {
            adapter.disable();
            Toast.makeText(mContext, "关", Toast.LENGTH_LONG).show();
        } else {
            adapter.enable();
            Toast.makeText(mContext, "开", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取本机蓝牙名字
     *
     * @return
     */
    public String getName() {
        if (btIsConnect) {
            return adapter.getName();
        } else {
            return mContext.getString(R.string.bt_Please_connect_bt);
        }

    }

    /**
     * 获取连接的设备的名称
     *
     * @return
     */
    public String getDevicesName() {
        deviceName = mPreferences.getString("deviceName", "");
        return deviceName;
    }


    /**
     * 蓝牙关闭状态回调
     */
    public void onClose() {
        btIsOpen = false;
    }

    /**
     * 关闭中状态回调...
     */
    public void onClosing() {

    }

    /**
     * 蓝牙开启状态回调
     */
    public void onOpen() {
        btIsOpen = true;
    }

    /**
     * 开启中状态回调...
     */
    public void onOpening() {

    }

    /**
     * 蓝牙连接状态改变
     */
    public void notifyBtState() {
        if (btIsOpen) {
            //未连接
            for (BluetoothStateChangeListener listener : listeners) {
                listener.onBluetoothDisConnect();
            }

        } else {
            //未开启
            for (BluetoothStateChangeListener listener : listeners) {
                listener.onBluetoothClose();
            }
        }
    }

    public boolean isBtOpen() {
        return btIsOpen;
    }

    public boolean isBtConnect() {


        return isConnect();
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        for (BluetoothStateChangeListener listener : listeners) {
            listener.onUpdateConnectDeviceName(mContext.getString(R.string.bt_connected) + deviceName);
        }
    }

    private BluetoothAdapter ba;                   //蓝牙适配器
    ConnectivityManager mConnectMgr;

    private boolean isConnect() {

        ba = BluetoothAdapter.getDefaultAdapter();
        mConnectMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //蓝牙适配器是否存在，即是否发生了错误
        if (ba == null) {
            //error
            isBlueCon = -1;
        } else if (ba.isEnabled()) {
            int a2dp = ba.getProfileConnectionState(BluetoothProfile.A2DP);              //可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
            int headset = ba.getProfileConnectionState(BluetoothProfile.HEADSET);        //蓝牙头戴式耳机，支持语音输入输出
            int health = ba.getProfileConnectionState(BluetoothProfile.HEALTH);          //蓝牙穿戴式设备

            //查看是否蓝牙是否连接到三种设备的一种，以此来判断是否处于连接状态还是打开并没有连接的状态
            int flag = -1;
            if (a2dp == BluetoothProfile.STATE_CONNECTED) {
                flag = a2dp;
            } else if (headset == BluetoothProfile.STATE_CONNECTED) {
                flag = headset;
            } else if (health == BluetoothProfile.STATE_CONNECTED) {
                flag = health;
            }
            //说明连接上了三种设备的一种
            if (flag != -1) {
                isBlueCon = 1;            //discontinued
            } else if (flag == -1) {
                NetworkInfo netInfo = mConnectMgr.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

                if (netInfo == null) {
                    isBlueCon = -1;     //discontinued
                } else {
                    NetworkInfo.State blt = netInfo.getState();
//                    isBlueCon = getDevState(blt);          //系统内部，返回连接与否
                }
            }
            //判断    Handfree 是否连接
            int handsfreeConnectionState = BluetoothPhoneModule.getInstance().getHandsfreeConnectionState();
            if (handsfreeConnectionState == BluetoothProfile.STATE_CONNECTED){
                isBlueCon = 1;
                if (a2dp==BluetoothProfile.STATE_DISCONNECTED){
//                    BluetoothMusicModule.getInstance().initMusicService();
                    BluetoothMusicModule.getInstance().connectAudioDevice(BluetoothPhoneModule.getInstance().getConnectDevice());
                }
            }


        } else {
            isBlueCon = 2;    //shut off
        }


        return isBlueCon == 1 ? true : false;
    }
}
