package com.etrans.jt.btlibrary.module;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 单元名称:BaseModule.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public abstract class BaseModule<T> {
    protected  Context mContext;
    protected List<T> listeners = new ArrayList<T>();

    public BaseModule() {

    }


    public void registerListener(T obj) {
        if (!listeners.contains(obj)) {
            listeners.add(obj);
        }
    }

    public void unregisterListener(T obj) {
        if (listeners.contains(obj)) {
            listeners.remove(obj);
        }
    }



}
