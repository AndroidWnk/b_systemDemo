package com.linkgent.remoute.api;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.SparseArray;

import com.linkgent.xxaidl.IXxAudioFocusChange;
import com.linkgent.xxaidl.IXxAudioService;

/**
 * Created by Administrator on 2017/3/2.
 */

class XxAudioServiceMng extends XxBaseMng {
    IXxAudioService mAudioService = null;
    SparseArray<IXxAudioFocusChange> mSparrAudioListener = new SparseArray();

    public XxAudioServiceMng(Context context, XxServiceConnection con) {
        super(context, con);
        this.mHandler = new MyHandler(this);
        this.mbRelease = false;
        this.mbAddListener = false;
        this.bindService("com.linkgent.remote.service.XXAUDIOSERVICE");
    }

    protected void onHandleMessage(Message msg) {
        if(msg.what == 1) {
            IXxAudioFocusChange l = (IXxAudioFocusChange)msg.obj;
            if(this.mAudioService != null) {
                try {
                    this.mAudioService.addIXxAudioFocusChange(msg.arg1, l);
                } catch (RemoteException var4) {
                    var4.printStackTrace();
                }
            } else {
                this.addAudioFocus(msg.arg1, l);
            }
        }

    }

    protected void onServiceConnected(ComponentName name, IBinder service) {
        this.mAudioService = IXxAudioService.Stub.asInterface(service);
        if(this.mbAddListener) {
            for(int i = 0; i < this.mSparrAudioListener.size(); ++i) {
                int key = this.mSparrAudioListener.keyAt(i);
                IXxAudioFocusChange l = (IXxAudioFocusChange)this.mSparrAudioListener.get(key);
                if(l != null) {
                    try {
                        this.mAudioService.addIXxAudioFocusChange(key, l);
                    } catch (RemoteException var7) {
                        var7.printStackTrace();
                    }
                }
            }

            this.mbAddListener = false;
        }

    }

    protected void onServiceDisconnected(ComponentName name) {
        this.mAudioService = null;
        this.mbAddListener = true;
        this.mHandler.removeCallbacksAndMessages((Object)null);
        if(!this.mbRelease) {
            this.bindService("com.linkgent.remote.service.XXAUDIOSERVICE");
        }
    }

    public void release() {
        super.release();
        this.mbRelease = true;
        this.unBind();
        this.mAudioService = null;
    }

    public void addAudioFocusListener(int module, IXxAudioFocusChange listener) {
        this.mSparrAudioListener.put(module, listener);
        if(this.mAudioService != null) {
            try {
                this.mAudioService.addIXxAudioFocusChange(module, listener);
            } catch (RemoteException var4) {
                var4.printStackTrace();
            }
        }

        this.addAudioFocus(module, listener);
    }

    private void addAudioFocus(int module, IXxAudioFocusChange listener) {
        this.sendMessageDelayed(1, module, listener, 1000L);
    }

    public void removeAudioFocusListener(int module) {
        this.mSparrAudioListener.remove(module);
        if(this.mAudioService != null) {
            try {
                this.mAudioService.removeIXxAudioFocusChange(module);
            } catch (RemoteException var3) {
                var3.printStackTrace();
            }

        }
    }
}
