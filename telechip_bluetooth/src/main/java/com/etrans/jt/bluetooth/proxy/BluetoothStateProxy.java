package com.etrans.jt.bluetooth.proxy;

import com.etrans.jt.bluetooth.base.BaseProxy;
import com.etrans.jt.btlibrary.module.BluetoothModule;

import java.lang.reflect.Method;

/**
 * 单元名称:BluetoothStateProxy.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class BluetoothStateProxy extends BaseProxy {
    public BluetoothStateProxy(Object _target) {
        super(_target);
    }

    @Override
    protected boolean beforeCall(Object proxy, Method method, Object[] args, Object resObj) {

        if (BluetoothModule.getInstance().isBtConnect()) {
            return true;
        } else {
            BluetoothModule.getInstance().notifyBtState();
            return false;
        }
    }

    @Override
    protected void afterCall(Object proxy, Method method, Object[] args, Object resObj) {
        super.afterCall(proxy, method, args, resObj);
    }
}
