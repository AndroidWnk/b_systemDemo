package com.etrans.jt.bluetooth.base;

import android.content.Context;

import com.etrans.jt.btlibrary.module.BaseModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyanze on 2016/3/8.
 */
public abstract class BasePresenter {

    private List<BaseModule> modules;
    protected Context mActContext;


    public BasePresenter(Context _context) {
        this.mActContext = _context;
        registerRefHandles();
    }

    private void registerRefHandles() {
        if (modules == null) {
            modules = new ArrayList<BaseModule>();
        }
        List<BaseModule> tmpModules = getRefHandles();
        for (BaseModule module : tmpModules) {
            module.registerListener(this);
            modules.add(module);
        }

    }

    protected abstract List<BaseModule> getRefHandles();

    public void onDestory() {
        if (modules != null) {
            for (BaseModule module : modules) {
                module.unregisterListener(this);
            }
        }
    }


}
