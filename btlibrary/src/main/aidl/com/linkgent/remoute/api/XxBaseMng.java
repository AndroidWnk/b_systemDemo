package com.linkgent.remoute.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/3/2.
 */

abstract class XxBaseMng {
    XxServiceConnection mCon = null;
    Context mContext;
    boolean mbAddListener = false;
    boolean mbRelease = false;
    ServiceConnection con = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            XxBaseMng.this.onServiceConnected(name, service);
            if(XxBaseMng.this.mCon != null) {
                XxBaseMng.this.mCon.onServiceConnected();
            }

        }

        public void onServiceDisconnected(ComponentName name) {
            XxBaseMng.this.onServiceDisconnected(name);
            if(XxBaseMng.this.mCon != null) {
                XxBaseMng.this.mCon.onServiceDisconnected();
            }

        }
    };
    protected MyHandler mHandler = null;

    public XxBaseMng(Context context, XxServiceConnection con) {
        this.mContext = context.getApplicationContext();
        this.mCon = con;
        this.mHandler = new MyHandler(this);
    }

    public void setServiceConnection(XxServiceConnection con) {
        this.mCon = con;
    }

    protected abstract void onHandleMessage(Message var1);

    protected abstract void onServiceConnected(ComponentName var1, IBinder var2);

    protected abstract void onServiceDisconnected(ComponentName var1);

    protected void bindService(String action) {
        Intent service = new Intent(action);
        this.mContext.bindService(service, this.con, 1);
    }

    protected void unBind() {
        this.mContext.unbindService(this.con);
    }

    public void release() {
        this.mHandler.removeCallbacksAndMessages((Object)null);
    }

    public void sendMessage(int what, int arg1, Object obj) {
        Message msg = this.mHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        this.mHandler.sendMessage(msg);
    }

    public void sendMessageDelayed(int what, int arg1, Object obj, long delayMillis) {
        Message msg = this.mHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        this.mHandler.sendMessageDelayed(msg, delayMillis);
    }

    static class MyHandler extends Handler {
        WeakReference<XxBaseMng> mReference;

        MyHandler(XxBaseMng mng) {
            this.mReference = new WeakReference(mng);
        }

        public void handleMessage(Message msg) {
            XxBaseMng mng = (XxBaseMng)this.mReference.get();
            if(mng != null) {
                mng.onHandleMessage(msg);
            }
        }
    }
}
