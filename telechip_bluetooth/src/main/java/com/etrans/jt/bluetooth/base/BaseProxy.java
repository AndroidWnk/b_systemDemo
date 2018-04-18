package com.etrans.jt.bluetooth.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liyanze on 2016/3/8.
 */
public abstract class BaseProxy {

    private Object target;
    private Invocation invocation;

    protected BaseProxy(Object _target) {
        this.target = _target;
        invocation = new Invocation();
    }

    public Object bind() {
        return invocation.bind(target);
    }

    protected boolean beforeCall(Object proxy , Method method, Object[] args , final Object resObj) {
        return true;
    }

    protected void afterCall(Object proxy , Method method, Object[] args , final Object resObj) {
    }

    private class Invocation implements InvocationHandler {

        public Object bind(Object _target) {
            return Proxy.newProxyInstance(_target.getClass().getClassLoader() , _target.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object retObj = null;
            if(beforeCall(proxy , method , args , target)) {
                retObj = method.invoke(target  ,args);
            }
            afterCall(proxy , method , args , target);
            return retObj;
        }
    }
}
