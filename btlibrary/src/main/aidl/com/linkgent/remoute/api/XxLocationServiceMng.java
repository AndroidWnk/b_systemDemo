package com.linkgent.remoute.api;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.linkgent.xxaidl.IXxDistrictListener;
import com.linkgent.xxaidl.IXxGpsStarListener;
import com.linkgent.xxaidl.IXxGpsStateListener;
import com.linkgent.xxaidl.IXxLocationListener;
import com.linkgent.xxaidl.IXxLocationService;
import com.linkgent.xxaidl.XxDistrict;
import com.linkgent.xxaidl.XxLocationInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

class XxLocationServiceMng extends XxBaseMng {
    static final int MSG_LOCATION = 1;
    static final int MSG_GPSSTAR = 2;
    static final int MSG_GPSSTATE = 3;
    static final int MSG_DISTRICT = 4;
    IXxLocationService mLocationService;
    HashSet<IXxLocationListener> mSetLoaction = new HashSet();
    List<IXxLocationListener> mArrLoaction = new ArrayList();
    HashSet<IXxGpsStateListener> mSetState = new HashSet();
    List<IXxGpsStateListener> mArrState = new ArrayList();
    HashSet<IXxGpsStarListener> mSetStar = new HashSet();
    List<IXxGpsStarListener> mArrStar = new ArrayList();
    HashSet<IXxDistrictListener> mSetDistrict = new HashSet();
    List<IXxDistrictListener> mArrDistrict = new ArrayList();

    public XxLocationServiceMng(Context context, XxServiceConnection con) {
        super(context, con);
        this.bindService("com.linkgent.remote.service.XXLOCATIONSERVICE");
    }

    public void addLocationListener(IXxLocationListener listener) {
        if(listener != null) {
            if(!this.mSetLoaction.contains(listener)) {
                this.mSetLoaction.add(listener);
                this.mArrLoaction.add(listener);
                if(this.mLocationService != null) {
                    try {
                        this.mLocationService.addIXxLocationListener(listener);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }
                } else {
                    this.sendMessageDelayed(1, 0, listener, 1000L);
                }

            }
        }
    }

    public void removeLocationListener(IXxLocationListener listener) {
        if(this.mLocationService != null) {
            if(listener != null) {
                if(this.mSetLoaction.contains(listener)) {
                    this.mSetLoaction.remove(listener);
                    this.mArrLoaction.remove(listener);

                    try {
                        this.mLocationService.removeIXxLocationListener(listener);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }

                }
            }
        }
    }

    public void release() {
        super.release();
        this.mbRelease = true;
        this.unBind();
    }

    public void addGpsStateListener(IXxGpsStateListener listener) {
        if(listener != null) {
            if(!this.mSetState.contains(listener)) {
                this.mSetState.add(listener);
                this.mArrState.add(listener);
                if(this.mLocationService != null) {
                    try {
                        this.mLocationService.addIXxGpsStateListener(listener);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }
                } else {
                    this.sendMessageDelayed(3, 0, listener, 1000L);
                }

            }
        }
    }

    public void removeGpsStateListener(IXxGpsStateListener listener) {
        if(this.mLocationService != null) {
            if(listener != null) {
                if(this.mSetState.contains(listener)) {
                    this.mSetState.remove(listener);
                    this.mArrState.remove(listener);

                    try {
                        this.mLocationService.removeIXxGpsStateListener(listener);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }

                }
            }
        }
    }

    public void addGpsStarListener(IXxGpsStarListener listener) {
        if(listener != null) {
            if(!this.mSetStar.contains(listener)) {
                this.mSetStar.add(listener);
                this.mArrStar.add(listener);
                if(this.mLocationService != null) {
                    try {
                        this.mLocationService.addIXxGpsStarListener(listener);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }
                } else {
                    this.sendMessageDelayed(2, 0, listener, 1000L);
                }

            }
        }
    }

    public void removeGpsStarListener(IXxGpsStarListener listener) {
        if(this.mLocationService != null) {
            if(listener != null) {
                if(this.mSetStar.contains(listener)) {
                    this.mSetStar.remove(listener);
                    this.mArrStar.remove(listener);

                    try {
                        this.mLocationService.removeIXxGpsStarListener(listener);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }

                }
            }
        }
    }

    public void addDistrictListener(IXxDistrictListener listener) {
        if(listener != null) {
            if(!this.mSetDistrict.contains(listener)) {
                this.mSetDistrict.add(listener);
                this.mArrDistrict.add(listener);
                if(this.mLocationService != null) {
                    try {
                        this.mLocationService.addIXxDistrictListener(listener);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }
                } else {
                    this.sendMessageDelayed(4, 0, listener, 1000L);
                }

            }
        }
    }

    public void removeDistrictListener(IXxDistrictListener listener) {
        if(this.mLocationService != null) {
            if(listener != null) {
                if(this.mSetDistrict.contains(listener)) {
                    this.mSetDistrict.remove(listener);
                    this.mArrDistrict.remove(listener);

                    try {
                        this.mLocationService.removeIXxDistrictListener(listener);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }

                }
            }
        }
    }

