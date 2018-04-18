package com.etrans.jt.bluetooth.base;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

/**
 * 单元名称:BaseApplication.java
 * Created by fuxiaolei on 2016/7/4.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/4.
 */
public class BaseApplication extends Application {
    private static final String TAG = BaseApplication.class.getSimpleName();

    private Stack<Activity> activityStack = new Stack<Activity>();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void addActivity(Activity activity) {
        if (null == activityStack) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (null == activityStack) {
            activityStack = new Stack<Activity>();
        }
        activityStack.remove(activity);
    }

    public Activity currentActivity() {
        return activityStack.lastElement();
    }


    public int activitiesCount() {
        return activityStack.size();
    }

    public void finishAllActivity() {
        if (null != activityStack) {
            for (Activity act : activityStack) {
                act.finish();
            }
            activityStack.clear();
        }
    }

    public void quit() {
        finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myTid());
    }
}