    protected void onHandleMessage(Message msg) {
        if(msg.what >= 1 && msg.what <= 4) {
            if(this.mLocationService != null) {
                if(msg.what == 4) {
                    IXxDistrictListener l = (IXxDistrictListener)msg.obj;
                    if(l == null) {
                        return;
                    }

                    try {
                        this.mLocationService.addIXxDistrictListener(l);
                    } catch (RemoteException var7) {
                        var7.printStackTrace();
                    }
                } else if(msg.what == 1) {
                    IXxLocationListener l1 = (IXxLocationListener)msg.obj;
                    if(l1 == null) {
                        return;
                    }

                    try {
                        this.mLocationService.addIXxLocationListener(l1);
                    } catch (RemoteException var6) {
                        var6.printStackTrace();
                    }
                } else if(msg.what == 2) {
                    IXxGpsStarListener l2 = (IXxGpsStarListener)msg.obj;
                    if(l2 == null) {
                        return;
                    }

                    try {
                        this.mLocationService.addIXxGpsStarListener(l2);
                    } catch (RemoteException var5) {
                        var5.printStackTrace();
                    }
                } else if(msg.what == 3) {
                    IXxGpsStateListener l3 = (IXxGpsStateListener)msg.obj;
                    if(l3 == null) {
                        return;
                    }

                    try {
                        this.mLocationService.addIXxGpsStateListener(l3);
                    } catch (RemoteException var4) {
                        var4.printStackTrace();
                    }
                }
            } else {
                this.sendMessageDelayed(msg.what, 0, msg.obj, 1000L);
            }

        }
    }

    protected void onServiceConnected(ComponentName name, IBinder service) {
        this.mLocationService = IXxLocationService.Stub.asInterface(service);
        if(this.mbAddListener) {
            this.addListener();
            this.mbAddListener = false;
        }

    }

    protected void onServiceDisconnected(ComponentName name) {
        this.mLocationService = null;
        if(!this.mbRelease) {
            this.mbAddListener = true;
            this.mHandler.removeCallbacksAndMessages((Object)null);
            this.bindService("com.linkgent.remote.action.XXLOCATION");
        }
    }

    void addListener() {
        int i;
        for(i = 0; i < this.mArrDistrict.size(); ++i) {
            IXxDistrictListener l = (IXxDistrictListener)this.mArrDistrict.get(i);
            if(l != null) {
                try {
                    this.mLocationService.addIXxDistrictListener(l);
                } catch (RemoteException var7) {
                    var7.printStackTrace();
                }
            }
        }

        for(i = 0; i < this.mArrState.size(); ++i) {
            IXxGpsStateListener var8 = (IXxGpsStateListener)this.mArrState.get(i);
            if(var8 != null) {
                try {
                    this.mLocationService.addIXxGpsStateListener(var8);
                } catch (RemoteException var6) {
                    var6.printStackTrace();
                }
            }
        }

        for(i = 0; i < this.mArrStar.size(); ++i) {
            IXxGpsStarListener var9 = (IXxGpsStarListener)this.mArrStar.get(i);
            if(var9 != null) {
                try {
                    this.mLocationService.addIXxGpsStarListener(var9);
                } catch (RemoteException var5) {
                    var5.printStackTrace();
                }
            }
        }

        for(i = 0; i < this.mArrLoaction.size(); ++i) {
            IXxLocationListener var10 = (IXxLocationListener)this.mArrLoaction.get(i);
            if(var10 != null) {
                try {
                    this.mLocationService.addIXxLocationListener(var10);
                } catch (RemoteException var4) {
                    var4.printStackTrace();
                }
            }
        }

    }

    public int getGpsState() {
        if(this.mLocationService != null) {
            try {
                return this.mLocationService.getGpsState();
            } catch (RemoteException var2) {
                var2.printStackTrace();
            }
        }

        return 0;
    }

    public XxLocationInfo getLocationInfo() {
        if(this.mLocationService != null) {
            try {
                return this.mLocationService.getLocationInfo();
            } catch (RemoteException var2) {
                var2.printStackTrace();
            }
        }

        return null;
    }

    public XxDistrict getCurDistrictInfo() {
        if(this.mLocationService != null) {
            try {
                return this.mLocationService.getCurDistrictInfo();
            } catch (RemoteException var2) {
                var2.printStackTrace();
            }
        }

        return null;
    }

    public XxDistrict queryDistritByCode(int district) {
        if(this.mLocationService != null) {
            try {
                return this.mLocationService.queryDistritByCode(district);
            } catch (RemoteException var3) {
                var3.printStackTrace();
            }
        }

        return null;
    }

    public XxDistrict queryDistrictByGeo(int gpsType, double lon, double lat) {
        if(this.mLocationService != null) {
            try {
                return this.mLocationService.queryDistrictByGeo(gpsType, lon, lat);
            } catch (RemoteException var7) {
                var7.printStackTrace();
            }
        }

        return null;
    }
}
